package queue;

import java.util.Objects;

import com.dinstone.beanstalkc.BeanstalkClientFactory;
import com.dinstone.beanstalkc.Configuration;
import com.dinstone.beanstalkc.JobProducer;

public class JobGeneratorFactory {

	private String host, tube;
	
	private Integer port, ttr;
	
	private int priority = 0, delay = 0;
	
	public static JobGeneratorFactory newInstance() {
		return new JobGeneratorFactory();
	}
	
	public JobGeneratorFactory host(String host) {
		this.host = Objects.requireNonNull(host, "host must not be null");
		return this;
	}
	
	public JobGeneratorFactory port(int port) {
		this.port = port;
		return this;
	}
	
	public JobGeneratorFactory tube(String tube) {
		this.tube = Objects.requireNonNull(tube, "tube must not be null");
		return this;
	}
	
	public JobGeneratorFactory priority(int priority) {
		this.priority = priority;
		return this;
	}
	
	public JobGeneratorFactory delay(int delay) {
		this.delay = delay;
		return this;
	}
	
	public JobGeneratorFactory timeToRun(int ttr) {
		this.ttr = ttr;
		return this;
	}
	
	public JobGenerator<byte[]> create() {
		Configuration config = new Configuration();
		config.setServiceHost(Objects.requireNonNull(host, "host not set"));
		config.setServicePort(Objects.requireNonNull(port, "port not set"));
		
		BeanstalkClientFactory factory = new BeanstalkClientFactory(config);
		JobProducer producer = factory.createJobProducer(
				Objects.requireNonNull(tube, "tube not set"));
		
		return new JobGenerator<byte[]>(producer, t -> t, priority, delay, 
				Objects.requireNonNull(ttr, "timeToRun not set"));
	}
}
