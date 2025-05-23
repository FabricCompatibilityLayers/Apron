package modoptionsapi;

import java.util.Hashtable;
import java.util.LinkedList;

public class ModOptionsGuiController {
	private ModOptions options;
	private LinkedList<String> wide = new LinkedList<>();
	private Hashtable<ModOption<?>, LinkedList<MODisplayString>> formatters = new Hashtable<>();

	public ModOptionsGuiController(ModOptions o) {
		this.options = o;
	}

	public void setWide(String name) {
		this.wide.add(name);
	}

	public void setWide(ModOption<?> option) {
		this.setWide(option.getName());
	}

	public boolean isWide(ModOption<?> o) {
		return o instanceof ModTextOption ? true : this.wide.contains(o.getName());
	}

	public void setFormatter(ModOption<?> option, MODisplayString formatter) {
		LinkedList<MODisplayString> newList = new LinkedList<>();
		newList.add(formatter);
		this.formatters.put(option, newList);
	}

	public void addFormatter(ModOption<?> option, MODisplayString formatter) {
		LinkedList<MODisplayString> list;

		if (this.formatters.containsKey(option)) {
			list = this.formatters.get(option);
		} else {
			list = new LinkedList<>();
			this.formatters.put(option, list);
		}

		list.add(formatter);
	}

	public String getDisplayString(ModOption<?> o) {
		return this.getDisplayString(o, false);
	}

	public String getDisplayString(ModOption<?> o, boolean localMode) {
		String value;

		if (localMode && o.useGlobalValue()) {
			value = "GLOBAL";
		} else if (o instanceof ModSliderOption) {
			ModSliderOption o2 = (ModSliderOption) o;
			value = Float.toString(o2.getValue(!localMode));
		} else if (o instanceof ModMappedMultiOption) {
			ModMappedMultiOption o2 = (ModMappedMultiOption) o;
			value = o2.getStringValue(o2.getValue(!localMode));
		} else if (o instanceof ModMultiOption) {
			value = ((ModMultiOption) o).getValue(!localMode);
		} else if (o instanceof ModBooleanOption) {
			ModBooleanOption o2 = (ModBooleanOption) o;
			value = o2.getStringValue(o2.getValue(!localMode));
		} else if (o instanceof ModTextOption) {
			value = ((ModTextOption) o).getValue(!localMode);
		} else if (o instanceof ModKeyOption) {
			value = ModKeyOption.getKeyName(((ModKeyOption) o).getValue(!localMode));
		} else {
			value = o.getValue(!localMode).toString();
		}

		if (this.formatters.containsKey(o) && (!localMode || !o.useGlobalValue())) {
			for (MODisplayString s : this.formatters.get(o)) {
				value = s.manipulate(o.getName(), value);
			}

			return value;
		} else if (o instanceof ModKeyOption) {
			MODisplayString s = MOFormatters.noFormat;
			return s.manipulate(o.getName(), value);
		} else {
			MODisplayString s = MOFormatters.defaultFormat;
			return s.manipulate(o.getName(), value);
		}
	}
}
