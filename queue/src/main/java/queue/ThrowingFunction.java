package queue;

import java.util.function.Function;

@FunctionalInterface
public interface ThrowingFunction<T, R> extends Function<T, R> {

	@Override
	default R apply(T t) {
		try {
			return applyWithThrows(t);
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	R applyWithThrows(T t) throws Exception;
}
