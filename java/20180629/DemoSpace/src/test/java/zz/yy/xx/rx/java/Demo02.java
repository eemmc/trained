package zz.yy.xx.rx.java;

import org.junit.Test;

import io.reactivex.Observable;

public class Demo02 {

	@Test
	public void demo01() throws Exception{
		Observable.range(5, 3).subscribe(i->{
			System.err.println(i);
		});
	}
	
	@Test
	public void demo02() throws Exception {
		Observable.<Integer>create(s -> {
			s.onNext(5);
			s.onNext(6);
			s.onNext(7);
			s.onComplete();
			System.err.println("Complete.");
		}).subscribe(i -> {
			System.err.println("Elem:" + i);
		});
	}
}
