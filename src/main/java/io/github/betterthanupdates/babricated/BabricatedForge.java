package io.github.betterthanupdates.babricated;

import forge.MinecraftForge;
import fr.catcore.modremapperapi.utils.Constants;
import modloader.ModLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class BabricatedForge {
	// FIXME: Downloads should not be used! At the very least, they should not be hardcoded!
	protected static final String MODLOADER_URL = "";
	protected static final String MODLOADERMP_CLIENT_URL = "";
	protected static final String MODLOADERMP_SERVER_URL = "";
	protected static final String FORGE_CLIENT_URL = "";
	protected static final String FORGE_SERVER_URL = "";

	// Logging
	public static final Logger LOGGER = LogManager.getLogger(BabricatedForge.class);
	public static final java.util.logging.Logger MOD_LOADER_LOGGER = ModLoader.getLogger();
	public static final Logger FORGE_LOGGER = MinecraftForge.LOGGER;

	public static final File MOD_CACHE_FOLDER = new File(Constants.VERSIONED_FOLDER, "mods");

	static {
		MOD_CACHE_FOLDER.mkdirs();
	}
}
