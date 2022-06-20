package io.github.betterthanupdates.modloader.mixin.server;

import java.util.logging.Logger;

import modloadermp.ModLoaderMp;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.packet.play.ChatMessagePacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerPlayerConnectionManager;
import net.minecraft.server.command.Command;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.CommandSource;
import net.minecraft.server.entity.player.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

@Environment(EnvType.SERVER)
@Mixin(CommandManager.class)
public abstract class CommandManagerMixin {
	@Shadow
	private MinecraftServer server;

	@Shadow
	protected abstract void sendHelp(CommandSource arg);

	@Shadow
	protected abstract void sendFeedbackAndLog(String string, String string2);

	@Shadow
	protected abstract int parseIntOrElse(String string, int i);

	@Shadow
	private static Logger LOGGER;

	@Shadow
	protected abstract void processWhitelistCommand(String string, String string2, CommandSource arg);

	/**
	 * @author Risugami
	 * @reason yes
	 */
	@Overwrite
	public void processCommand(Command servercommand) {
		String s = servercommand.commandString;
		CommandSource icommandlistener = servercommand.source;
		String s1 = icommandlistener.getName();
		ServerPlayerConnectionManager serverconfigurationmanager = this.server.serverPlayerConnectionManager;

		if (s.toLowerCase().startsWith("help") || s.toLowerCase().startsWith("?")) {
			this.sendHelp(icommandlistener);
		} else if (s.toLowerCase().startsWith("list")) {
			icommandlistener.sendFeedback("Connected players: " + serverconfigurationmanager.getAllPlayerNames());
		} else if (s.toLowerCase().startsWith("stop")) {
			this.sendFeedbackAndLog(s1, "Stopping the server..");
			this.server.stopRunning();
		} else if (s.toLowerCase().startsWith("save-all")) {
			this.sendFeedbackAndLog(s1, "Forcing save..");

			if (serverconfigurationmanager != null) {
				serverconfigurationmanager.updateAllPlayers();
			}

			for (int i = 0; i < this.server.worlds.length; ++i) {
				ServerWorld worldserver = this.server.worlds[i];
				worldserver.saveLevel(true, null);
			}

			this.sendFeedbackAndLog(s1, "Save complete.");
		} else if (s.toLowerCase().startsWith("save-off")) {
			this.sendFeedbackAndLog(s1, "Disabling level saving..");

			for (int j = 0; j < this.server.worlds.length; ++j) {
				ServerWorld worldserver1 = this.server.worlds[j];
				worldserver1.field_275 = true;
			}
		} else if (s.toLowerCase().startsWith("save-on")) {
			this.sendFeedbackAndLog(s1, "Enabling level saving..");

			for (int k = 0; k < this.server.worlds.length; ++k) {
				ServerWorld worldserver2 = this.server.worlds[k];
				worldserver2.field_275 = false;
			}
		} else if (s.toLowerCase().startsWith("op ")) {
			String s2 = s.substring(s.indexOf(" ")).trim();
			serverconfigurationmanager.addOp(s2);
			this.sendFeedbackAndLog(s1, "Opping " + s2);
			serverconfigurationmanager.sendMessage(s2, "§eYou are now op!");
		} else if (s.toLowerCase().startsWith("deop ")) {
			String s3 = s.substring(s.indexOf(" ")).trim();
			serverconfigurationmanager.removeOp(s3);
			serverconfigurationmanager.sendMessage(s3, "§eYou are no longer op!");
			this.sendFeedbackAndLog(s1, "De-opping " + s3);
		} else if (s.toLowerCase().startsWith("ban-ip ")) {
			String s4 = s.substring(s.indexOf(" ")).trim();
			serverconfigurationmanager.addIpBan(s4);
			this.sendFeedbackAndLog(s1, "Banning ip " + s4);
		} else if (s.toLowerCase().startsWith("pardon-ip ")) {
			String s5 = s.substring(s.indexOf(" ")).trim();
			serverconfigurationmanager.removeIpBan(s5);
			this.sendFeedbackAndLog(s1, "Pardoning ip " + s5);
		} else if (s.toLowerCase().startsWith("ban ")) {
			String s6 = s.substring(s.indexOf(" ")).trim();
			serverconfigurationmanager.addBan(s6);
			this.sendFeedbackAndLog(s1, "Banning " + s6);
			ServerPlayerEntity entityplayermp = serverconfigurationmanager.getServerPlayer(s6);

			if (entityplayermp != null) {
				entityplayermp.packetHandler.kick("Banned by admin");
			}
		} else if (s.toLowerCase().startsWith("pardon ")) {
			String s7 = s.substring(s.indexOf(" ")).trim();
			serverconfigurationmanager.removeBan(s7);
			this.sendFeedbackAndLog(s1, "Pardoning " + s7);
		} else if (s.toLowerCase().startsWith("kick ")) {
			String s8 = s.substring(s.indexOf(" ")).trim();
			ServerPlayerEntity entityplayermp1 = null;

			for (int l = 0; l < serverconfigurationmanager.players.size(); ++l) {
				ServerPlayerEntity entityplayermp5 = (ServerPlayerEntity) serverconfigurationmanager.players.get(l);

				if (entityplayermp5.name.equalsIgnoreCase(s8)) {
					entityplayermp1 = entityplayermp5;
				}
			}

			if (entityplayermp1 != null) {
				entityplayermp1.packetHandler.kick("Kicked by admin");
				this.sendFeedbackAndLog(s1, "Kicking " + entityplayermp1.name);
			} else {
				icommandlistener.sendFeedback("Can't find user " + s8 + ". No kick.");
			}
		} else if (s.toLowerCase().startsWith("tp ")) {
			String[] as = s.split(" ");

			if (as.length == 3) {
				ServerPlayerEntity entityplayermp2 = serverconfigurationmanager.getServerPlayer(as[1]);
				ServerPlayerEntity entityplayermp3 = serverconfigurationmanager.getServerPlayer(as[2]);

				if (entityplayermp2 == null) {
					icommandlistener.sendFeedback("Can't find user " + as[1] + ". No tp.");
				} else if (entityplayermp3 == null) {
					icommandlistener.sendFeedback("Can't find user " + as[2] + ". No tp.");
				} else if (entityplayermp2.dimensionId != entityplayermp3.dimensionId) {
					icommandlistener.sendFeedback("User " + as[1] + " and " + as[2] + " are in different dimensions. No tp.");
				} else {
					entityplayermp2.packetHandler
							.method_832(entityplayermp3.x, entityplayermp3.y, entityplayermp3.z, entityplayermp3.yaw, entityplayermp3.pitch);
					this.sendFeedbackAndLog(s1, "Teleporting " + as[1] + " to " + as[2] + ".");
				}
			} else {
				icommandlistener.sendFeedback("Syntax error, please provice a source and a target.");
			}
		} else if (s.toLowerCase().startsWith("give ")) {
			String[] as1 = s.split(" ");

			if (as1.length != 3 && as1.length != 4) {
				return;
			}

			String s9 = as1[1];
			ServerPlayerEntity entityplayermp4 = serverconfigurationmanager.getServerPlayer(s9);

			if (entityplayermp4 != null) {
				try {
					int j1 = Integer.parseInt(as1[2]);

					if (Item.byId[j1] != null) {
						this.sendFeedbackAndLog(s1, "Giving " + entityplayermp4.name + " some " + j1);
						int i2 = 1;

						if (as1.length > 3) {
							i2 = this.parseIntOrElse(as1[3], 1);
						}

						if (i2 < 1) {
							i2 = 1;
						}

						if (i2 > 64) {
							i2 = 64;
						}

						entityplayermp4.dropItem(new ItemStack(j1, i2, 0));
					} else {
						icommandlistener.sendFeedback("There's no item with id " + j1);
					}
				} catch (NumberFormatException var11) {
					icommandlistener.sendFeedback("There's no item with id " + as1[2]);
				}
			} else {
				icommandlistener.sendFeedback("Can't find user " + s9);
			}
		} else if (s.toLowerCase().startsWith("time ")) {
			String[] as2 = s.split(" ");

			if (as2.length != 3) {
				return;
			}

			String s10 = as2[1];

			try {
				int i1 = Integer.parseInt(as2[2]);

				if ("add".equalsIgnoreCase(s10)) {
					for (int k1 = 0; k1 < this.server.worlds.length; ++k1) {
						ServerWorld worldserver3 = this.server.worlds[k1];
						worldserver3.setWorldTime(worldserver3.getWorldTime() + (long) i1);
					}

					this.sendFeedbackAndLog(s1, "Added " + i1 + " to time");
				} else if ("set".equalsIgnoreCase(s10)) {
					for (int l1 = 0; l1 < this.server.worlds.length; ++l1) {
						ServerWorld worldserver4 = this.server.worlds[l1];
						worldserver4.setWorldTime((long) i1);
					}

					this.sendFeedbackAndLog(s1, "Set time to " + i1);
				} else {
					icommandlistener.sendFeedback("Unknown method, use either \"add\" or \"set\"");
				}
			} catch (NumberFormatException var12) {
				icommandlistener.sendFeedback("Unable to convert time value, " + as2[2]);
			}
		} else if (s.toLowerCase().startsWith("say ")) {
			s = s.substring(s.indexOf(" ")).trim();
			LOGGER.info("[" + s1 + "] " + s);
			serverconfigurationmanager.sendToAll(new ChatMessagePacket("§d[Server] " + s));
		} else if (s.toLowerCase().startsWith("tell ")) {
			String[] as3 = s.split(" ");

			if (as3.length >= 3) {
				s = s.substring(s.indexOf(" ")).trim();
				s = s.substring(s.indexOf(" ")).trim();
				LOGGER.info("[" + s1 + "->" + as3[1] + "] " + s);
				s = "§7" + s1 + " whispers " + s;
				LOGGER.info(s);

				if (!serverconfigurationmanager.trySendPacket(as3[1], new ChatMessagePacket(s))) {
					icommandlistener.sendFeedback("There's no player by that name online.");
				}
			}
		} else if (s.toLowerCase().startsWith("whitelist ")) {
			this.processWhitelistCommand(s1, s, icommandlistener);
		} else if (!ModLoaderMp.handleCommand(s, s1, icommandlistener, (CommandManager) (Object) this)) {
			LOGGER.info("Unknown console command. Type \"help\" for help.");
		}
	}
}
