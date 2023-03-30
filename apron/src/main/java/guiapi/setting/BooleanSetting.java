package guiapi.setting;

public class BooleanSetting extends Setting<Boolean> {
	public BooleanSetting(String name) {
		this(name, false);
	}

	public BooleanSetting(String name, Boolean defValue) {
		this.defaultValue = defValue;
		this.values.put("", this.defaultValue);
		this.backendName = name;
	}

	@Override
	public void fromString(String s, String context) {
		this.values.put(context, s.equals("true"));

		if (this.displayWidget != null) {
			this.displayWidget.update();
		}
	}

	@Override
	public Boolean get(String context) {
		if (this.values.get(context) != null) {
			return this.values.get(context);
		} else {
			return this.values.get("") != null ? this.values.get("") : this.defaultValue;
		}
	}

	@Override
	public void set(Boolean v, String context) {
		this.values.put(context, v);

		if (this.parent != null) {
			this.parent.save(context);
		}

		if (this.displayWidget != null) {
			this.displayWidget.update();
		}
	}

	@Override
	public String toString(String context) {
		return this.get(context) ? "true" : "false";
	}
}
