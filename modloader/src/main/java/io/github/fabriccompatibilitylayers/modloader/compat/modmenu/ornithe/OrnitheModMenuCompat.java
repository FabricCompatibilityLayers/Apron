package io.github.fabriccompatibilitylayers.modloader.compat.modmenu.ornithe;

import com.terraformersmc.modmenu.ModMenu;
import modloader.ModLoader;

import java.util.stream.Collectors;

public class OrnitheModMenuCompat {
	public static void init() {
		try {
			initModMenu();
		} catch (Exception e) {}
	}

	private static void initModMenu() {
		ModMenu.MODS.putAll(
				ModLoader.getLoadedMods().stream().map(MLMod::new).collect(Collectors.toMap(
						MLMod::getId,
						mod -> mod
				))
		);
	}
}
