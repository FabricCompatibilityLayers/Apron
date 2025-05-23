package modoptionsapi;

public class ModSliderOption extends ModOption<Float> {
	private int low = 0;
	private int high = 100;

	public ModSliderOption(String name) {
		this.name = name;
		this.value = 1.0F;
		this.localValue = 1.0F;
	}

	public ModSliderOption(String name, int low, int high) {
		this(name);
		this.low = low;
		this.high = high;
	}

	public int getHighVal() {
		return this.high;
	}

	public int getLowVal() {
		return this.low;
	}

	public void setValue(Float value) {
		super.setValue(this.getBoundedValue(value, this.low, this.high));
	}

	public void setValue(int value) {
		this.setValue((float) value);
	}

	public void setLocalValue(Float value) {
		super.setLocalValue(this.getBoundedValue(value, this.low, this.high));
	}

	public void setLocalValue(int value) {
		this.setLocalValue((float) value);
	}

	public void setGlobalValue(Float value) {
		super.setGlobalValue(this.getBoundedValue(value, this.low, this.high));
	}

	public void setGlobalValue(int value) {
		this.setGlobalValue((float) value);
	}

	public float transformValue(float value, int lower, int upper) {
		value = this.getBoundedValue(value, this.low, this.high);
		float base = (value - (float) this.low) / (float) (this.high - this.low);
		return base * (float) (upper - lower) + (float) lower;
	}

	public float untransformValue(float value, int lower, int upper) {
		value = this.getBoundedValue(value, lower, upper);
		float base = (value - (float) lower) / (float) (upper - lower);
		return value * (float) (this.high - this.low) + (float) this.low;
	}

	private float getBoundedValue(float value, int lower, int upper) {
		if (value < (float) lower) {
			return (float) lower;
		} else {
			return Math.min(value, (float) upper);
		}
	}

	@Deprecated
	public int getIntValue(float value) {
		return (int) value;
	}

	@Deprecated
	public void setIntValue(int val) {
		this.setValue(val);
	}

	@Deprecated
	public float getFloatValue() {
		return this.getValue();
	}

	@Deprecated
	public float getFloatValue(int value) {
		return (float) value;
	}

	@Deprecated
	public int getIntValue() {
		return this.getValue().intValue();
	}
}
