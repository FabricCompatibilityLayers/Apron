package modoptionsapi;

public class IncompatibleOptionTypeException extends RuntimeException {
	public IncompatibleOptionTypeException(String msg) {
		super(msg);
	}

	public IncompatibleOptionTypeException() {
	}
}
