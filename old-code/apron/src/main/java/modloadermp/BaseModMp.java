package modloadermp;

import modloader.BaseMod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.class_39;
import net.minecraft.class_426;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.ServerPlayerEntity;
import io.github.betterthanupdates.Legacy;
import io.github.betterthanupdates.apron.Apron;

@SuppressWarnings("unused")
@Legacy
public abstract class BaseModMp extends BaseMod {
	public BaseModMp() {
	}

	public final int getId() {
		return this.toString().hashCode();
	}

	public void ModsLoaded() {
		if (Apron.getEnvironment().equals(EnvType.CLIENT)) {
			ModLoaderMp.Init();
		} else {
			ModLoaderMp.InitModLoaderMp();
		}
	}

	@Environment(EnvType.CLIENT)
	public void HandlePacket(final ModLoaderPacket packet) {
	}

	@Environment(EnvType.CLIENT)
	public void HandleTileEntityPacket(final int i, final int j, final int k, final int l, final int[] ai, final float[] af, final String[] as) {
	}

	@Environment(EnvType.CLIENT)
	public Screen HandleGUI(final int invType) {
		return null;
	}

	@Environment(EnvType.SERVER)
	public void HandlePacket(ModLoaderPacket modloaderPacket, ServerPlayerEntity entityplayermp) {
	}

	@Environment(EnvType.SERVER)
	public void HandleLogin(ServerPlayerEntity entityplayermp) {
	}

	@Environment(EnvType.SERVER)
	public void HandleSendKey(ServerPlayerEntity entityplayermp, int i) {
	}

	@Environment(EnvType.SERVER)
	public void GetCommandInfo(class_39 icommandlistener) {
	}

	@Environment(EnvType.SERVER)
	public boolean HandleCommand(String s, String s1, class_39 icommandlistener, class_426 consolecommandhandler) {
		return false;
	}

	@Environment(EnvType.SERVER)
	public boolean hasClientSide() {
		return true;
	}
}
