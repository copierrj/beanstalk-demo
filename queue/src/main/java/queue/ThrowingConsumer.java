package queue;

import java.util.function.Consumer;

@FunctionalInterface
public interface ThrowingConsumer<T> extends Consumer<T> {

	@Override
	default void accept(T t) {
		try {
			acceptWithThrows(t);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	void acceptWithThrows(T t) throws Exception;
}
