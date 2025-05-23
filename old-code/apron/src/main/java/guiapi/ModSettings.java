package guiapi;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import de.matthiasmann.twl.Widget;
import guiapi.setting.BooleanSetting;
import guiapi.setting.FloatSetting;
import guiapi.setting.IntSetting;
import guiapi.setting.KeySetting;
import guiapi.setting.MultiSetting;
import guiapi.setting.Setting;
import guiapi.setting.TextSetting;
import guiapi.widget.BooleanWidget;
import guiapi.widget.FloatWidget;
import guiapi.widget.IntWidget;
import guiapi.widget.KeybindingWidget;
import guiapi.widget.MultiWidget;
import guiapi.widget.TextWidget;

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
		return Minecraft.getApplicationDirectory(app);
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

	public BooleanSetting addSetting(ModSettingScreen screen, String nicename, String backendname, boolean value) {
		BooleanSetting s = new BooleanSetting(backendname, value);
		BooleanWidget w = new BooleanWidget(s, nicename);
		screen.append(w);
		this.append(s);
		return s;
	}

	public BooleanSetting addSetting(ModSettingScreen screen, String nicename, String backendname, boolean value, String truestring, String falsestring) {
		BooleanSetting s = new BooleanSetting(backendname, value);
		BooleanWidget w = new BooleanWidget(s, nicename, truestring, falsestring);
		screen.append(w);
		this.append(s);
		return s;
	}

	public FloatSetting addSetting(ModSettingScreen screen, String nicename, String backendname, float value) {
		FloatSetting s = new FloatSetting(backendname, value);
		FloatWidget w = new FloatWidget(s, nicename);
		screen.append(w);
		this.append(s);
		return s;
	}

	public FloatSetting addSetting(ModSettingScreen screen, String nicename, String backendname, float value, float min, float step, float max) {
		FloatSetting s = new FloatSetting(backendname, value, min, step, max);
		FloatWidget w = new FloatWidget(s, nicename);
		screen.append(w);
		this.append(s);
		return s;
	}

	public KeySetting addSetting(ModSettingScreen screen, String nicename, String backendname, int value) {
		KeySetting s = new KeySetting(backendname, value);
		KeybindingWidget w = new KeybindingWidget(s, nicename);
		screen.append(w);
		this.append(s);
		return s;
	}

	public IntSetting addSetting(ModSettingScreen screen, String nicename, String backendname, int value, int min, int max) {
		IntSetting s = new IntSetting(backendname, value, min, 1, max);
		IntWidget w = new IntWidget(s, nicename);
		screen.append(w);
		this.append(s);
		return s;
	}

	public IntSetting addSetting(ModSettingScreen screen, String nicename, String backendname, int value, int min, int step, int max) {
		IntSetting s = new IntSetting(backendname, value, min, step, max);
		IntWidget w = new IntWidget(s, nicename);
		screen.append(w);
		this.append(s);
		return s;
	}

	public MultiSetting addSetting(ModSettingScreen screen, String nicename, String backendname, int value, String... labels) {
		MultiSetting s = new MultiSetting(backendname, value, labels);
		MultiWidget w = new MultiWidget(s, nicename);
		screen.append(w);
		this.append(s);
		return s;
	}

	public TextSetting addSetting(ModSettingScreen screen, String nicename, String backendname, String value) {
		TextSetting s = new TextSetting(backendname, value);
		TextWidget w = new TextWidget(s, nicename);
		screen.append(w);
		this.append(s);
		return s;
	}

	public BooleanSetting addSetting(Widget w2, String nicename, String backendname, boolean value) {
		BooleanSetting s = new BooleanSetting(backendname, value);
		BooleanWidget w = new BooleanWidget(s, nicename);
		w2.add(w);
		this.append(s);
		return s;
	}

	public BooleanSetting addSetting(Widget w2, String nicename, String backendname, boolean value, String truestring, String falsestring) {
		BooleanSetting s = new BooleanSetting(backendname, value);
		BooleanWidget w = new BooleanWidget(s, nicename, truestring, falsestring);
		w2.add(w);
		this.append(s);
		return s;
	}

	public FloatSetting addSetting(Widget w2, String nicename, String backendname, float value) {
		FloatSetting s = new FloatSetting(backendname, value);
		FloatWidget w = new FloatWidget(s, nicename);
		w2.add(w);
		this.append(s);
		return s;
	}

	public FloatSetting addSetting(Widget w2, String nicename, String backendname, float value, float min, float step, float max) {
		FloatSetting s = new FloatSetting(backendname, value, min, step, max);
		FloatWidget w = new FloatWidget(s, nicename);
		w2.add(w);
		this.append(s);
		return s;
	}

	public KeySetting addSetting(Widget w2, String nicename, String backendname, int value) {
		KeySetting s = new KeySetting(backendname, value);
		KeybindingWidget w = new KeybindingWidget(s, nicename);
		w2.add(w);
		this.append(s);
		return s;
	}

	public IntSetting addSetting(Widget w2, String nicename, String backendname, int value, int min, int max) {
		IntSetting s = new IntSetting(backendname, value, min, 1, max);
		IntWidget w = new IntWidget(s, nicename);
		w2.add(w);
		this.append(s);
		return s;
	}

	public IntSetting addSetting(Widget w2, String nicename, String backendname, int value, int min, int step, int max) {
		IntSetting s = new IntSetting(backendname, value, min, step, max);
		IntWidget w = new IntWidget(s, nicename);
		w2.add(w);
		this.append(s);
		return s;
	}

	public MultiSetting addSetting(Widget w2, String nicename, String backendname, int value, String... labels) {
		MultiSetting s = new MultiSetting(backendname, value, labels);
		MultiWidget w = new MultiWidget(s, nicename);
		w2.add(w);
		this.append(s);
		return s;
	}

	public TextSetting addSetting(Widget w2, String nicename, String backendname, String value) {
		TextSetting s = new TextSetting(backendname, value);
		TextWidget w = new TextWidget(s, nicename);
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

	public ArrayList<BooleanSetting> getAllBooleanSettings() {
		return this.getAllBooleanSettings(currentContext);
	}

	public ArrayList<BooleanSetting> getAllBooleanSettings(String context) {
		ArrayList<BooleanSetting> settings = new ArrayList<>();

		for (Setting<?> setting : this.Settings) {
			if (BooleanSetting.class.isAssignableFrom(setting.getClass())) {
				settings.add((BooleanSetting) setting);
			}
		}

		return settings;
	}

	public ArrayList<FloatSetting> getAllFloatSettings() {
		return this.getAllFloatSettings(currentContext);
	}

	public ArrayList<FloatSetting> getAllFloatSettings(String context) {
		ArrayList<FloatSetting> settings = new ArrayList<>();

		for (Setting<?> setting : this.Settings) {
			if (FloatSetting.class.isAssignableFrom(setting.getClass())) {
				settings.add((FloatSetting) setting);
			}
		}

		return settings;
	}

	public ArrayList<IntSetting> getAllIntSettings() {
		return this.getAllIntSettings(currentContext);
	}

	public ArrayList<IntSetting> getAllIntSettings(String context) {
		ArrayList<IntSetting> settings = new ArrayList<>();

		for (Setting<?> setting : this.Settings) {
			if (IntSetting.class.isAssignableFrom(setting.getClass())) {
				settings.add((IntSetting) setting);
			}
		}

		return settings;
	}

	public ArrayList<KeySetting> getAllKeySettings() {
		return this.getAllKeySettings(currentContext);
	}

	public ArrayList<KeySetting> getAllKeySettings(String context) {
		ArrayList<KeySetting> settings = new ArrayList<>();

		for (Setting<?> setting : this.Settings) {
			if (KeySetting.class.isAssignableFrom(setting.getClass())) {
				settings.add((KeySetting) setting);
			}
		}

		return settings;
	}

	public ArrayList<MultiSetting> getAllMultiSettings() {
		return this.getAllMultiSettings(currentContext);
	}

	public ArrayList<MultiSetting> getAllMultiSettings(String context) {
		ArrayList<MultiSetting> settings = new ArrayList<>();

		for (Setting<?> setting : this.Settings) {
			if (MultiSetting.class.isAssignableFrom(setting.getClass())) {
				settings.add((MultiSetting) setting);
			}
		}

		return settings;
	}

	public ArrayList<TextSetting> getAllTextSettings() {
		return this.getAllTextSettings(currentContext);
	}

	public ArrayList<TextSetting> getAllTextSettings(String context) {
		ArrayList<TextSetting> settings = new ArrayList<>();

		for (Setting<?> setting : this.Settings) {
			if (TextSetting.class.isAssignableFrom(setting.getClass())) {
				settings.add((TextSetting) setting);
			}
		}

		return settings;
	}

	public Boolean getBooleanSetting(String backendName) {
		return this.getBooleanSetting(backendName, currentContext);
	}

	public Boolean getBooleanSetting(String backendName, String context) {
		for (Setting<?> setting : this.Settings) {
			if (BooleanSetting.class.isAssignableFrom(setting.getClass()) && setting.backendName.equals(backendName)) {
				return ((BooleanSetting) setting).get(context);
			}
		}

		throw new InvalidParameterException("SettingBoolean '" + backendName + "' not found.");
	}

	public Float getFloatSetting(String backendName) {
		return this.getFloatSetting(backendName, currentContext);
	}

	public Float getFloatSetting(String backendName, String context) {
		for (Setting<?> setting : this.Settings) {
			if (FloatSetting.class.isAssignableFrom(setting.getClass()) && setting.backendName.equals(backendName)) {
				return ((FloatSetting) setting).get(context);
			}
		}

		throw new InvalidParameterException("SettingFloat '" + backendName + "' not found.");
	}

	public Integer getIntSetting(String backendName) {
		return this.getIntSetting(backendName, currentContext);
	}

	public Integer getIntSetting(String backendName, String context) {
		for (Setting<?> setting : this.Settings) {
			if (IntSetting.class.isAssignableFrom(setting.getClass()) && setting.backendName.equals(backendName)) {
				return ((IntSetting) setting).get(context);
			}
		}

		throw new InvalidParameterException("SettingInt '" + backendName + "' not found.");
	}

	public Integer getKeySetting(String backendName) {
		return this.getKeySetting(backendName, currentContext);
	}

	public Integer getKeySetting(String backendName, String context) {
		for (Setting<?> setting : this.Settings) {
			if (KeySetting.class.isAssignableFrom(setting.getClass()) && setting.backendName.equals(backendName)) {
				return ((KeySetting) setting).get(context);
			}
		}

		throw new InvalidParameterException("SettingKey '" + backendName + "' not found.");
	}

	public Integer getMultiSetting(String backendName) {
		return this.getMultiSetting(backendName, currentContext);
	}

	public Integer getMultiSetting(String backendName, String context) {
		for (Setting<?> setting : this.Settings) {
			if (MultiSetting.class.isAssignableFrom(setting.getClass()) && setting.backendName.equals(backendName)) {
				return ((MultiSetting) setting).get(context);
			}
		}

		throw new InvalidParameterException("SettingMulti '" + backendName + "' not found.");
	}

	public String getMultiSettingLabel(String backendName) {
		return this.getMultiSettingLabel(backendName, currentContext);
	}

	public String getMultiSettingLabel(String backendName, String context) {
		for (Setting<?> setting : this.Settings) {
			if (MultiSetting.class.isAssignableFrom(setting.getClass()) && setting.backendName.equals(backendName)) {
				return ((MultiSetting) setting).getLabel(context);
			}
		}

		throw new InvalidParameterException("SettingMulti '" + backendName + "' not found.");
	}

	public String getTextSetting(String backendName) {
		return this.getTextSetting(backendName, currentContext);
	}

	public String getTextSetting(String backendName, String context) {
		for (Setting<?> setting : this.Settings) {
			if (TextSetting.class.isAssignableFrom(setting.getClass()) && setting.backendName.equals(backendName)) {
				return ((TextSetting) setting).get(context);
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
