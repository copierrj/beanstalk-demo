import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.dinstone.beanstalkc.BeanstalkClientFactory;
import com.dinstone.beanstalkc.Configuration;
import com.dinstone.beanstalkc.JobProducer;

public class Producer {
	
	public static void main(String[] args) throws Exception {
		System.out.println("Producer started");
		
		Configuration config = new Configuration();
	    config.setServiceHost("127.0.0.1");
	    config.setServicePort(11300);
	    
	    BeanstalkClientFactory factory = new BeanstalkClientFactory(config);
	    JobProducer producer = factory.createJobProducer("demo");
		
		try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
			for(;;) {
				System.out.print("> ");
				System.out.flush();
				
				String command = br.readLine().trim();
				
				if(command.equalsIgnoreCase("exit")) {
					break;
				}
				
				if(command.toLowerCase().startsWith("put")) {
					String message = command.substring(4).trim();
					
					long jobId = producer.putJob(0, 0, 1, message.getBytes("utf-8"));
					
					System.out.println("put message: '" + message + "', jobId: " + jobId);
					continue;
				}
				
				System.out.println("unknown command: '" + command + "'");
			}
		}
		
		System.out.println("Producer terminated");
	}
}