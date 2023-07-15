package io.github.betterthanupdates.apron.stapi;

import net.modificationstation.stationapi.api.registry.ModID;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static io.github.betterthanupdates.apron.LifecycleUtils.CURRENT_MOD;

public class ApronStAPICompat {
	public static boolean isModLoaderTime() {
		return CURRENT_MOD != null;
	}

	public static ModID getModID() {
		return ModID.of(CURRENT_MOD);
	}

	private static final Map<ModID, ModContents> MOD_CONTENTS = new HashMap<>();

	public static ModContents getModContent(ModID modID) {
		if (!MOD_CONTENTS.containsKey(modID)) {
			MOD_CONTENTS.put(modID, new ModContents());
		}

		return MOD_CONTENTS.get(modID);
	}

	public static ModContents getModContent() {
		return getModContent(getModID());
	}

	public static Set<Map.Entry<ModID, ModContents>> getModContents() {
		return MOD_CONTENTS.entrySet();
	}
}
