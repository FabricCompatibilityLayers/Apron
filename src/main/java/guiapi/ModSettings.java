package guiapi;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import de.matthiasmann.twl.Widget;

import net.minecraft.client.Minecraft;

import io.github.betterthanupdates.apron.api.ApronApi;

public class ModSettings {
	public static ArrayList<ModSettings> all = new ArrayList<>();
	public static HashMap<String, String> contextDatadirs = new HashMap<>();
	public static String currentContext = "";
	public static final boolean debug = false;
	private static Minecraft minecraftInstance;
	public String backendname;
	public ArrayList<Setting<?>> Settings;
	public boolean settingsLoaded = false;

	public static void dbgout(String s) {
	}

	public static File getAppDir(String app) {
		return Minecraft.getWorkingDirectory(app);
	}

	public static Minecraft getMcinst() {
		if (minecraftInstance == null) {
			minecraftInstance = (Minecraft) ApronApi.getInstance().getGame();
		}

		return minecraftInstance;
	}

	public static void loadAll(String context) {
		for (ModSettings modSettings : all) {
			modSettings.load(context);
		}
	}

	public static void presetMcint(Minecraft m) {
		minecraftInstance = m;
	}

	public static void setContext(String name, String location) {
		if (name != null) {
			contextDatadirs.put(name, location);
			currentContext = name;

			if (!name.equals("")) {
				loadAll(currentContext);
			}
		} else {
			currentContext = "";
		}
	}

	public ModSettings(String modbackendname) {
		this.backendname = modbackendname;
		this.Settings = new ArrayList<>();
		all.add(this);
	}

	public SettingBoolean addSetting(ModSettingScreen screen, String nicename, String backendname, boolean value) {
		SettingBoolean s = new SettingBoolean(backendname, value);
		WidgetBoolean w = new WidgetBoolean(s, nicename);
		screen.append(w);
		this.append(s);
		return s;
	}

	public SettingBoolean addSetting(ModSettingScreen screen, String nicename, String backendname, boolean value, String truestring, String falsestring) {
		SettingBoolean s = new SettingBoolean(backendname, value);
		WidgetBoolean w = new WidgetBoolean(s, nicename, truestring, falsestring);
		screen.append(w);
		this.append(s);
		return s;
	}

	public SettingFloat addSetting(ModSettingScreen screen, String nicename, String backendname, float value) {
		SettingFloat s = new SettingFloat(backendname, value);
		WidgetFloat w = new WidgetFloat(s, nicename);
		screen.append(w);
		this.append(s);
		return s;
	}

	public SettingFloat addSetting(ModSettingScreen screen, String nicename, String backendname, float value, float min, float step, float max) {
		SettingFloat s = new SettingFloat(backendname, value, min, step, max);
		WidgetFloat w = new WidgetFloat(s, nicename);
		screen.append(w);
		this.append(s);
		return s;
	}

	public SettingKey addSetting(ModSettingScreen screen, String nicename, String backendname, int value) {
		SettingKey s = new SettingKey(backendname, value);
		WidgetKeybinding w = new WidgetKeybinding(s, nicename);
		screen.append(w);
		this.append(s);
		return s;
	}

	public SettingInt addSetting(ModSettingScreen screen, String nicename, String backendname, int value, int min, int max) {
		SettingInt s = new SettingInt(backendname, value, min, 1, max);
		WidgetInt w = new WidgetInt(s, nicename);
		screen.append(w);
		this.append(s);
		return s;
	}

	public SettingInt addSetting(ModSettingScreen screen, String nicename, String backendname, int value, int min, int step, int max) {
		SettingInt s = new SettingInt(backendname, value, min, step, max);
		WidgetInt w = new WidgetInt(s, nicename);
		screen.append(w);
		this.append(s);
		return s;
	}

	public SettingMulti addSetting(ModSettingScreen screen, String nicename, String backendname, int value, String... labels) {
		SettingMulti s = new SettingMulti(backendname, value, labels);
		WidgetMulti w = new WidgetMulti(s, nicename);
		screen.append(w);
		this.append(s);
		return s;
	}

	public SettingText addSetting(ModSettingScreen screen, String nicename, String backendname, String value) {
		SettingText s = new SettingText(backendname, value);
		WidgetText w = new WidgetText(s, nicename);
		screen.append(w);
		this.append(s);
		return s;
	}

	public SettingBoolean addSetting(Widget w2, String nicename, String backendname, boolean value) {
		SettingBoolean s = new SettingBoolean(backendname, value);
		WidgetBoolean w = new WidgetBoolean(s, nicename);
		w2.add(w);
		this.append(s);
		return s;
	}

	public SettingBoolean addSetting(Widget w2, String nicename, String backendname, boolean value, String truestring, String falsestring) {
		SettingBoolean s = new SettingBoolean(backendname, value);
		WidgetBoolean w = new WidgetBoolean(s, nicename, truestring, falsestring);
		w2.add(w);
		this.append(s);
		return s;
	}

