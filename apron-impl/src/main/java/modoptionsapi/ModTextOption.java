package modoptionsapi;

public class ModTextOption extends ModOption<String> {
	private int maxLength = 0;

	public ModTextOption(String name) {
		this(name, 0);
	}

	public ModTextOption(String name, Integer maxLen) {
		this(name, maxLen.intValue());
	}

	public ModTextOption(String name, int maxLen) {
		this.name = name;
		this.setGlobalValue("");
		this.setMaxLength(maxLen);
	}

	public void setMaxLength(int maxlen) {
		if (maxlen < 0) {
			maxlen = 0;
		}

		this.maxLength = maxlen;
	}

	public void setMaxLength(Integer maxlen) {
		if (maxlen < 0) {
			maxlen = new Integer(0);
		}

		this.maxLength = maxlen;
	}

	public int getMaxLength() {
		return this.maxLength;
	}
}
