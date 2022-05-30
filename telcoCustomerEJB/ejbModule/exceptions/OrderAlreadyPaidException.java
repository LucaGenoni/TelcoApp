package exceptions;

public class OrderAlreadyPaidException extends Exception {
	private static final long serialVersionUID = 1L;

	public OrderAlreadyPaidException(String message) {
		super(message);
	}
}
