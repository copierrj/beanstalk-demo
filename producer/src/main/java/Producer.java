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
			.tube("demo")
			.timeToRun(2)
			.create()
				.map((String s) -> s.getBytes("utf-8"))
				.supply(() -> {
					int delay = r.nextInt(10000);
					System.out.println("waiting " + delay + "ms");
					
					Thread.sleep(delay);
					
					String message = randomString(r, 20);
					System.out.println("supplying message: '" + message + "'");
					
					return message;
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