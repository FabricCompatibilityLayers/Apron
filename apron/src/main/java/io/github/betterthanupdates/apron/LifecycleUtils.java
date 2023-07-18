package io.github.betterthanupdates.apron;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.fabricmc.loader.api.FabricLoader;

public class LifecycleUtils {
	public static String CURRENT_MOD = null;

	public static void triggerModsAllLoadedEvent() {
		FabricLoader.getInstance().getEntrypoints("apron:loading_done", Runnable.class)
				.forEach(Runnable::run);
	}

	public static final List<File> MOD_FILES = new ArrayList<>();
}
