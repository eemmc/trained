package zz.yy.xx.message;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class MessageProcessor {
	private static final String THREAD_NAME = "mps";
	private static final int CORE_POOL_SIZE = 5;

	private final ExecutorService mExecutor;

	private final ThreadFactory mThreadFactory;

	private final DelayQueue<DelegateRunnable> mQueue;

	private final Runnable mHandlerAction = new Runnable() {

		@Override
		public void run() {
			onHandlerLooper();
		}
	};

	public MessageProcessor() {

		mQueue = new DelayQueue<>();

		mThreadFactory = new NamedThreadFactory(THREAD_NAME);

		mExecutor = Executors.newFixedThreadPool(CORE_POOL_SIZE, mThreadFactory);
		
		
		mExecutor.execute(mHandlerAction);
	}

	private void onHandlerLooper() {

		DelegateRunnable target;

		try {
			Thread thread = Thread.currentThread();
			thread.setName(THREAD_NAME + "-dis");

			while ((target = mQueue.take()) != null) {
				if (mExecutor != null && !mExecutor.isShutdown()) {
					mExecutor.execute(target);
				}
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public void post(Runnable runnable) {
		this.post(runnable, 0L, -1L);
	}

	public void post(Runnable runnable, long delayMills) {
		this.post(runnable, delayMills, -1L);
	}

	public void post(Runnable runnable, long delayMills, long periodMills) {

	}

	public synchronized void remove(Runnable runnable) {
		mQueue.remove(runnable);
	}

	@SuppressWarnings({ "unused" })
	private static class DelegateRunnable implements Runnable, Delayed {
		private final long time;
		private final long delay;
		private final long period;

		private Runnable target;

		DelegateRunnable(Runnable runnable, long delay, long period) {

			this.time = TimeUnit.MILLISECONDS.toNanos(delay) + System.nanoTime();

			this.delay = delay;
			this.period = period;

			this.target = runnable;
		}

		final long delay() {
			return delay;
		}

		final long period() {
			return period;
		}

		@Override
		public void run() {
			if (target != null) {
				target.run();
			}

		}

		@Override
		public final int hashCode() {
			if (target != null) {
				return System.identityHashCode(target);
			} else {
				return super.hashCode();
			}
		}

		@Override
		public final boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;

			return this.hashCode() == obj.hashCode();
		}

		@Override
		public int compareTo(Delayed o) {
			return Long.compare(this.getDelay(TimeUnit.MILLISECONDS), o.getDelay(TimeUnit.MILLISECONDS));
		}

		@Override
		public long getDelay(TimeUnit unit) {
			return unit.convert(this.time - System.nanoTime(), TimeUnit.NANOSECONDS);
		}

		static int getHashCode(Runnable runnable) {
			if (runnable instanceof DelegateRunnable) {
				return runnable.hashCode();
			} else {
				return System.identityHashCode(runnable);
			}
		}
	}

	public static class NamedThreadFactory implements ThreadFactory, Thread.UncaughtExceptionHandler {
		private final AtomicInteger number = new AtomicInteger(1);
		private final ThreadGroup group;
		private final String name;

		public NamedThreadFactory(String name) {
			SecurityManager s = System.getSecurityManager();
			group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
			this.name = name;
		}

		@Override
		public void uncaughtException(Thread t, Throwable e) {
			System.err.println(t);
			e.printStackTrace(System.err);
		}

		@Override
		public Thread newThread(Runnable r) {
			String name = this.name + "-" + number.getAndIncrement();
			Thread thread = new Thread(group, r, name, 0);
			thread.setUncaughtExceptionHandler(this);
			thread.setDaemon(true);
			return thread;
		}

	}

}
