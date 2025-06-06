package modloadermp;

import io.github.fabriccompatibilitylayers.modloader.ApronModLoader;
import modloader.BaseMod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandHandler;

public abstract class BaseModMp extends BaseMod {
	public final int getId() {
		return this.toString().hashCode();
	}

	public void ModsLoaded() {
		if (ApronModLoader.IS_CLIENT) ModLoaderMp.Init();
		else ModLoaderMp.InitModLoaderMp();
	}

	@Environment(EnvType.CLIENT)
	public void HandlePacket(Packet230ModLoader packet230modloader) {
	}

	@Environment(EnvType.CLIENT)
	public void HandleTileEntityPacket(int i, int j, int k, int l, int[] ai, float[] af, String[] as) {
	}

	@Environment(EnvType.CLIENT)
	public Screen HandleGUI(int i) {
		return null;
	}

	@Environment(EnvType.SERVER)
	public void HandlePacket(Packet230ModLoader packet230modloader, ServerPlayerEntity entityplayermp) {
	}

	@Environment(EnvType.SERVER)
	public void HandleLogin(ServerPlayerEntity entityplayermp) {
	}

	@Environment(EnvType.SERVER)
	public void HandleSendKey(ServerPlayerEntity entityplayermp, int i) {
	}

	@Environment(EnvType.SERVER)
	public void GetCommandInfo(CommandOutput icommandlistener) {
	}

	@Environment(EnvType.SERVER)
	public boolean HandleCommand(String s, String s1, CommandOutput icommandlistener, ServerCommandHandler consolecommandhandler) {
		return false;
	}

	@Environment(EnvType.SERVER)
	public boolean hasClientSide() {
		return true;
	}
}
