package modoptionsapi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import net.minecraft.client.Minecraft;

public class ModOptionsAPI {
	private static TreeMap<String, ModOptions> modOptions = new TreeMap<>();
	private static boolean ingame = false;
	private static boolean multiplayerWorld = false;

	public ModOptionsAPI() {
	}

	public static boolean worldLoaded() {
		return ingame;
	}

	public static boolean isMultiplayerWorld() {
		return multiplayerWorld;
	}

	public static void joinedMultiplayerWorld(String s) {
		ModOptions[] m = getMultiplayerMods();

		for (ModOptions op : m) {
			op.loadValues(s, true);
		}

		multiplayerWorld = true;
		ingame = true;
	}

	public static void selectedWorld(String s) {
		ModOptions[] m = getSingleplayerMods();

		for (ModOptions op : m) {
			op.loadValues(s, false);
		}

		multiplayerWorld = false;
		ingame = true;
	}

	public static void viewingMainMenu() {
		multiplayerWorld = false;
		ingame = false;
	}

	public static ModOptions[] getAllMods() {
		Set<Map.Entry<String, ModOptions>> s = modOptions.entrySet();
		ModOptions[] m = new ModOptions[s.size()];
		int i = 0;

		for (Map.Entry<String, ModOptions> e : s) {
			m[i] = e.getValue();
			++i;
		}

		return m;
	}

	public static ModOptions[] getMultiplayerMods() {
		Set<Map.Entry<String, ModOptions>> s = modOptions.entrySet();
		LinkedList<ModOptions> m = new LinkedList<>();

		for (Map.Entry<String, ModOptions> e : s) {
			if (e.getValue().isMultiplayerMod()) {
				m.add(e.getValue());
			}
		}

		return m.toArray(new ModOptions[0]);
	}

	public static ModOptions[] getSingleplayerMods() {
		Set<Map.Entry<String, ModOptions>> s = modOptions.entrySet();
		LinkedList<ModOptions> m = new LinkedList<>();

		for (Map.Entry<String, ModOptions> e : s) {
			if (e.getValue().isSingleplayerMod()) {
				m.add(e.getValue());
			}
		}

		return m.toArray(new ModOptions[0]);
	}

	public static ModOptions getModOptions(String name) {
		return modOptions.get(name);
	}

	public static void addMod(ModOptions o) {
		modOptions.put(o.getName(), o);
	}

	public static ModOptions addMod(String name) throws MOMissingModException {
		File file = getFile(name);
		ModOptions mod = new ModOptions(name);

		if (file == null) {
			throw new MOMissingModException(name + " mod is missing");
		} else {
			try {
				applyModFile(mod, file);
			} catch (FileNotFoundException var4) {
				throw new MOMissingModException(name + " mod is missing");
			} catch (IOException var5) {
				System.out.println("(ModOptionsAPI): IOException occured: " + var5.getMessage());
				throw new MOMissingModException(name + " mod is missing due to an IOException");
			}

			addMod(mod);
			return mod;
		}
	}

	private static void applyModFile(ModOptions mod, File file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		ModOptions cur = mod;

		String line;

		while ((line = reader.readLine()) != null) {
			if (isSection(line)) {
				cur = parseSection(line, mod);
			} else if (isOption(line)) {
				try {
					parseOption(line, cur);
				} catch (IncompatibleOptionTypeException var6) {
					System.out.println(var6.getMessage());
				}
			}
		}
	}

	private static boolean isOption(String line) {
		return line.contains(":") && line.split(":").length > 1;
	}

