package guiapi.setting;

import guiapi.ModSettings;

public class MultiSetting extends Setting<Integer> {
	public String[] labelValues;

	public MultiSetting(String title, int defValue, String... labelValues) {
		if (labelValues.length != 0) {
			this.values.put("", defValue);
			this.defaultValue = defValue;
			this.labelValues = labelValues;
			this.backendName = title;
		}
	}

	public MultiSetting(String title, String... labelValues) {
		this(title, 0, labelValues);
	}

	@Override
	public void fromString(String s, String context) {
		int x = -1;

		for (int i = 0; i < this.labelValues.length; ++i) {
			if (this.labelValues[i].equals(s)) {
				x = i;
			}
		}

		if (x != -1) {
			this.values.put(context, x);
		} else {
			this.values.put(context, Float.valueOf(s).intValue());
		}

		ModSettings.dbgout("fromstring multi " + s);

		if (this.displayWidget != null) {
			this.displayWidget.update();
		}
	}

	@Override
	public Integer get(String context) {
		if (this.values.get(context) != null) {
			return this.values.get(context);
		} else {
			return this.values.get("") != null ? this.values.get("") : this.defaultValue;
		}
	}

	public String getLabel() {
		return this.labelValues[this.get()];
	}

	public String getLabel(String context) {
		return this.labelValues[this.get(context)];
	}

	public void next() {
		this.next(ModSettings.currentContext);
	}

	public void next(String context) {
		int tempvalue = this.get(context) + 1;

		while (tempvalue >= this.labelValues.length) {
			tempvalue -= this.labelValues.length;
		}

		this.set(tempvalue, context);
	}

	@Override
	public void set(Integer v, String context) {
		this.values.put(context, v);

		if (this.parent != null) {
			this.parent.save(context);
		}

		if (this.displayWidget != null) {
			this.displayWidget.update();
		}
	}

	public void set(String v) {
		this.set(v, ModSettings.currentContext);
	}

	public void set(String v, String context) {
		int x = -1;

		for (int i = 0; i < this.labelValues.length; ++i) {
			if (this.labelValues[i].equals(v)) {
				x = i;
			}
		}

		if (x != -1) {
			this.set(x, context);
		}
	}

	@Override
	public String toString(String context) {
		return this.labelValues[this.get(context)];
	}
}
