package guiapi.setting;

import java.util.HashMap;

import de.matthiasmann.twl.Widget;
import guiapi.ModSettings;
import guiapi.widget.SettingWidget;

public abstract class Setting<T> extends Widget {
	public String backendName;
	public T defaultValue;
	public SettingWidget displayWidget = null;
	public ModSettings parent = null;
	public HashMap<String, T> values = new HashMap<>();

	public Setting() {
	}

	public void copyContext(String srccontext, String destcontext) {
		this.values.put(destcontext, this.values.get(srccontext));
	}

	public abstract void fromString(String string, String string2);

	public T get() {
		return this.get(ModSettings.currentContext);
	}

	public abstract T get(String string);

	public void reset() {
		this.reset(ModSettings.currentContext);
	}

	public void reset(String context) {
		this.set(this.defaultValue, context);
	}

	public void set(T v) {
		this.set(v, ModSettings.currentContext);
	}

	public abstract void set(T object, String string);

	public abstract String toString(String string);
}
