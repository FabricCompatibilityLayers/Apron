package reforged;

import forge.ForgeHooks;
import forge.MinecraftForge;
import modloadermp.BaseModMp;

import net.minecraft.block.Block;
import net.minecraft.client.MultiplayerClientInteractionManager;
import net.minecraft.client.SingleplayerInteractionManager;
import net.minecraft.item.Item;

import io.github.betterthanupdates.Legacy;

@Legacy
public class ReforgedMod extends BaseModMp {
	public ReforgedMod() {
		try {
			Class.forName(Block.class.getName());
			Class.forName(Item.class.getName());
			Class.forName(MultiplayerClientInteractionManager.class.getName());
			Class.forName(SingleplayerInteractionManager.class.getName());
		} catch (NoSuchMethodError | ClassNotFoundException e) {
			e.printStackTrace();
			MinecraftForge.killMinecraft("mod_Reforged", "Something concerning happened.");
		}
	}

	public String Version() {
		return String.format("[Forge %d.%d.%d, Reforged %d.%d.%d]",
				ForgeHooks.majorVersion, ForgeHooks.minorVersion, ForgeHooks.revisionVersion,
				ReforgedHooks.majorVersion, ReforgedHooks.minorVersion, ReforgedHooks.revisionVersion);
	}
}
