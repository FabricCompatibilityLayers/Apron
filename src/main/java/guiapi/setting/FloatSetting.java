package guiapi.setting;

public class FloatSetting extends Setting<Float> {
	public float maximumValue;
	public float minimumValue;
	public float stepValue;

	public FloatSetting(String title) {
		this(title, 0.0F, 0.0F, 0.1F, 1.0F);
	}

	public FloatSetting(String title, float defValue) {
		this(title, defValue, 0.0F, 0.1F, 1.0F);
	}

	public FloatSetting(String title, float defValue, float minValue, float maxValue) {
		this(title, defValue, minValue, 0.1F, maxValue);
	}

	public FloatSetting(String title, float defValue, float minValue, float stepValue, float maxValue) {
		this.values.put("", defValue);
		this.defaultValue = defValue;
		this.minimumValue = minValue;
		this.stepValue = stepValue;
		this.maximumValue = maxValue;
		this.backendName = title;

		if (this.minimumValue > this.maximumValue) {
			float t = this.minimumValue;
			this.minimumValue = this.maximumValue;
			this.maximumValue = t;
		}
	}

	@Override
	public void fromString(String s, String context) {
		this.values.put(context, new Float(s));

		if (this.displayWidget != null) {
			this.displayWidget.update();
		}
	}

	@Override
	public Float get(String context) {
		if (this.values.get(context) != null) {
			return this.values.get(context);
		} else {
			return this.values.get("") != null ? this.values.get("") : this.defaultValue;
		}
	}

	@Override
	public void set(Float v, String context) {
		if (this.stepValue > 0.0F) {
			this.values.put(context, (float) Math.round(v / this.stepValue) * this.stepValue);
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
