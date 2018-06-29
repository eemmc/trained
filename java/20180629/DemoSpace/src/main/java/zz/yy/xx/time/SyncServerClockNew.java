package zz.yy.xx.time;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 同步服务器的时间
 *
 * @author huqiang
 */
public class SyncServerClockNew {

    private static final String[] SYNC_NTP_TIMER_SERVERS = {
            "ntp1.aliyun.com", "ntp2.aliyun.com",
            "ntp3.aliyun.com", "ntp4.aliyun.com",
            "ntp5.aliyun.com", "ntp6.aliyun.com",
            "ntp7.aliyun.com"
    };

    private static volatile SyncServerClockNew sInstance;


    private final Random mRandom = new Random();


    private long minusTime = 0; // 系统时间与服务器时间的差值

    private int mTaskIndex = 0;

    private ScheduledExecutorService mExecutor;

    /*|第一次需要广播通知app获取到服务器的时间|*/
    private volatile boolean mFirstNotifyApp = true;

    public static SyncServerClockNew newInstance() {
        if (sInstance == null) {
            sInstance = new SyncServerClockNew();
        }
        return sInstance;
    }

    public static synchronized
    SyncServerClockNew getInstance() {
        return sInstance;
    }

    public static long getCurrentTime() {
        if (getInstance() != null) {
            return getInstance().getServerTime();
        }
        return System.currentTimeMillis();
    }
    
    private SyncServerClockNew() {
    	
    }


    public synchronized void startSynTime() {
        if (mExecutor != null && !mExecutor.isShutdown()) {
            mExecutor.shutdown();
        }

        this.mTaskIndex = 0;
        this.mFirstNotifyApp = true;
        mExecutor = Executors.newScheduledThreadPool(3);

        trySendSyncTimeTask(mExecutor, 3);
    }

    /**
     * 尝试发送同步时间任务
     *
     * @param executor 线程池
     * @param count    单次任务数据
     */
    private void trySendSyncTimeTask(ScheduledExecutorService executor, int count) {
        if (!mFirstNotifyApp || count <= 0 || mExecutor == null
                || mExecutor.isShutdown()) {
            return;
        }
        int bound = SYNC_NTP_TIMER_SERVERS.length;
        int index = mRandom.nextInt(bound);
        for (int i = 0; i < count; i++) {
            String host = SYNC_NTP_TIMER_SERVERS[(index + i) % bound];
            executor.execute(new SyncTimeTask(this.mTaskIndex, host,executor));
            ++this.mTaskIndex;
        }
    }

    /**
     * 设置当前的设备时间
     **/
    @SuppressWarnings("all")
    private void setCurrentTime(long hostTime) {
        // 必须大于2016年1月1日0时0分0秒 = 1451577600000
        if (mFirstNotifyApp && hostTime > 1451577600000L) {
            minusTime = hostTime - System.currentTimeMillis();
            System.err.println("Time:同步到服务器时间，minusTime=" + minusTime);

            mFirstNotifyApp = false;

            System.err.println("==============================");
        	System.err.println("==============================");
        	System.err.println("==============================");
        	System.err.println("==============================");
        	System.err.println("==============================");
        	System.err.println("==============================");
        	
            
            /*|同步完成后，直接关闭线程池|*/
            if (mExecutor != null && !mExecutor.isShutdown()) {
                mExecutor.shutdown();
            }
        }
    }

    /**
     * 获取当前的服务器时间，若没有服务器时间，则返回系统时间
     *
     * @return 当前时间戳
     */
    private long getServerTime() {
        if (minusTime == 0) {
            return System.currentTimeMillis();
        } else {
            return minusTime + System.currentTimeMillis();
        }
    }


    private static class SyncTimeTask implements Runnable {
        private int mIndex;
        private final String mHost;
        
        private ScheduledExecutorService mExecutor; 

        private SyncTimeTask(int index, String host,ScheduledExecutorService executor) {
            this.mIndex = index;
            this.mHost = host;
            this.mExecutor=executor;
        }

        /**
         * 同步服务器时间,单位：毫秒
         *
         * @return 服务器时间
         */
        @SuppressWarnings("all")
        private long getTime(InetAddress address) throws IOException {
            DatagramSocket socket = null;
            NtpMessage ntpMsg = null;
            try {

                socket = new DatagramSocket();
                socket.setSoTimeout(2000);
                //
                byte[] data = new NtpMessage().toByteArray();
                DatagramPacket outgoing = new DatagramPacket(data, data.length, address, 123);
                socket.send(outgoing);
                DatagramPacket incoming = new DatagramPacket(data, data.length);
                socket.receive(incoming);
                //
                ntpMsg = new NtpMessage(incoming.getData());
            } finally {
                if (socket != null) {
                    socket.close();
                }
            }

            if (ntpMsg != null && ntpMsg.transmitTimestamp > 0) {
                return (Math.round(ntpMsg.transmitTimestamp) - 2208988800L) * 1000L;
            }
            return 0L;
        }

        @Override
        public void run() {
            InetAddress address = null;
            try {
                address = InetAddress.getByName(mHost);
                Long serverTime = getTime(address);
                
                getInstance().setCurrentTime(serverTime);
            } catch (Exception e) {
                System.err.println("socket->host:" + mIndex + ":" + address + ":" + e);
                
                if(this.mExecutor!=null&&!this.mExecutor.isShutdown()) {
                	this.mExecutor.schedule(this, 5, TimeUnit.SECONDS);
                }
            }
        }

    }

}
