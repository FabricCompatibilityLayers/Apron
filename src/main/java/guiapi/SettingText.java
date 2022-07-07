package guiapi;

public class SettingText extends Setting<String> {
	public SettingText(String title, String defaulttext) {
		this.values.put("", defaulttext);
		this.defaultValue = defaulttext;
		this.backendName = title;
	}

	public void fromString(String s, String context) {
		this.values.put(context, s);

		if (this.displayWidget != null) {
			this.displayWidget.update();
		}
	}

	public String get(String context) {
		if (this.values.get(context) != null) {
			return this.values.get(context);
		} else {
			return this.values.get("") != null ? this.values.get("") : this.defaultValue;
		}
	}

	public void set(String v, String context) {
		this.values.put(context, v);

		if (this.parent != null) {
			this.parent.save(context);
		}

		if (this.displayWidget != null) {
			this.displayWidget.update();
		}
	}

	public String toString(String context) {
		return this.get(context);
	}
}
