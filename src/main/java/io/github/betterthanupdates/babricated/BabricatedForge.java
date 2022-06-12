package io.github.betterthanupdates.babricated;

import forge.MinecraftForge;
import fr.catcore.modremapperapi.utils.Constants;
import modloader.ModLoader;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.ApiStatus;
import org.spongepowered.include.com.google.common.collect.ImmutableList;

import java.io.File;
import java.util.List;

@ApiStatus.Internal
public final class BabricatedForge {
	// Logging
	public static final Logger LOGGER = LogManager.getLogger(BabricatedForge.class);
	public static final java.util.logging.Logger MOD_LOADER_LOGGER = ModLoader.getLogger();
	public static final Logger FORGE_LOGGER = MinecraftForge.LOGGER;

	public static final File MOD_CACHE_FOLDER = new File(Constants.VERSIONED_FOLDER, "mods");

	public static final List<String> BUILT_IN_MOD_LOADER_MOD_CLASSES = ImmutableList.of("ShockAhPI");

	public static final ModContainer MOD_CONTAINER = FabricLoader.getInstance().getModContainer("babricated-forge").get();

	static {
		if (!MOD_CACHE_FOLDER.mkdirs()) {
			LOGGER.debug("Could not create mod cache directory. It may already exist.");
		}
	}
}
