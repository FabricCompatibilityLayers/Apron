package guiapi.setting;

import guiapi.ModSettings;
import org.lwjgl.input.Keyboard;

public class KeySetting extends Setting<Integer> {
	public KeySetting(String title, int key) {
		this.defaultValue = key;
		this.values.put("", key);
		this.backendName = title;
	}

	public KeySetting(String title, String key) {
		this(title, Keyboard.getKeyIndex(key));
	}

	@Override
	public void fromString(String s, String context) {
		if (s.equals("UNBOUND")) {
			this.values.put(context, 0);
		} else {
			this.values.put(context, Keyboard.getKeyIndex(s));
		}

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

	public boolean isKeyDown() {
		return this.isKeyDown(ModSettings.currentContext);
	}

	public boolean isKeyDown(String context) {
		return this.get(context) != -1 && Keyboard.isKeyDown(this.get(context));
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
		this.set(Keyboard.getKeyIndex(v), context);
	}

	@Override
	public String toString(String context) {
		return Keyboard.getKeyName(this.get(context));
	}
}
