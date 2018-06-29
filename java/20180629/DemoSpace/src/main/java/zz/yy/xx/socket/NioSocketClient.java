package zz.yy.xx.socket;

import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Socket连接客户端
 * 
 * <pre>
 * 实现分包，以包头的4个字节标识当前包体的大小.
 * 以{@linkplain SocketChannel}实现，
 * 以{@linkplain Selector}实现单线程读写操作
 * </pre>
 * 
 * <pre>
 * SocketChannel 没有超时概念，超时逻辑需要自己轮循检查
 * </pre>
 *
 */
public class NioSocketClient {

	private static final int SOCKET_TIMEOUT = 10000;
	private static final int BUFFER_MAX_SIZE = 0x400000;
	private static final long CONNECT_PERIOD = 30000L;
	
	
	private static final AtomicInteger sThreadNumber 
			= new AtomicInteger(1);

	/**
	 * 数据接收监听器
	 */
	public interface OnReceiveListener {
		void onReceive(byte[] data);
	}

	private final String mHost;
	private final int mPort;

	private Selector mSelector;
	private SocketChannel mChannel;

	private OnReceiveListener mListener;

	private final ByteBuffer mBuffer;
	private final ConcurrentLinkedQueue<Object> mQueue;

	private boolean mForceStop = false;
	private boolean mUseLazyConnect = false;
	private volatile boolean mAlive = false;

	/**
	 * 当前线程主任务对象
	 */
	private final Runnable mLoopAction = new Runnable() {
		@Override
		public void run() {
			selfLoopAction();
		}
	};

	/**
	 * 当前线程未捕获异常处理回调实例
	 */
	private final UncaughtExceptionHandler mExceptHandler = 
			new UncaughtExceptionHandler() {
		@Override
		public void uncaughtException(Thread t, Throwable e) {
			e.printStackTrace(System.out);
			onUncaughtException(t, e);
		}
	};

	/**
	 * Socket 客户端实现
	 * @param host 主机地址
	 * @param port 主机端口
	 */
	public NioSocketClient(String host, int port) {
		this.mHost = host;
		this.mPort = port;
		
		mQueue = new ConcurrentLinkedQueue<>();
		mBuffer = ByteBuffer.allocateDirect(0x80000);
	}
	
	/**
	 * 设置数据接收器
	 * @param listener 据接收器实例
	 */
	public void setOnReceiveListener( OnReceiveListener listener) {
		this.mListener = listener;
	}

	/**
	 * 初初化Socket连接
	 */
	public void init() {
		Thread thread = new Thread(mLoopAction, "sock-" 
				+ sThreadNumber.getAndIncrement());
		thread.setUncaughtExceptionHandler(mExceptHandler);
		thread.setDaemon(true);
		thread.start();
		
		this.mAlive = true;
		this.mForceStop = false;
	}

	/**
	 * 检查当前Socket连接是否正常
	 * 
	 * @return 是否正常
	 */
	public final synchronized boolean isAlive() {
		return mAlive && mSelector != null && mSelector.isOpen() && 
				mChannel != null && mChannel.isConnected();
	}

	/**
	 * 向Socket远端发送数据
	 * @param data 数据实例(字节数据)
	 */
	public synchronized void send(byte[] data) {
		if (data == null || data.length < 1) {
			return;
		}
		if (mQueue != null) {
			mQueue.add(data);
		}
		if (mSelector != null && mSelector.isOpen()) {
			mSelector.wakeup();
		}
	}
	
	/**
	 * 退出当前任务
	 */
	public void exit() {
		terminate();

		mChannel = null;
		mSelector = null;

		if (mQueue != null && !mQueue.isEmpty()) {
			mQueue.clear();
		}
		
		mAlive = false;
		mForceStop = true;
	}

	/**
	 * 线程内部未捕获异常处理
	 * @param t 异常线和
	 * @param e 异常对象
	 */
	private void onUncaughtException(Thread t, Throwable e) {
		if (!mForceStop) {
			mUseLazyConnect = true;
			init();
		}
	}
	/**
	 * 当前线程主循环体
	 */
	private void selfLoopAction() {
		/*|检查懒启动延迟|*/
		lazyStartupDelay();
		
		try {
			/*|网络连接|*/
			connect();
			/*|主循环体|*/
			while(mChannel!=null&&mChannel.isConnectionPending()) {
				/* |检查数据接收| */
				checkReceiverData();
				/* |检查数据发送| */
				checkSendData();
			}

			if(mAlive) {
				/*|非正常退出主循环，抛出异常|*/
				throw new IllegalStateException("Connection abnormal disconnection");
			}

		} catch (IOException e) {
			/*|将IO异常转为运行时异常抛出|*/
			throw new RuntimeException(e);
		} finally {
			this.mAlive = false;
		}
	}
	
