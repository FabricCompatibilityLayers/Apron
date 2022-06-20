package modloadermp;

import modloader.BaseMod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.CommandSource;
import net.minecraft.server.entity.player.ServerPlayerEntity;

@SuppressWarnings("unused")
public abstract class BaseModMp extends BaseMod {
	public BaseModMp() {
	}

	public final int getId() {
		return this.toString().hashCode();
	}

	public void ModsLoaded() {
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
			ModLoaderMp.Init();
		} else {
			ModLoaderMp.InitModLoaderMp();
		}
	}

	@Environment(EnvType.CLIENT)
	public void HandlePacket(final Packet230ModLoader packet) {
	}

	@Environment(EnvType.CLIENT)
	public void HandleTileEntityPacket(final int i, final int j, final int k, final int l, final int[] ai, final float[] af, final String[] as) {
	}

	@Environment(EnvType.CLIENT)
	public Screen HandleGUI(final int invType) {
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
	public void GetCommandInfo(CommandSource icommandlistener) {
	}

	@Environment(EnvType.SERVER)
	public boolean HandleCommand(String s, String s1, CommandSource icommandlistener, CommandManager consolecommandhandler) {
		return false;
	}

	@Environment(EnvType.SERVER)
	public boolean hasClientSide() {
		return true;
	}
}
