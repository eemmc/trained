package zz.yy.xx.rx.java;

import org.junit.Test;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class Demo01 {
	
	
	@Test
	public void demo01() {
		Flowable.<String>just("Hello World")
		.subscribe(System.out::println);
	}

	@Test
	public void demo02() {
		Flowable.<String>just("Hello World")
		.subscribe(new Consumer<String>() {

			@Override
			public void accept(String t) throws Exception {
				System.err.println(t);
			}
			
		});
	}
	
	
	@Test
	public void demo03() {
		Observable.create(emitter->{
			while(!emitter.isDisposed()) {
				long time=System.currentTimeMillis();
				emitter.onNext(time);
				if(time%2!=0) {
					emitter.onError(new IllegalStateException("Odd millisecond!"));
					break;
				}
			}
		}).subscribe(System.out::println,Throwable::printStackTrace);
	}
	
	
	@Test
	public void demo04() {
		Flowable.<String>fromCallable(()->{
			Thread.sleep(1000L);
			return "Done";
		}).subscribeOn(Schedulers.io())
		.observeOn(Schedulers.single())
		.subscribe(System.err::println,Throwable::printStackTrace);
	}
	
	
	@Test
	public void Demo05() {
		Observable.<String>create(s -> {
			s.onNext("Hello World!");
			s.onComplete();
		}).subscribeOn(Schedulers.io()).subscribe(hello -> {
			System.err.println(hello);
		});
	}

}
