package queue;

import java.util.function.Function;

import com.dinstone.beanstalkc.JobProducer;

public class JobGenerator<T> {
	
	private final JobProducer producer;
	
	private final Function<T, byte[]> mapper;
	
	private final int priority, delay, ttr;

	public JobGenerator(JobProducer producer, Function<T, byte[]> mapper, int priority, int delay, int ttr) {
		this.producer = producer;
		this.mapper = mapper;
		this.priority = priority;
		this.delay = delay;
		this.ttr = ttr;
	}
	
	public <U> JobGenerator<U> map(ThrowingFunction<U, T> mapper) {
		return new JobGenerator<U>(producer, u -> this.mapper.apply(mapper.apply(u)),
				priority, delay, ttr);
	}
	
	public void supply(ThrowingSupplier<T> supplier) {
		for(;;) {
			supply(supplier.get());
		}
	}
	
	public void supply(T message) {
		long jobId = producer.putJob(priority, delay, ttr, mapper.apply(message));
		System.out.println("job put in queue, jobId: " + jobId);
	}
}