	/**
	 * 检查是否使用懒启动延迟
	 */
	private void lazyStartupDelay() {
		if (mUseLazyConnect) {
			try {
				TimeUnit.MILLISECONDS.sleep(CONNECT_PERIOD);
			} catch (InterruptedException e) {
				// do nothing.
			}
		}
	}

	/**
	 * 新建Socket连接
	 */
	private void connect() throws IOException {

		terminate();

		mSelector = Selector.open();

		mChannel = SocketChannel.open();
		mChannel.configureBlocking(true);
		mChannel.socket().setSoTimeout(SOCKET_TIMEOUT);
		mChannel.connect(new InetSocketAddress(mHost, mPort));

		mChannel.configureBlocking(false);
		mChannel.register(mSelector, SelectionKey.OP_READ);
		
	}
	
	/**
	 * 关掉当前Socket连接
	 */
	private void terminate() {
		if (mSelector != null) {
			try {
				mSelector.close();
			} catch (IOException e) {
				// do nothing.
			}
		}
		if (mChannel != null) {
			try {
				mChannel.close();
			} catch (IOException e) {
				// do nothing.
			}
		}
	}

	/**
	 * 检查发送数据
	 * @throws IOException 异常
	 */
	private void checkSendData() throws IOException {
		while (mChannel.isConnected() && !mQueue.isEmpty()) {
			Object ref = mQueue.poll();
			if (ref instanceof byte[]) {
				write((byte[]) ref);
			}
		}
	}

	/**
	 * 检查数据接收
	 * @throws IOException 异常
	 */
	private void checkReceiverData() throws IOException {
		if (mSelector != null && mSelector.select(SOCKET_TIMEOUT) > 0) {
			Set<SelectionKey> keys = mSelector.selectedKeys();
			Iterator<SelectionKey> it = keys.iterator();

			SelectionKey key;
			while (it.hasNext()) {
				key = it.next();
				if (key.isReadable()) {
					read(key);
				}
				it.remove();
			}
		}
	}

	

	/**
	 * 读收数据
	 * @param channel Socket通道
	 * @param buffer 缓冲区
	 * @throws IOException 异常
	 */
	private void checkRead(ReadableByteChannel channel, ByteBuffer buffer)
			throws IOException {
		do {
			if (channel.read(buffer) == -1) {
				throw new IOException(channel + " channel is closed.");
			}
		} while (buffer.hasRemaining());
	}

	/**
	 * 发送数据
	 * @param channel Socket通道
	 * @param buffer 缓冲区
	 * @throws IOException 异常
	 */
	private void checkSend(WritableByteChannel channel, ByteBuffer buffer)
			throws IOException {
		do {
			if (channel.write(buffer) == -1) {
				throw new IOException(channel + " channel is closed.");
			}
		} while (buffer.hasRemaining());
	}

	/**
	 * 写入数据
	 * @param data 数据对象(字节数组)
	 * @throws IOException 异常
	 */
	private void write(byte[] data) throws IOException {
		if (data == null || data.length < 1) {
			return;
		}

		int offset;
		int useful;
		int pos = 0;

		ByteBuffer buffer = mBuffer;
		buffer.clear();

		int len = data.length;
		buffer.putInt(len);
		do {
			useful = buffer.limit() - buffer.position();
			offset = len - pos;
			if (useful < offset) {
				offset = useful;
			}

			buffer.put(data, pos, offset);
			pos += offset;

			buffer.flip();
			checkSend(mChannel, buffer);
			buffer.clear();

		} while (pos < len);
		
		System.err.println("send:"+Arrays.toString(data));
	}

	/**
	 * 读取数据
	 * @param key 选择器类型
	 * @throws IOException 异常
	 */
	private void read(SelectionKey key) throws IOException {
		if (key == null || key.channel() != mChannel) {
			return;
		}

		int offset;
		byte[] data;
		int pos = 0;

		ByteBuffer buffer = mBuffer;
		buffer.clear();

		buffer.limit(4);
		checkRead(mChannel, buffer);
		buffer.flip();

		int len = buffer.getInt();
		if (len > BUFFER_MAX_SIZE) {
			throw new IOException("buffer size already overflow.");
		} else {
			data = new byte[len];
		}

		do {
			buffer.clear();
			offset = len - pos;
			if (buffer.limit() > offset) {
				buffer.limit(offset);
			}

			checkRead(mChannel, buffer);

			buffer.flip();
			buffer.get(data, pos, buffer.limit());
			pos += buffer.limit();
		} while (pos < len);

		if (mListener != null) {
			mListener.onReceive(data);
		}
		
		System.err.println("read:"+Arrays.toString(data));
	}

}
