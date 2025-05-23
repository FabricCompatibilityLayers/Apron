package modoptionsapi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import net.minecraft.client.Minecraft;

public class ModOptions {
	private LinkedHashMap<String, ModOption<?>> options = new LinkedHashMap<>();
	private LinkedHashMap<String, ModOptions> subOptions = new LinkedHashMap<>();
	private ModOptionsGuiController gui = new ModOptionsGuiController(this);
	private String name;
	private ModOptions parent = null;
	private boolean multiplayer = false;
	private boolean singleplayer = true;

	public ModOptions(String name) {
		this.name = name;
	}

	public ModOptions(String name, ModOptions p) {
		this.name = name;
		this.parent = p;
	}

	public void addOption(ModOption<?> option) {
		this.options.put(option.getName(), option);
	}

	private ModOption<?> addNewOption(ModOption<?> option) {
		this.options.put(option.getName(), option);
		return option;
	}

	public ModOption<?> addTextOption(String name) {
		ModTextOption option = new ModTextOption(name);
		return this.addNewOption(option);
	}

	public ModOption<?> addTextOption(String name, String value) {
		ModTextOption option = new ModTextOption(name);
		option.setGlobalValue(value);
		return this.addNewOption(option);
	}

	public ModOption<?> addTextOption(String name, int maxlen) {
		ModTextOption option = new ModTextOption(name, maxlen);
		return this.addNewOption(option);
	}

	public ModOption<?> addTextOption(String name, String value, int maxlen) {
		ModTextOption option = new ModTextOption(name, maxlen);
		option.setGlobalValue(value);
		return this.addNewOption(option);
	}

	public ModOption<?> addTextOption(String name, String value, Integer maxlen) {
		return this.addTextOption(name, value, maxlen.intValue());
	}

	public ModOption<?> addKeyBinding(String name) {
		ModKeyOption option = new ModKeyOption(name);
		return this.addNewOption(option);
	}

	public ModOption<?> addKeyOption(String name) {
		ModKeyOption option = new ModKeyOption(name);
		return this.addNewOption(option);
	}

	public void addMultiOption(String name, String[] values) {
		ModMultiOption option = new ModMultiOption(name, values);
		this.addOption(option);
	}

	public void addMappedMultiOption(String name, Integer[] keys, String[] values) throws IndexOutOfBoundsException {
		if (keys.length != values.length) {
			throw new IndexOutOfBoundsException("Arrays are not same length");
		} else {
			ModMappedMultiOption option = new ModMappedMultiOption(name);

			for (int x = 0; x < keys.length; ++x) {
				option.addValue(keys[x], values[x]);
			}

			this.addOption(option);
		}
	}

	public void addMappedMultiOption(String name, int[] keys, String[] values) throws IndexOutOfBoundsException {
		if (keys.length != values.length) {
			throw new IndexOutOfBoundsException("Arrays are not same length");
		} else {
			ModMappedMultiOption option = new ModMappedMultiOption(name);

			for (int x = 0; x < keys.length; ++x) {
				option.addValue(Integer.valueOf(keys[x]), values[x]);
			}

			this.addOption(option);
		}
	}

	public void addToggle(String name) {
		ModBooleanOption option = new ModBooleanOption(name);
		this.addOption(option);
	}

	public void addSlider(String name) {
		ModSliderOption option = new ModSliderOption(name);
		this.addOption(option);
	}

	public void addSlider(String name, int low, int high) {
		ModSliderOption option = new ModSliderOption(name, low, high);
		this.addOption(option);
	}

	public void addSubOptions(ModOptions m) {
		m.setParent(this);
		this.subOptions.put(m.getName(), m);
	}

	public boolean containsSubOptions(String name) {
		return this.subOptions.containsKey(name);
	}

	public ModOptions[] getSubOptions() {
		Set<Map.Entry<String, ModOptions>> s = this.subOptions.entrySet();
		ModOptions[] m = new ModOptions[s.size()];
		int i = 0;

		for (Map.Entry<String, ModOptions> e : s) {
			m[i] = e.getValue();
			++i;
		}

		return m;
	}

	public ModOptions[] getMultiplayerSubOptions() {
		Set<Map.Entry<String, ModOptions>> s = this.subOptions.entrySet();
		LinkedList<ModOptions> m = new LinkedList<>();

		for (Map.Entry<String, ModOptions> e : s) {
			if (e.getValue().isMultiplayerMod()) {
				m.add(e.getValue());
			}
		}

		return m.toArray(new ModOptions[0]);
	}

	public ModOptions[] getSingleplayerSubOptions() {
		Set<Map.Entry<String, ModOptions>> s = this.subOptions.entrySet();
		LinkedList<ModOptions> m = new LinkedList<>();

		for (Map.Entry<String, ModOptions> e : s) {
			if (e.getValue().isSingleplayerMod()) {
				m.add(e.getValue());
			}
		}

		return m.toArray(new ModOptions[0]);
	}

