package io.github.betterthanupdates.babricated;

import fr.catcore.modremapperapi.utils.Constants;
import modloader.BaseMod;
import modloader.ModLoader;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.include.com.google.common.collect.ImmutableList;
import shockahpi.ShockAhPI;

import java.io.File;
import java.util.List;
import java.util.Optional;

/**
 * Internal class used for storing static members used by all modules of the project.
 */
@ApiStatus.Internal
public final class BabricatedForge {
	public static final String MOD_ID = "babricated-forge";
	public static long fabricModCount = 0, rmlModCount = 0;

	// Logging
	/**
	 * Logger for internal {@link io.github.betterthanupdates.babricated babricated} use.
	 */
	public static final Logger LOGGER;

	// ModLoader
	/**
	 * {@link fr.catcore.modremapperapi Mod Remapping API} places remapped {@link ModLoader} mods in this folder.
	 */
	@NotNull
	public static final File MOD_CACHE_FOLDER;
	/**
	 * ModLoader mods that are shipped with Babricated Forge.<br>
	 * They will have had their names changed, so they no longer meat the ModLoader spec.
	 *
	 * @see ShockAhPI
	 */
	public static final List<BaseMod> BUILT_IN_RML_MODS;

	/**
	 * Fabric mod container for Babricated Forge, represents the root of the mod JAR archive.
	 */
	public static final Optional<ModContainer> MOD_CONTAINER;

	static {
		LOGGER = LogManager.getLogger(BabricatedForge.class);

		MOD_CACHE_FOLDER = new File(Constants.VERSIONED_FOLDER, "libs");
		if (!MOD_CACHE_FOLDER.mkdirs()) {
			LOGGER.warn("Could not create mod cache directory. It may already exist.");
		}
		BUILT_IN_RML_MODS = ImmutableList.of(
				new ShockAhPI()
		);

		MOD_CONTAINER = FabricLoader.getInstance().getModContainer(MOD_ID);
	}

	/**
	 * For use in screen mixins
	 * @param original what the line originally said
	 * @return the string to replace it with
	 */
	public static String versionString(final String original) {
		return "Babricated Minecraft Beta 1.7.3" + (original.endsWith("(") ? " (" : "");
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
