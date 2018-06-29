package zz.yy.xx.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class SocketClient {

	private static final int SOCKET_TIMEOUT = 10000;

	private static final long DEFAULT_CONN_DELAY = 10000L;
	private static final long READ_DATA_MAX_SIZE = 0x800000L;

	public interface OnStateListener {
		int STATE_CONNECT_ERROR = 1;
		int STATE_READING_ERROR = 2;
		int STATE_SENDING_ERROR = 3;

		void onConnected();

		void onReceive(byte[] data);

		void onClosed(int state);
	}

	private final String mHost;
	private final int mPort;

	private Socket mClient;
	private ExecutorService mExecutor;

	private OnStateListener mListener;

	private volatile boolean isAlready = false;
	private volatile boolean isRunning = false;

	private final ConcurrentLinkedQueue<Object> mQueue;

	private final Runnable mConnectAction = new Runnable() {
		@Override
		public void run() {
			onConnect();
		}
	};

	private final Runnable mReadAction = new Runnable() {

		@Override
		public void run() {
			onDataRead();
		}

	};

	private final Runnable mSendAction = new Runnable() {

		@Override
		public void run() {
			onDataSend();
		}

	};

	private final ThreadFactory mThreadFactory = new SocketThreadFactory() {

		@Override
		public void uncaughtException(Thread t, Throwable e) {
			// do nothing.
		}

	};

	public SocketClient(String host, int port) {
		this.mHost = host;
		this.mPort = port;

		mQueue = new ConcurrentLinkedQueue<>();

		mExecutor = Executors.newFixedThreadPool(2, mThreadFactory);

	}

	public void setOnStateListener(OnStateListener listener) {
		this.mListener = listener;
	}

	public void start() {
		if (mExecutor != null && !mExecutor.isShutdown()) {
			mExecutor.execute(mConnectAction);
		}
	}

	public synchronized void send(byte[] data) {
		if (data == null || data.length < 1) {
			return;
		}

		if (isRunning && mQueue != null) {
			mQueue.offer(data);
		}

		if (mExecutor != null && !mExecutor.isShutdown()) {
			mExecutor.execute(mSendAction);
		}
	}

	public void shutdown() {
		mListener = null;

		disconnect();

		try {
			if (mExecutor != null) {
				mExecutor.shutdown();
				mExecutor = null;
			}
		} catch (Exception e) {
			// do nothing.
		}
	}

	private void disconnect() {

		try {
			isRunning = false;
			mQueue.clear();
			if (mClient != null) {
				mClient.close();
				mClient = null;
			}
		} catch (Exception e) {
			// do nothing.
		}
	}

	private void onConnect() {
		disconnect();
		/* |检查网张连接前是否需要延迟| */
		try {
			if (!isAlready) {
				isAlready = true;
			} else {
				TimeUnit.MILLISECONDS.sleep(DEFAULT_CONN_DELAY);
			}
		} catch (Exception e) {
			// do nothing.
		}

		/* |网络连接| */
		try {
			mClient = new Socket();
			SocketAddress address = new InetSocketAddress(mHost, mPort);
			mClient.connect(address, SOCKET_TIMEOUT);
		} catch (Exception e) {
			// do nothing.
		}

		/* |启动数据传输任务| */
		try {
			if (mClient != null && mExecutor != null 
					&& mClient.isConnected() 
					&& !mExecutor.isShutdown()) {
				isRunning = true;
				mExecutor.execute(mSendAction);
				mExecutor.execute(mReadAction);
			} else {
				isRunning = false;
			}
		} catch (Exception e) {
			// do nothing.
		}

		/* |检查连接状态，成功则通知监听| */
		try {
			if (mListener != null) {
				if (isRunning) {
					mListener.onConnected();
				} else {
					mListener.onClosed(OnStateListener.STATE_CONNECT_ERROR);
				}
			}
		} catch (Exception e) {
			// do nothing.
		}
	}

	private void onDataSend() {
		Throwable t = null;

		try {
			byte[] body;
			byte[] head = new byte[4];
			OutputStream output = mClient.getOutputStream();

			while (!mQueue.isEmpty()) {
				Object ref = mQueue.poll();
				if (!(ref instanceof byte[])) {
					continue;
				}

				body = (byte[]) ref;
				ByteBuffer.wrap(head).putInt(body.length);
				output.write(head);
				output.write(body);
				output.flush();

				System.err.println("send:"+Arrays.toString(body));
			}

			
		} catch (IOException e) {
			t = e;
		} catch (Exception e) {
			t = e;
		} finally {
			if (t != null) {
				disconnect();
				try {
					if (mListener != null) {
						mListener.onClosed(OnStateListener.STATE_SENDING_ERROR);
					}
				} catch (Exception e) {
					// do nothing.
				}
			}
		}
	}

	private void onDataRead() {

		Throwable t = null;

		try {
			int size;
			byte[] body;
			byte[] head = new byte[4];

			InputStream input = mClient.getInputStream();
			while (isRunning) {
				/* |读取数据包头信息| */
				if (read(input, head, head.length) < 1) {
					continue;
				}
				/* |解析数据包大小| */
				size = ByteBuffer.wrap(head).getInt();
				if (size > READ_DATA_MAX_SIZE) {
					/* |数据过大(大于8M)，直接抛出异常| */
					throw new IllegalStateException("Too large data transmission.");
				}
				/* |读取数据包主体| */
				body = new byte[size];
				if (size < 1 || read(input, body, size) < 1) {
					continue;
				}
				/* |分发数据包| */
				try {
					if (mListener != null) {
						mListener.onReceive(body);
					}
				} catch (Exception e) {
					// do nothing.
				}
			}
		} catch (IOException e) {
			t = e;
		} catch (Exception e) {
			t = e;
		} finally {
			if (t != null) {
				disconnect();
				try {
					if (mListener != null) {
						mListener.onClosed(OnStateListener.STATE_READING_ERROR);
					}
				} catch (Exception e) {
					// do nothing.
				}
			}
		}
	}

	private static int read(InputStream is, byte[] swap, int len) throws IOException {
		int read;
		int resultCount = 0;
		while (resultCount < len) {
			read = is.read(swap, resultCount, len - resultCount);
			if (read == -1) {
				throw new IOException("Socket is break off！");
			}
			resultCount += read;
		}
		return resultCount;
	}

	private static abstract class SocketThreadFactory implements ThreadFactory, Thread.UncaughtExceptionHandler {

		private final AtomicInteger threadNumber = new AtomicInteger(1);
		private final ThreadGroup group;

		SocketThreadFactory() {
			SecurityManager s = System.getSecurityManager();
			group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
		}

		@Override
		public Thread newThread(Runnable r) {
			Thread t = new Thread(group, r, "socket-" + threadNumber.getAndIncrement(), 0);
			t.setDaemon(true);
			t.setUncaughtExceptionHandler(this);
			return t;
		}
	}
}
