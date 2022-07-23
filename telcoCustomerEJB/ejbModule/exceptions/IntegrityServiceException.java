package exceptions;

public class IntegrityServiceException extends Exception {
	private static final long serialVersionUID = 1L;

	public IntegrityServiceException(String message) {
		super(message);
	}
}