	public SettingFloat addSetting(Widget w2, String nicename, String backendname, float value) {
		SettingFloat s = new SettingFloat(backendname, value);
		WidgetFloat w = new WidgetFloat(s, nicename);
		w2.add(w);
		this.append(s);
		return s;
	}

	public SettingFloat addSetting(Widget w2, String nicename, String backendname, float value, float min, float step, float max) {
		SettingFloat s = new SettingFloat(backendname, value, min, step, max);
		WidgetFloat w = new WidgetFloat(s, nicename);
		w2.add(w);
		this.append(s);
		return s;
	}

	public SettingKey addSetting(Widget w2, String nicename, String backendname, int value) {
		SettingKey s = new SettingKey(backendname, value);
		WidgetKeybinding w = new WidgetKeybinding(s, nicename);
		w2.add(w);
		this.append(s);
		return s;
	}

	public SettingInt addSetting(Widget w2, String nicename, String backendname, int value, int min, int max) {
		SettingInt s = new SettingInt(backendname, value, min, 1, max);
		WidgetInt w = new WidgetInt(s, nicename);
		w2.add(w);
		this.append(s);
		return s;
	}

	public SettingInt addSetting(Widget w2, String nicename, String backendname, int value, int min, int step, int max) {
		SettingInt s = new SettingInt(backendname, value, min, step, max);
		WidgetInt w = new WidgetInt(s, nicename);
		w2.add(w);
		this.append(s);
		return s;
	}

	public SettingMulti addSetting(Widget w2, String nicename, String backendname, int value, String... labels) {
		SettingMulti s = new SettingMulti(backendname, value, labels);
		WidgetMulti w = new WidgetMulti(s, nicename);
		w2.add(w);
		this.append(s);
		return s;
	}

	public SettingText addSetting(Widget w2, String nicename, String backendname, String value) {
		SettingText s = new SettingText(backendname, value);
		WidgetText w = new WidgetText(s, nicename);
		w2.add(w);
		this.append(s);
		return s;
	}

	public void append(Setting<?> s) {
		this.Settings.add(s);
		s.parent = this;
	}

	public void copyContextAll(String src, String dest) {
		for (Setting<?> setting : this.Settings) {
			setting.copyContext(src, dest);
		}
	}

	public ArrayList<SettingBoolean> getAllBooleanSettings() {
		return this.getAllBooleanSettings(currentContext);
	}

	public ArrayList<SettingBoolean> getAllBooleanSettings(String context) {
		ArrayList<SettingBoolean> settings = new ArrayList<>();

		for (Setting<?> setting : this.Settings) {
			if (SettingBoolean.class.isAssignableFrom(setting.getClass())) {
				settings.add((SettingBoolean) setting);
			}
		}

		return settings;
	}

	public ArrayList<SettingFloat> getAllFloatSettings() {
		return this.getAllFloatSettings(currentContext);
	}

	public ArrayList<SettingFloat> getAllFloatSettings(String context) {
		ArrayList<SettingFloat> settings = new ArrayList<>();

		for (Setting<?> setting : this.Settings) {
			if (SettingFloat.class.isAssignableFrom(setting.getClass())) {
				settings.add((SettingFloat) setting);
			}
		}

		return settings;
	}

	public ArrayList<SettingInt> getAllIntSettings() {
		return this.getAllIntSettings(currentContext);
	}

	public ArrayList<SettingInt> getAllIntSettings(String context) {
		ArrayList<SettingInt> settings = new ArrayList<>();

		for (Setting<?> setting : this.Settings) {
			if (SettingInt.class.isAssignableFrom(setting.getClass())) {
				settings.add((SettingInt) setting);
			}
		}

		return settings;
	}

	public ArrayList<SettingKey> getAllKeySettings() {
		return this.getAllKeySettings(currentContext);
	}

	public ArrayList<SettingKey> getAllKeySettings(String context) {
		ArrayList<SettingKey> settings = new ArrayList<>();

		for (Setting<?> setting : this.Settings) {
			if (SettingKey.class.isAssignableFrom(setting.getClass())) {
				settings.add((SettingKey) setting);
			}
		}

		return settings;
	}

	public ArrayList<SettingMulti> getAllMultiSettings() {
		return this.getAllMultiSettings(currentContext);
	}

	public ArrayList<SettingMulti> getAllMultiSettings(String context) {
		ArrayList<SettingMulti> settings = new ArrayList<>();

		for (Setting<?> setting : this.Settings) {
			if (SettingMulti.class.isAssignableFrom(setting.getClass())) {
				settings.add((SettingMulti) setting);
			}
		}

		return settings;
	}

	public ArrayList<SettingText> getAllTextSettings() {
		return this.getAllTextSettings(currentContext);
	}

	public ArrayList<SettingText> getAllTextSettings(String context) {
		ArrayList<SettingText> settings = new ArrayList<>();

		for (Setting<?> setting : this.Settings) {
			if (SettingText.class.isAssignableFrom(setting.getClass())) {
				settings.add((SettingText) setting);
			}
		}

		return settings;
	}

	public Boolean getBooleanSetting(String backendName) {
		return this.getBooleanSetting(backendName, currentContext);
	}

