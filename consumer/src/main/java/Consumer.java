import com.surftools.BeanstalkClient.Client;
import com.surftools.BeanstalkClient.Job;
import com.surftools.BeanstalkClientImpl.ClientImpl;

public class Consumer {
	
	public static void main(String[] args) throws Exception {
		System.out.println("Consumer started");
		
		Client client = new ClientImpl();
		client.useTube("demo");
		
		for(;;) {
			Job job = client.reserve(null);
			
			String message = new String(job.getData(), "utf-8");
			System.out.println("message received: '" + message + "'");
			
			client.delete(job.getJobId());
		}
	}
}