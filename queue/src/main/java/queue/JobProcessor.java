package queue;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import com.dinstone.beanstalkc.Job;
import com.dinstone.beanstalkc.JobConsumer;

public class JobProcessor<T> {
	
	private final JobConsumer jobConsumer;
	
	private final Function<byte[], T> mapper;
	
	private final long touchDelay; 
	
	private final TimeUnit touchTimeUnit;

	JobProcessor(JobConsumer consumer, Function<byte[], T> mapper, long touchDelay, TimeUnit touchTimeUnit) {
		this.jobConsumer = consumer;
		this.mapper = mapper;
		this.touchDelay = touchDelay;
		this.touchTimeUnit = touchTimeUnit;
	}
	
	public <U> JobProcessor<U> map(ThrowingFunction<T, U> mapper) {
		return new JobProcessor<U>(jobConsumer, 
			b -> mapper.apply(this.mapper.apply(b)),
			touchDelay, touchTimeUnit); 
	}
	
	public void consume(ThrowingConsumer<T> consumer) {
		ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(1);
		
		for(;;) {			
			Job job = jobConsumer.reserveJob(0);
			long jobId = job.getId();
			
			System.out.println("job received, jobId: " + jobId);
			
			ScheduledFuture<?> future = executor.scheduleAtFixedRate(() -> {
				System.out.println("touching job");
				jobConsumer.touchJob(jobId);
			}, touchDelay, touchDelay, touchTimeUnit);
			
			consumer.accept(mapper.apply(job.getData()));
			
			future.cancel(false);
			jobConsumer.deleteJob(jobId);
		}
	}
}
