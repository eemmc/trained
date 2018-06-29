package zz.yy.xx.socket;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.bihu.singledevice.push.Driver;


public class SocketTest {
	
	
	@Test
	public void demo01() throws Exception{
		
		
		Driver.GpsInfo gps = Driver.GpsInfo.newBuilder()
				.setDeviceId("CC7314AD8D87")
				.setLng("121.6625976563").setLat("31.4204600497")
				.build();

		final Driver.ClientMessage message = Driver.ClientMessage.newBuilder()
				.setType(4).setGpsInfo(gps)
				.build();
		
		
		final SocketClient socket=new SocketClient("t214.socket.behuh.com",3001);
		socket.setOnStateListener(new SocketClient.OnStateListener() {
			
			@Override
			public void onConnected() {
				socket.send(message.toByteArray());
			}
			
			
			@Override
			public void onReceive(byte[] data) {
				
				System.err.println(Arrays.toString(data));
			}
			
			
			
			@Override
			public void onClosed(int state) {
				System.err.println("state:"+state);
				
			}
		});
		
		socket.start();
		
		
		TimeUnit.SECONDS.sleep(10);
		
		for(int i=0;i<10;i++) {
			socket.send(message.toByteArray());
			TimeUnit.SECONDS.sleep(5);
		}
		
		TimeUnit.SECONDS.sleep(120);
		
	}


}
