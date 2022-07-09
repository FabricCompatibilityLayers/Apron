package guiapi.setting;

import guiapi.ModSettings;

public class IntSetting extends Setting<Integer> {
	public int maximumValue;
	public int minimumValue;
	public int stepValue;

	public IntSetting(String title) {
		this(title, 0, 0, 1, 100);
	}

	public IntSetting(String title, int defValue) {
		this(title, defValue, 0, 1, 100);
	}

	public IntSetting(String title, int defValue, int minValue, int maxValue) {
		this(title, defValue, minValue, 1, maxValue);
	}

	public IntSetting(String title, int defValue, int minValue, int stepValue, int maxValue) {
		this.values.put("", defValue);
		this.defaultValue = defValue;
		this.minimumValue = minValue;
		this.stepValue = stepValue;
		this.maximumValue = maxValue;
		this.backendName = title;

		if (this.minimumValue > this.maximumValue) {
			int t = this.minimumValue;
			this.minimumValue = this.maximumValue;
			this.maximumValue = t;
		}
	}

	@Override
	public void fromString(String s, String context) {
		this.values.put(context, Integer.valueOf(s));

		if (this.displayWidget != null) {
			this.displayWidget.update();
		}

		ModSettings.dbgout("fromstring " + s);
	}

	@Override
	public Integer get(String context) {
		if (this.values.get(context) != null) {
			return this.values.get(context);
		} else {
			return this.values.get("") != null ? this.values.get("") : this.defaultValue;
		}
	}

	@Override
	public void set(Integer v, String context) {
		ModSettings.dbgout("set " + v);

		if (this.stepValue > 1) {
			this.values.put(context, (int) ((float) Math.round((float) v.intValue() / (float) this.stepValue) * (float) this.stepValue));
		} else {
			this.values.put(context, v);
		}

		if (this.parent != null) {
			this.parent.save(context);
		}

		if (this.displayWidget != null) {
			this.displayWidget.update();
		}
	}

	@Override
	public String toString(String context) {
		return "" + this.get(context);
	}
}
