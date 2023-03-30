package modoptionsapi;

import java.util.Hashtable;
import java.util.Objects;

import org.lwjgl.input.Keyboard;

public class ModKeyOption extends ModOption<Integer> {
	private static Hashtable<Integer, ModOption<?>> bindings = new Hashtable<>();
	public static final Integer defaultVal = 0;

	public ModKeyOption(String name) {
		this.name = name;
		this.setValue(defaultVal, true);
		this.setValue(defaultVal, false);
	}

	public void setValue(int value) {
		this.setValue(Integer.valueOf(value), this.global);
	}

	public void setValue(Integer value) {
		this.setValue(value, this.global);
	}

	public void setValue(int value, boolean scope) {
		this.setValue(Integer.valueOf(value), scope);
	}

	public void setValue(Integer value, boolean scope) {
		Integer curVal = this.getValue(scope);

		if (Objects.equals(value, defaultVal)) {
			bindings.remove(value);
			super.setValue(value, this.global);
		} else {
			if ((!Objects.equals(this.getLocalValue(), value) || this.global) && (!Objects.equals(this.getGlobalValue(), value) || !this.global) && isKeyBound(value)) {
				throw new KeyAlreadyBoundException(value);
			}

			if (curVal != null) {
				bindings.remove(curVal);
			}

			super.setValue(value, scope);
			bindings.put(value, this);
		}
	}

	public static boolean isKeyBound(Integer c) {
		return !c.equals(defaultVal) && bindings.containsKey(c);
	}

	public static String getKeyName(Integer key) {
		String val = Keyboard.getKeyName(key);
		return val == null ? "INVALID" : val;
	}
}