	private static void parseOption(String line, ModOptions mod) throws IncompatibleOptionTypeException {
		ModOption<?> mo = null;
		String[] sections = line.trim().split(":");
		String name = sections[0].trim();
		String type = sections[1].trim().toLowerCase();
		String[] params = new String[0];

		if (sections.length > 2) {
			sections[2] = sections[2].trim();
			params = sections[2].substring(1, sections[2].length() - 1).split(",");

			if (sections.length > 3 && sections[3].trim().toUpperCase().equals("WIDE")) {
				mod.setWideOption(name);
			}
		}

		ModOption<?> var7;

		switch (type) {
			case "boolean":
				if (params.length > 1) {
					var7 = new ModBooleanOption(name, params[0].trim(), params[1].trim());
				} else {
					var7 = new ModBooleanOption(name);
				}

				break;
			case "multi":
				var7 = new ModMultiOption(name, params);
				break;
			case "mappedmulti":
				var7 = parseMappedMultiOption(name, params, mod);
				break;
			case "slider":
				var7 = parseSliderOption(name, params, mod);
				break;
			case "text":
				var7 = parseTextOption(name, params, mod);
				break;
			default:
				if (!type.equals("keybinding")) {
					throw new IncompatibleOptionTypeException(type + " is an invalid option type in mod " + mod.getName());
				}

				var7 = parseKeyBinding(name, params, mod);
				break;
		}

		mod.addOption(var7);
	}

	private static ModMappedMultiOption parseMappedMultiOption(String name, String[] params, ModOptions mod) {
		ModMappedMultiOption mo = new ModMappedMultiOption(name);

		for (String param : params) {
			String[] tmp = param.trim().split("=");

			try {
				if (tmp.length == 2) {
					String value = tmp[1];
					Integer key = Integer.parseInt(tmp[0]);
					mo.addValue(key, value);
				}
			} catch (NumberFormatException var8) {
				System.out.println("Number format for key value (" + tmp[0] + ") invalid for option " + name + " in mod " + mod.getName());
			}
		}

		return mo;
	}

	private static ModOption<?> parseSliderOption(String name, String[] params, ModOptions mod) {
		ModSliderOption mo;

		try {
			if (params.length > 1) {
				mo = new ModSliderOption(name, Integer.parseInt(params[0].trim()), Integer.parseInt(params[1].trim()));
			} else {
				mo = new ModSliderOption(name);
			}
		} catch (NumberFormatException var5) {
			System.out.println("Number format for high or low value invalid for option " + name + " in mod " + mod.getName());
			mo = new ModSliderOption(name);
		}

		return mo;
	}

	private static ModOption<?> parseTextOption(String name, String[] params, ModOptions mod) {
		ModOption<?> mo;

		try {
			if (params.length > 1) {
				mo = new ModTextOption(name, Integer.parseInt(params[0].trim()));
			} else {
				mo = new ModTextOption(name);
			}
		} catch (NumberFormatException var5) {
			System.out.println("Number must be a valid integer for " + name + " in mod " + mod.getName() + ". Using infinite");
			mo = new ModTextOption(name);
		}

		return mo;
	}

	private static ModOption<?> parseKeyBinding(String name, String[] params, ModOptions mod) {
		return new ModKeyOption(name);
	}

	private static ModOptions parseSection(String line, ModOptions mod) {
		ModOptions cur = mod;
		line = line.trim();
		line = line.substring(1, line.length() - 1);
		String[] parts = line.split(":");
		String name = parts[0].trim();
		boolean multi = false;
		boolean single = false;

		if (parts.length > 1) {
			String[] modParts = parts[1].split(",");

			for (String modifier : modParts) {
				modifier = modifier.trim().toUpperCase();

				if (modifier.equals("MULTIPLAYER")) {
					multi = true;
				} else if (modifier.equals("SINGLEPLAYER")) {
					single = true;
				}
			}
		}

		String[] path = name.split("/");

		for (String s : path) {
			String tmp = s.trim();

			if (cur.containsSubOptions(tmp)) {
				cur = mod.getSubOption(tmp);
			} else {
				ModOptions parent = cur;
				cur = new ModOptions(tmp);
				parent.addSubOptions(cur);
			}
		}

		cur.setSingleplayerMode(single);
		cur.setMultiplayerMode(multi);
		return cur;
	}

	private static boolean isSection(String line) {
		return line.startsWith("[") && line.endsWith("]");
	}

	private static File getFile(String name) {
		File file = new File(Minecraft.getRunDirectory() + "/ModOptions/" + name + "/" + name + ".modoptions");
		return !file.exists() ? null : file;
	}
}
