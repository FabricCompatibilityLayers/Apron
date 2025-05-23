package modoptionsapi;

public abstract class ModOption<E> {
	protected String name;
	protected E value;
	protected E localValue;
	protected boolean global = true;
	protected MOCallback callback = null;

	public ModOption() {
	}

	public String getName() {
		return this.name;
	}

	public void setValue(E value) {
		if (this.global) {
			this.value = value;
		} else {
			this.localValue = value;
		}
	}

	public void setValue(E value, boolean scope) {
		if (scope) {
			this.value = value;
		} else {
			this.localValue = value;
		}
	}

	public void setLocalValue(E value) {
		this.setValue(value, false);
	}

	public void setGlobalValue(E value) {
		this.setValue(value, true);
	}

	public E getValue() {
		return this.global ? this.value : this.localValue;
	}

	public E getValue(boolean ignoredScope) {
		return this.global ? this.getGlobalValue() : this.getLocalValue();
	}

	public E getGlobalValue() {
		return this.value;
	}

	public E getLocalValue() {
		return this.localValue;
	}

	public boolean useGlobalValue() {
		return this.global;
	}

	public void setGlobal(boolean global) {
		this.global = global;
	}

	public MOCallback getCallback() {
		return this.callback;
	}

	public boolean hasCallback() {
		return this.callback != null;
	}

	public void setCallback(MOCallback callback) {
		this.callback = callback;
	}
}
