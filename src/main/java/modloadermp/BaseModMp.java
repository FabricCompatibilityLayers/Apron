package modloadermp;

import io.github.betterthanupdates.Legacy;
import modloader.BaseMod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.CommandSource;
import net.minecraft.server.entity.player.ServerPlayerEntity;

import io.github.betterthanupdates.apron.Apron;

@SuppressWarnings("unused")
@Legacy
public abstract class BaseModMp extends BaseMod {
	@Legacy
	public BaseModMp() {
	}

	@Legacy
	public final int getId() {
		return this.toString().hashCode();
	}

	@Legacy
	public void ModsLoaded() {
		if (Apron.getEnvironment().equals(EnvType.CLIENT)) {
			ModLoaderMp.Init();
		} else {
			ModLoaderMp.InitModLoaderMp();
		}
	}

	@Legacy
	@Environment(EnvType.CLIENT)
	public void HandlePacket(final ModLoaderPacket packet) {
	}

	@Legacy
	@Environment(EnvType.CLIENT)
	public void HandleTileEntityPacket(final int i, final int j, final int k, final int l, final int[] ai, final float[] af, final String[] as) {
	}

	@Legacy
	@Environment(EnvType.CLIENT)
	public Screen HandleGUI(final int invType) {
		return null;
	}

	@Legacy
	@Environment(EnvType.SERVER)
	public void HandlePacket(ModLoaderPacket modloaderPacket, ServerPlayerEntity entityplayermp) {
	}

	@Legacy
	@Environment(EnvType.SERVER)
	public void HandleLogin(ServerPlayerEntity entityplayermp) {
	}

	@Legacy
	@Environment(EnvType.SERVER)
	public void HandleSendKey(ServerPlayerEntity entityplayermp, int i) {
	}

	@Legacy
	@Environment(EnvType.SERVER)
	public void GetCommandInfo(CommandSource icommandlistener) {
	}

	@Legacy
	@Environment(EnvType.SERVER)
	public boolean HandleCommand(String s, String s1, CommandSource icommandlistener, CommandManager consolecommandhandler) {
		return false;
	}

	@Legacy
	@Environment(EnvType.SERVER)
	public boolean hasClientSide() {
		return true;
	}
}
