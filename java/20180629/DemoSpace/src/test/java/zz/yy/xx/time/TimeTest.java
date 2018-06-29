package zz.yy.xx.time;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class TimeTest {
	
	
	
	@Test
	public void demo01() throws Exception {

		SyncServerClock.newInstance().startSynTime();

		TimeUnit.SECONDS.sleep(300);

	}
	
	
	@Test
	public void demo02() throws Exception {
		SyncServerClockNew.newInstance().startSynTime();

		TimeUnit.SECONDS.sleep(300);
	}

}
