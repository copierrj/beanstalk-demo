package queue;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import com.dinstone.beanstalkc.BeanstalkClientFactory;
import com.dinstone.beanstalkc.Configuration;
import com.dinstone.beanstalkc.JobConsumer;

public class JobProcessorFactory {
	
	private String host, tube;
			
	private int port = 11300;
	
	private Long touchDelay;
		
	JobProcessorFactory() {
		
	}
	
	public static JobProcessorFactory newInstance() {
		return new JobProcessorFactory();
	}
	
	public JobProcessorFactory host(String host) {
		this.host = Objects.requireNonNull(host, "host must not be null");
		return this;
	}
	
	public JobProcessorFactory port(int port) {
		this.port = port;
		return this;
	}
	
	public JobProcessorFactory tube(String tube) {
		this.tube = Objects.requireNonNull(tube, "tube must not be null");
		return this;
	}
	
	public JobProcessorFactory touchDelay(long delay) {
		this.touchDelay = delay;
		return this;
	}
	
	public JobProcessor<byte[]> create() {
		Configuration config = new Configuration();
		config.setServiceHost(Objects.requireNonNull(host, "host not set"));
		config.setServicePort(port);
		
		BeanstalkClientFactory factory = new BeanstalkClientFactory(config);
		JobConsumer consumer = factory.createJobConsumer(
				Objects.requireNonNull(tube, "tube not set"));
		
		return new JobProcessor<byte[]>(consumer, t -> t,
				Objects.requireNonNull(touchDelay, "touchDelay not set"),
				TimeUnit.SECONDS);
	}
}