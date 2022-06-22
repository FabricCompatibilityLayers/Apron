package reforged;

import forge.MinecraftForge;
import modloadermp.BaseModMp;

import net.minecraft.block.Block;
import net.minecraft.client.MultiplayerClientInteractionManager;
import net.minecraft.client.SingleplayerInteractionManager;
import net.minecraft.item.Item;

public class mod_Reforged extends BaseModMp {
	public mod_Reforged() {
		try {
			Block.touch();
			Item.touch();
		} catch (NoSuchMethodError var3) {
			if (Reforged.hasIDResolver()) {
				MinecraftForge.killMinecraft("mod_Reforged", "Please install Reforged after IDResolver!");
			} else {
				MinecraftForge.killMinecraft("mod_Reforged", "Block or Item was modified. Please fix your installation!");
			}
		}

		try {
			MultiplayerClientInteractionManager.touch();
			SingleplayerInteractionManager.touch();
		} catch (NoSuchMethodError var2) {
			if (Reforged.hasSAPI()) {
				MinecraftForge.killMinecraft("mod_Reforged", "Please install Reforged after the ForgeSAPI patch and SAPI!");
			} else {
				MinecraftForge.killMinecraft("mod_Reforged", "PlayerControllers were modified. Please fix your installation!");
			}
		}

	}

	public String Version() {
		return String.format("[Forge %d.%d.%d, Reforged %d.%d.%d]", 1, 0, 6, 1, 0, 0);
	}
}
