package modoptionsapi;

public class KeyAlreadyBoundException extends IllegalStateException {
	public KeyAlreadyBoundException(Integer key) {
		super("Key " + ModKeyOption.getKeyName(key) + " is already bound");
	}
}
