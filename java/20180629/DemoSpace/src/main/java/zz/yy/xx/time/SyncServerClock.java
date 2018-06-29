package zz.yy.xx.time;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 同步服务器的时间
 *
 * @author huqiang
 */
public class SyncServerClock {
    private static final String SYNC_NTP_TIMER_SERVER="time3.aliyun.com";

    private static volatile SyncServerClock sInstance;

    private long minusTime = 0; // 系统时间与服务器时间的差值
    private boolean mFirstNotifyApp = true; // 第一次需要广播通知app获取到服务器的时间
    private Timer mRequestTimer;

    @SuppressWarnings("unused")
    public static SyncServerClock newInstance() {
        if (sInstance == null) {
            sInstance = new SyncServerClock();
        }
        return sInstance;
    }

    public static synchronized
    SyncServerClock getInstance() {
        return sInstance;
    }

    public static long getCurrentTime() {
        if (getInstance() != null) {
            return getInstance().getServerTime();
        }
        return System.currentTimeMillis();
    }

    private SyncServerClock() {
    }


    public synchronized void startSynTime() {
        if (mRequestTimer != null) {
            mRequestTimer.cancel();
        } else {
            mRequestTimer = new Timer();
            mRequestTimer.schedule(new GetSerTimeTask(), 0);
        }
    }

    /**
     * 设置当前的设备时间
     **/
    @SuppressWarnings("all")
    private void setCurrentTime(long hostTime) {
        // 必须大于2016年1月1日0时0分0秒 = 1451577600000
        if (hostTime > 1451577600000L) {
            minusTime = hostTime - System.currentTimeMillis();
            System.err.println("Time:同步到服务器时间，minusTime=" + minusTime);
        } else {
            mFirstNotifyApp = false;
        }

        if (mFirstNotifyApp) {

        	System.err.println("==============================");
        	System.err.println("==============================");
        	System.err.println("==============================");
        	System.err.println("==============================");
        	System.err.println("==============================");
        	System.err.println("==============================");
        	
            mFirstNotifyApp = false;
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

    private class GetSerTimeTask extends TimerTask {
        @Override
        public void run() {
            int requestCnt = 0;
            long serverTime = 0;
            do {
                try {
                    serverTime = getTime();
                    if (serverTime > 0) {
                        setCurrentTime(serverTime);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    requestCnt++;
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                        Thread.currentThread().interrupt();
                    }
                }
                if (requestCnt >= 360) {
                    break;
                }
            } while (serverTime == 0);
        }

        private NtpMessage loadNtpMessage(DatagramSocket socket, InetAddress address) {
            int retry = 2;
            int port = 123;
            int serviceStatus = -1;
            NtpMessage ntpMsg = null;
            for (int attempts = 0; attempts <= retry && serviceStatus != 1; attempts++) {
                try {
                    // Send NTP request
                    //
                    byte[] data = new NtpMessage().toByteArray();
                    DatagramPacket outgoing = new DatagramPacket(data, data.length, address, port);
                    socket.send(outgoing);
                    DatagramPacket incoming = new DatagramPacket(data, data.length);
                    socket.receive(incoming);
                    // Validate NTP Response
                    // IOException thrown if packet does not decode as expected.
                    ntpMsg = new NtpMessage(incoming.getData());
                    serviceStatus = 1;
                } catch (IOException ex) {
                    // Ignore, no response received.
                	ex.printStackTrace();
                }
            }
            return ntpMsg;
        }

        /**
         * 同步服务器时间,单位：毫秒
         *
         * @return 服务器时间
         */
        @SuppressWarnings("all")
        private long getTime() throws IOException {

            int timeout = 5000;
            //

            DatagramSocket socket = null;
            NtpMessage ntpMsg = null;
            try {
                InetAddress address = InetAddress.getByName(SYNC_NTP_TIMER_SERVER);
                socket = new DatagramSocket();
                socket.setSoTimeout(timeout); // will force the
                // InterruptedIOException

                ntpMsg = loadNtpMessage(socket, address);
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
    }


}
