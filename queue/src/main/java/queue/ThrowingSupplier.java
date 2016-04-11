package queue;

import java.util.function.Supplier;

@FunctionalInterface
public interface ThrowingSupplier<T> extends Supplier<T> {

	@Override
	default T get() {
		try {
			return getWithThrows();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	T getWithThrows() throws Exception;
}
