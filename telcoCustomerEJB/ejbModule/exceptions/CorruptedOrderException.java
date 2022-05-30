package exceptions;

public class CorruptedOrderException extends Exception {
	private static final long serialVersionUID = 1L;

	public CorruptedOrderException(String message) {
		super(message);
	}
}