	public Boolean getBooleanSetting(String backendName, String context) {
		for (Setting<?> setting : this.Settings) {
			if (SettingBoolean.class.isAssignableFrom(setting.getClass()) && setting.backendName.equals(backendName)) {
				return ((SettingBoolean) setting).get(context);
			}
		}

		throw new InvalidParameterException("SettingBoolean '" + backendName + "' not found.");
	}

	public Float getFloatSetting(String backendName) {
		return this.getFloatSetting(backendName, currentContext);
	}

	public Float getFloatSetting(String backendName, String context) {
		for (Setting<?> setting : this.Settings) {
			if (SettingFloat.class.isAssignableFrom(setting.getClass()) && setting.backendName.equals(backendName)) {
				return ((SettingFloat) setting).get(context);
			}
		}

		throw new InvalidParameterException("SettingFloat '" + backendName + "' not found.");
	}

	public Integer getIntSetting(String backendName) {
		return this.getIntSetting(backendName, currentContext);
	}

	public Integer getIntSetting(String backendName, String context) {
		for (Setting<?> setting : this.Settings) {
			if (SettingInt.class.isAssignableFrom(setting.getClass()) && setting.backendName.equals(backendName)) {
				return ((SettingInt) setting).get(context);
			}
		}

		throw new InvalidParameterException("SettingInt '" + backendName + "' not found.");
	}

	public Integer getKeySetting(String backendName) {
		return this.getKeySetting(backendName, currentContext);
	}

	public Integer getKeySetting(String backendName, String context) {
		for (Setting<?> setting : this.Settings) {
			if (SettingKey.class.isAssignableFrom(setting.getClass()) && setting.backendName.equals(backendName)) {
				return ((SettingKey) setting).get(context);
			}
		}

		throw new InvalidParameterException("SettingKey '" + backendName + "' not found.");
	}

	public Integer getMultiSetting(String backendName) {
		return this.getMultiSetting(backendName, currentContext);
	}

	public Integer getMultiSetting(String backendName, String context) {
		for (Setting<?> setting : this.Settings) {
			if (SettingMulti.class.isAssignableFrom(setting.getClass()) && setting.backendName.equals(backendName)) {
				return ((SettingMulti) setting).get(context);
			}
		}

		throw new InvalidParameterException("SettingMulti '" + backendName + "' not found.");
	}

	public String getMultiSettingLabel(String backendName) {
		return this.getMultiSettingLabel(backendName, currentContext);
	}

	public String getMultiSettingLabel(String backendName, String context) {
		for (Setting<?> setting : this.Settings) {
			if (SettingMulti.class.isAssignableFrom(setting.getClass()) && setting.backendName.equals(backendName)) {
				return ((SettingMulti) setting).getLabel(context);
			}
		}

		throw new InvalidParameterException("SettingMulti '" + backendName + "' not found.");
	}

	public String getTextSetting(String backendName) {
		return this.getTextSetting(backendName, currentContext);
	}

	public String getTextSetting(String backendName, String context) {
		for (Setting<?> setting : this.Settings) {
			if (SettingText.class.isAssignableFrom(setting.getClass()) && setting.backendName.equals(backendName)) {
				return ((SettingText) setting).get(context);
			}
		}

		throw new InvalidParameterException("SettingText '" + backendName + "' not found.");
	}

	public void load() {
		this.load("");
		this.settingsLoaded = true;
	}

	public void load(String context) {
		try {
			if (contextDatadirs.get(context) != null) {
				File path = getAppDir("minecraft/" + contextDatadirs.get(context) + "/" + this.backendname + "/");

				if (path.exists()) {
					File file = new File(path, "guiconfig.properties");

					if (file.exists()) {
						Properties p = new Properties();
						p.load(Files.newInputStream(file.toPath()));

						for (Setting<?> setting : this.Settings) {
							if (setting != null) {
								dbgout("setting load");

								if (p.containsKey(setting.backendName)) {
									dbgout("setting " + p.get(setting.backendName));
									setting.fromString((String) p.get(setting.backendName), context);
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void remove(Setting<?> s) {
		this.Settings.remove(s);
		s.parent = null;
	}

	public void resetAll() {
		this.resetAll(currentContext);
	}

	public void resetAll(String context) {
		for (Setting<?> setting : this.Settings) {
			setting.reset(context);
		}
	}

	public void save(String context) {
		if (this.settingsLoaded) {
			try {
				File path = getAppDir("minecraft/" + contextDatadirs.get(context) + "/" + this.backendname + "/");
				dbgout("saving context " + context + " (" + path.getAbsolutePath() + " [" + contextDatadirs.get(context) + "])");

				if (!path.exists()) {
					path.mkdirs();
				}

				File file = new File(path, "guiconfig.properties");
				Properties p = new Properties();

				for (Setting<?> z : this.Settings) {
					p.put(z.backendName, z.toString(context));
				}

				FileOutputStream out = new FileOutputStream(file);
				p.store(out, "");
			} catch (Exception var7) {
				var7.printStackTrace();
			}
		}
	}

	public int size() {
		return this.Settings.size();
	}

	static {
		contextDatadirs.put("", "mods");
	}
}
