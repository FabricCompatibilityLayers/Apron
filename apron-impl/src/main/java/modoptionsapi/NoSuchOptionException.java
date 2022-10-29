package modoptionsapi;

public class NoSuchOptionException extends RuntimeException {
	public NoSuchOptionException() {
	}

	public NoSuchOptionException(String message) {
		super(message);
	}
}