	public ModOptions getSubOption(String name) {
		return this.subOptions.get(name);
	}

	public void globalReset(boolean global) {
		if (ModOptionsAPI.isMultiplayerWorld() && this.multiplayer || !ModOptionsAPI.isMultiplayerWorld() && this.singleplayer) {
			ModOption<?>[] options = this.getOptions();

			for (ModOption<?> option : options) {
				option.setGlobal(global);
			}

			ModOptions[] subMenus;

			if (!ModOptionsAPI.isMultiplayerWorld() && this.singleplayer) {
				subMenus = this.getSingleplayerSubOptions();

				for (ModOptions sub : subMenus) {
					sub.globalReset(global);
				}
			}

			if (ModOptionsAPI.isMultiplayerWorld() && this.multiplayer) {
				subMenus = this.getMultiplayerSubOptions();

				for (ModOptions sub : subMenus) {
					sub.globalReset(global);
				}
			}
		}
	}

	public ModOption<?> getOption(String name) {
		return this.options.get(name);
	}

	public String getOptionValue(String name) {
		ModOption<?> option = this.options.get(name);
		return option instanceof ModSliderOption
				? Integer.toString(((ModSliderOption) option).getIntValue(((ModSliderOption) option).getValue()))
				: option.getValue().toString();
	}

	public String getTextValue(String name) throws NoSuchOptionException {
		ModOption<?> option = this.options.get(name);

		if (option == null) {
			throw new NoSuchOptionException("No option named " + name);
		} else if (!(option instanceof ModTextOption)) {
			throw new IncompatibleOptionTypeException("Option " + name + " is not a text option");
		} else {
			return ((ModTextOption) option).getValue();
		}
	}

	public boolean getToggleValue(String name) throws NoSuchOptionException {
		ModOption<?> option = this.options.get(name);

		if (option == null) {
			throw new NoSuchOptionException("No option named " + name);
		} else if (!(option instanceof ModBooleanOption)) {
			throw new IncompatibleOptionTypeException("Option " + name + " is not a toggle option");
		} else {
			return ((ModBooleanOption) option).getValue();
		}
	}

	public float getSliderValue(String name) throws NoSuchOptionException {
		ModOption<?> option = this.options.get(name);

		if (option == null) {
			throw new NoSuchOptionException("No option named " + name);
		} else if (!(option instanceof ModSliderOption)) {
			throw new IncompatibleOptionTypeException("Option " + name + " is not a slider option");
		} else {
			return ((ModSliderOption) option).getValue();
		}
	}

	public int getMappedValue(String name) throws NoSuchOptionException {
		ModOption<?> option = this.options.get(name);

		if (option == null) {
			throw new NoSuchOptionException("No option named " + name);
		} else if (!(option instanceof ModMappedMultiOption)) {
			throw new IncompatibleOptionTypeException("Option " + name + " is not a mapped multi option");
		} else {
			return ((ModMappedMultiOption) option).getValue();
		}
	}

	public void setOptionValue(String name, boolean value) {
		ModOption<?> m = this.getOption(name);

		if (m == null) {
			throw new NoSuchOptionException();
		} else if (m instanceof ModBooleanOption) {
			ModBooleanOption bo = (ModBooleanOption) m;
			bo.setGlobalValue(value);
		} else {
			throw new IncompatibleOptionTypeException();
		}
	}

	public void setOptionValue(String name, Integer value) {
		ModOption<?> m = this.getOption(name);

		if (m == null) {
			throw new NoSuchOptionException();
		} else {
			if (m instanceof ModSliderOption) {
				((ModSliderOption) m).setGlobalValue(value);
			} else if (m instanceof ModMappedMultiOption) {
				((ModMappedMultiOption) m).setGlobalValue(value);
			} else {
				if (!(m instanceof ModKeyOption)) {
					throw new IncompatibleOptionTypeException();
				}

				((ModKeyOption) m).setGlobalValue(value);
			}
		}
	}

	public void setOptionValue(String name, int value) {
		this.setOptionValue(name, Integer.valueOf(value));
	}

	public void setOptionValue(String name, String value) {
		ModOption<?> m = this.getOption(name);

		if (m == null) {
			throw new NoSuchOptionException();
		} else {
			if (m instanceof ModMultiOption) {
				ModMultiOption mo = (ModMultiOption) m;
				mo.setGlobalValue(value);
			} else {
				if (!(m instanceof ModTextOption)) {
					throw new IncompatibleOptionTypeException();
				}

				((ModTextOption) m).setGlobalValue(value);
			}
		}
	}

