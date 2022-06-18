package io.github.betterthanupdates.babricated;

import fr.catcore.modremapperapi.utils.Constants;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.legacyfabric.fabric.api.logger.v1.Logger;
import org.jetbrains.annotations.ApiStatus;

import java.io.File;

@ApiStatus.Internal
public final class BabricatedForge {
	public static final String MOD_ID = "babricated-forge";
	public static long fabricModCount = 0, rmlModCount = 0;

	// Logging
	public static final Logger LOGGER;

	public static final File MOD_CACHE_FOLDER;

	public static final ModContainer MOD_CONTAINER;

	static {
		LOGGER = Logger.get("Babricated Forge");

		MOD_CACHE_FOLDER = new File(Constants.VERSIONED_FOLDER, "mods");
		if (!(MOD_CACHE_FOLDER.exists() || MOD_CACHE_FOLDER.mkdirs())) {
			throw new RuntimeException("Could not get or create mod cache folder!");
		}

		MOD_CONTAINER = FabricLoader.getInstance().getModContainer("babricated-forge")
				.orElseThrow(RuntimeException::new);
	}

	/**
	 * For use in screen mixins
	 * @param original what the line originally said
	 * @return the string to replace it with
	 */
	public static String versionString(final String original) {
		return "Babricated Minecraft 1.7.3" + (original.endsWith(" (") ? " (" : "");
	}

	/**
	 * For use in screen mixins.
	 * @return a string telling how many mods are loaded from ModLoader
	 */
	public static String rmlModsLoaded() {
		return rmlModCount + " RML mods";
	}

	public static String fabricModsLoaded() {
		return fabricModCount + " Fabric mods";
	}
}
