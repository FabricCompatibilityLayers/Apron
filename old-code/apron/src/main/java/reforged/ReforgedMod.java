package reforged;

import forge.ForgeHooks;
import modloadermp.BaseModMp;

import io.github.betterthanupdates.Legacy;

@Legacy
public class ReforgedMod extends BaseModMp {
	public ReforgedMod() {
	}

	public String Version() {
		return String.format("[Forge %d.%d.%d, Reforged %d.%d.%d]",
				ForgeHooks.majorVersion, ForgeHooks.minorVersion, ForgeHooks.revisionVersion,
				ReforgedHooks.majorVersion, ReforgedHooks.minorVersion, ReforgedHooks.revisionVersion);
	}
}
