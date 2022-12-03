package io.github.betterthanupdates.bhapi;

import net.bhapi.util.ItemUtil;

public class BHApiHelper {
	public static void unfreezeItemRegistry() {
		ItemUtil.setFrozen(false);
	}

	public static void freezeItemRegistry() {
		ItemUtil.setFrozen(true);
	}
}
