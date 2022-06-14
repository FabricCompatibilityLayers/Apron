package io.github.betterthanupdates.babricated;

import forge.MinecraftForge;
import fr.catcore.modremapperapi.utils.Constants;
import modloader.ModLoader;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.ApiStatus;

import java.io.File;
import java.nio.file.FileSystemException;

@ApiStatus.Internal
public final class BabricatedForge {
	// Logging
	public static final Logger LOGGER = LogManager.getLogger(BabricatedForge.class);
	public static final java.util.logging.Logger MOD_LOADER_LOGGER = ModLoader.getLogger();
	public static final Logger FORGE_LOGGER = MinecraftForge.LOGGER;

	public static final File MOD_CACHE_FOLDER = new File(Constants.VERSIONED_FOLDER, "mods");

	public static final ModContainer MOD_CONTAINER = FabricLoader.getInstance().getModContainer("babricated-forge").get();

	static {
		if (!MOD_CACHE_FOLDER.mkdirs()) {
			LOGGER.debug("Could not create mod cache directory. It may already exist.");
		}
	}
}
