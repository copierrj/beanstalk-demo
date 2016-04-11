import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import queue.JobProcessorFactory;

public class Consumer {
	
	public static void main(String[] args) throws Exception {
		System.out.println("Consumer started");
		
		String host = System.getenv("BEANSTALK_HOST");
		if(host == null) {
			host = "localhost";
		}
		
		Random r = new Random();
		
		JobProcessorFactory
			.newInstance()
			.host(host)
			.tube("demo")
			.touchDelay(1, TimeUnit.SECONDS)
			.create()
				.map(b -> {
					try {
						return new String(b, "utf-8");
					} catch(UnsupportedEncodingException e) {
						throw new RuntimeException(e);
					}
				})
				.consume(s -> {
					try {
						System.out.println("message received: '" + s + "'");
						
						int delay = r.nextInt(10000);
						System.out.println("waiting " + delay + "ms");
						
						Thread.sleep(delay);
						System.out.println("done");
					} catch(InterruptedException e) {
						throw new RuntimeException(e);
					}
				});
	}
}