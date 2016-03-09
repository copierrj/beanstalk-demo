import com.dinstone.beanstalkc.BeanstalkClientFactory;
import com.dinstone.beanstalkc.Configuration;
import com.dinstone.beanstalkc.Job;
import com.dinstone.beanstalkc.JobConsumer;

public class Consumer {
	
	public static void main(String[] args) throws Exception {
		System.out.println("Consumer started");
		
		String host = System.getenv("BEANSTALK_HOST");
		if(host == null) {
			host = "localhost";
		}
		
		Configuration config = new Configuration();
	    config.setServiceHost(host);
	    config.setServicePort(11300);
	    
	    BeanstalkClientFactory factory = new BeanstalkClientFactory(config);
	    JobConsumer consumer = factory.createJobConsumer("demo");
		
		for(;;) {
			Job job = consumer.reserveJob(0);
			long jobId = job.getId();
			
			String message = new String(job.getData(), "utf-8");
			System.out.println("message received: '" + message + "', jobId: " + jobId);
			
			consumer.deleteJob(jobId);
		}
	}
}