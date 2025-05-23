package io.github.betterthanupdates.apron;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.fabricmc.loader.api.FabricLoader;
import shockahpi.DimensionBase;

public class LifecycleUtils {
	public static String CURRENT_MOD = null;

	public static void triggerModsAllLoadedEvent() {
		FabricLoader.getInstance().getEntrypoints("apron:loading_done", Runnable.class)
				.forEach(Runnable::run);
	}

	public static final List<File> MOD_FILES = new ArrayList<>();

	public static final List<String> MOD_ENTITIES = new ArrayList<>();
	public static final List<String> MOD_BLOCK_ENTITIES = new ArrayList<>();

	public static final Map<String, String> CACHED_TRANSLATIONS = new HashMap<>();

	public static final Map<String, List<DimensionBase>> SHOCKAHPI_DIMENSIONS = new HashMap<>();

	public static final List<String> MOD_LIST = new ArrayList<>();
}