	public ModOption<?>[] getOptions() {
		Set<Map.Entry<String, ModOption<?>>> s = this.options.entrySet();
		ModOption<?>[] m = new ModOption[s.size()];
		int i = m.length - 1;

		for (Map.Entry<String, ModOption<?>> e : s) {
			m[i] = e.getValue();
			--i;
		}

		return m;
	}

	public void setWideOption(String name) {
		this.gui.setWide(name);
	}

	public void setWideOption(ModOption<?> option) {
		this.setWideOption(option.getName());
	}

	public void setOptionStringFormat(String name, MODisplayString formatter) {
		this.gui.setFormatter(this.getOption(name), formatter);
	}

	public void addOptionFormatter(String name, MODisplayString formatter) {
		this.gui.addFormatter(this.getOption(name), formatter);
	}

	public String getName() {
		return this.name;
	}

	public ModOptions getParent() {
		return this.parent;
	}

	public void setParent(ModOptions o) {
		this.parent = o;
	}

	public ModOptionsGuiController getGuiController() {
		return this.gui;
	}

	public void setMultiplayerMode(boolean multi) {
		this.multiplayer = multi;
	}

	public void setSingleplayerMode(boolean single) {
		this.singleplayer = single;
	}

	public boolean isSingleplayerMod() {
		return this.singleplayer;
	}

	public boolean isMultiplayerMod() {
		return this.multiplayer;
	}

	public void loadValues() {
		this.loadValues("", false);
	}

	public void loadValues(String worldName, boolean multi) {
		for (ModOptions child : this.getSubOptions()) {
			child.loadValues(worldName, multi);
		}

		File file = this.getFile(worldName, multi);

		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			HashMap<String, String> map = new HashMap<>();

			String line;

			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(":", 2);
				String name = parts[0];
				String value = parts[1].replace(":", "");
				map.put(name, value);
			}

			for (ModOption o : this.getOptions()) {
				try {
					if (map.containsKey(o.getName())) {
						String val = map.get(o.getName());
						boolean global = worldName.length() == 0;

						if (o instanceof ModSliderOption) {
							ModSliderOption s = (ModSliderOption) o;
							s.setValue(Float.parseFloat(val), global);
						} else if (o instanceof ModMultiOption) {
							ModMultiOption t = (ModMultiOption) o;
							t.setValue(val, global);
						} else if (o instanceof ModBooleanOption) {
							ModBooleanOption b = (ModBooleanOption) o;
							b.setValue(Boolean.valueOf(val), global);
						} else if (o instanceof ModMappedMultiOption) {
							ModMappedMultiOption t = (ModMappedMultiOption) o;
							t.setValue(Integer.parseInt(val), global);
						} else if (o instanceof ModKeyOption) {
							ModKeyOption k = (ModKeyOption) o;
							Integer key = Integer.parseInt(val);

							if (!key.equals(ModKeyOption.defaultVal)) {
								try {
									k.setValue(key, global);
								} catch (KeyAlreadyBoundException var16) {
									System.out.println("(MOAPI) Key + " + val.charAt(0) + " already bound, please rebind in options");
								}
							}
						} else {
							o.setValue(val, global);
						}

						o.setGlobal(global);
					}
				} catch (NumberFormatException var17) {
					System.err.println("(ModOptionsAPI): Could not load option value");
				}
			}
		} catch (FileNotFoundException ignored) {
		} catch (IOException var19) {
			System.out.println("(ModOptionsAPI): IOException occured: " + var19.getMessage());
		}
	}

	public void save(String name, boolean multiplayer) {
		boolean global = name.length() == 0;
		File file = this.getFile(name, multiplayer);
		file.delete();

		try {
			PrintWriter printwriter = new PrintWriter(new FileWriter(file));

			for (ModOption<?> o : this.getOptions()) {
				Object obj = o.getValue(global);

				if (obj != null && (global || !o.useGlobalValue())) {
					printwriter.println(o.getName().replace(":", "") + ":" + obj);
				}
			}

			printwriter.close();
		} catch (IOException var11) {
			System.err.println("(ModOptionsAPI): Could not save options to " + name);
		}
	}

	public void save() {
		this.save("", false);
	}

	private String getDir() {
		StringBuilder subDir = new StringBuilder(this.name);

		for (ModOptions p = this.parent; p != null; p = p.getParent()) {
			subDir.insert(0, p.getName() + "/");
		}

		return Minecraft.getRunDirectory() + "/ModOptions/" + subDir;
	}

	private File getFile(String name, boolean multiplayer) {
		String prefix = "";

		if (name.length() > 0) {
			if (multiplayer) {
				prefix = name + ".server.";
			} else {
				prefix = name + ".world.";
			}
		}

		String subDir = this.getDir();
		File dir = new File(subDir);

		if (!dir.exists()) {
			dir.mkdirs();
		}

		return new File(subDir + "/" + prefix + "settings.ini");
	}
}
