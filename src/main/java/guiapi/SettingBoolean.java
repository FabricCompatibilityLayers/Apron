package guiapi;

public class SettingBoolean extends Setting<Boolean> {
	public SettingBoolean(String name) {
		this(name, false);
	}

	public SettingBoolean(String name, Boolean defValue) {
		this.defaultValue = defValue;
		this.values.put("", this.defaultValue);
		this.backendName = name;
	}

	public void fromString(String s, String context) {
		this.values.put(context, s.equals("true"));

		if (this.displayWidget != null) {
			this.displayWidget.update();
		}
	}

	public Boolean get(String context) {
		if (this.values.get(context) != null) {
			return this.values.get(context);
		} else {
			return this.values.get("") != null ? this.values.get("") : this.defaultValue;
		}
	}

	public void set(Boolean v, String context) {
		this.values.put(context, v);

		if (this.parent != null) {
			this.parent.save(context);
		}

		if (this.displayWidget != null) {
			this.displayWidget.update();
		}
	}

	public String toString(String context) {
		return this.get(context) ? "true" : "false";
	}
}
