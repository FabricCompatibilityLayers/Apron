package reforged;

import forge.ForgeHooks;
import forge.MinecraftForge;
import io.github.betterthanupdates.Legacy;
import modloadermp.BaseModMp;

import net.minecraft.block.Block;
import net.minecraft.client.MultiplayerClientInteractionManager;
import net.minecraft.client.SingleplayerInteractionManager;
import net.minecraft.item.Item;

@Legacy
public class mod_Reforged extends BaseModMp {
	@Legacy
	public mod_Reforged() {
		try {
			Class.forName(Block.class.getName());
			Class.forName(Item.class.getName());
		} catch (NoSuchMethodError | ClassNotFoundException var3) {
			if (Reforged.hasIDResolver()) {
				MinecraftForge.killMinecraft("mod_Reforged", "Please install Reforged after IDResolver!");
			} else {
				MinecraftForge.killMinecraft("mod_Reforged", "Block or Item was modified. Please fix your installation!");
			}
		}

		try {
			Class.forName(MultiplayerClientInteractionManager.class.getName());
			Class.forName(SingleplayerInteractionManager.class.getName());
		} catch (NoSuchMethodError | ClassNotFoundException var2) {
			if (Reforged.hasSAPI()) {
				MinecraftForge.killMinecraft("mod_Reforged", "Please install Reforged after the ForgeSAPI patch and SAPI!");
			} else {
				MinecraftForge.killMinecraft("mod_Reforged", "PlayerControllers were modified. Please fix your installation!");
			}
		}

	}

	@Legacy
	public String Version() {
		return String.format("[Forge %d.%d.%d, Reforged %d.%d.%d]",
				ForgeHooks.majorVersion, ForgeHooks.minorVersion, ForgeHooks.revisionVersion,
				ReforgedHooks.majorVersion, ReforgedHooks.minorVersion, ReforgedHooks.revisionVersion);
	}
}
