package modoptionsapi;

import java.util.MissingResourceException;

public class MOMissingModException extends MissingResourceException {
	public MOMissingModException(String msg) {
		super(msg, "", "");
	}
}
