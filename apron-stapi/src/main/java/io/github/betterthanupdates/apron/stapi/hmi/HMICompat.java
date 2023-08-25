package io.github.betterthanupdates.apron.stapi.hmi;

import net.glasslauncher.hmifabric.tabs.TabRegistry;

public class HMICompat {
	public static void regenerateRecipeList() {
		TabRegistry.INSTANCE.forEach(tab -> {
			((HMITab) tab).apron$updateRecipeList();
		});
	}
}
