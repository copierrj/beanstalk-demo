import java.util.Random;

import com.dinstone.beanstalkc.BeanstalkClientFactory;
import com.dinstone.beanstalkc.Configuration;
import com.dinstone.beanstalkc.JobProducer;

public class Producer {
	
	private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789";
	
	public static void main(String[] args) throws Exception {
		System.out.println("Producer started");
		
		String host = System.getenv("BEANSTALK_HOST");
		if(host == null) {
			host = "localhost";
		}
		
		Configuration config = new Configuration();
		config.setServiceHost(host);
		config.setServicePort(11300);
		
		BeanstalkClientFactory factory = new BeanstalkClientFactory(config);
		JobProducer producer = factory.createJobProducer("demo");
		
		Random r = new Random();
		
		for(;;) {
			int delay = r.nextInt(10000);
			System.out.println("waiting " + delay + "ms");
			
			Thread.sleep(delay);
			
			String message = randomString(r, 20);
			long jobId = producer.putJob(0, 0, 2, message.getBytes("utf-8"));
			
			System.out.println("put message: '" + message + "', jobId: " + jobId);
		}
	}
	
	public static String randomString(Random r, int length) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			sb.append(CHARACTERS.charAt(r.nextInt(CHARACTERS.length())));
		}
		
		return sb.toString();
	}
}