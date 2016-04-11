import java.io.UnsupportedEncodingException;
import java.util.Random;

import queue.JobGeneratorFactory;

public class Producer {
	
	private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789";
	
	public static void main(String[] args) throws Exception {
		System.out.println("Producer started");
		
		String host = System.getenv("BEANSTALK_HOST");
		if(host == null) {
			host = "localhost";
		}
		
		Random r = new Random();
		
		JobGeneratorFactory
			.newInstance()
			.host(host)
			.port(11300)
			.tube("demo")
			.timeToRun(1)
			.create()
				.map((String s) -> {
					try {
						return s.getBytes("utf-8");
					} catch(UnsupportedEncodingException e) {
						throw new RuntimeException(e);
					}
				})
				.supply(() -> {
					try {
						int delay = r.nextInt(10000);
						System.out.println("waiting " + delay + "ms");
						
						Thread.sleep(delay);
						
						String message = randomString(r, 20);
						System.out.println("supplying message: '" + message + "'");
						
						return message;
					} catch(InterruptedException e) {
						throw new RuntimeException(e);
					}
				});
	}
	
	public static String randomString(Random r, int length) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			sb.append(CHARACTERS.charAt(r.nextInt(CHARACTERS.length())));
		}
		
		return sb.toString();
	}
}