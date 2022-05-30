package exceptions;

public class NoSuchOrderException extends Exception {
	private static final long serialVersionUID = 1L;

	public NoSuchOrderException(String message) {
		super(message);
	}
}
