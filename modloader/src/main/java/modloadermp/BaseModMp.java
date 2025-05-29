package modloadermp;

import modloader.BaseMod;
import net.minecraft.client.gui.screen.Screen;

public abstract class BaseModMp extends BaseMod {
	public final int getId() {
		return this.toString().hashCode();
	}

	public void ModsLoaded() {
		ModLoaderMp.Init();
	}

	public void HandlePacket(Packet230ModLoader packet230modloader) {
	}

	public void HandleTileEntityPacket(int i, int j, int k, int l, int[] ai, float[] af, String[] as) {
	}

	public Screen HandleGUI(int i) {
		return null;
	}
}
