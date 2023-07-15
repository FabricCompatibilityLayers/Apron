package io.github.betterthanupdates.apron;

import net.fabricmc.loader.api.FabricLoader;

public class LifecycleUtils {
	public static String CURRENT_MOD = null;

	public static void triggerModsAllLoadedEvent() {
		FabricLoader.getInstance().getEntrypoints("apron:loading_done", Runnable.class)
				.forEach(Runnable::run);
	}
}
