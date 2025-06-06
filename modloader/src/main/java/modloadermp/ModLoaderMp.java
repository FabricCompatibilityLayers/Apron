package modloadermp;

import io.github.fabriccompatibilitylayers.modloader.ApronModLoader;
import io.github.fabriccompatibilitylayers.modloader.mixin.common.PacketAccessor;
import modloader.BaseMod;
import modloader.ModLoader;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.play.ChatMessagePacket;
import net.minecraft.network.packet.s2c.play.OpenScreenS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandHandler;
import net.minecraft.server.entity.EntityTracker;
import net.minecraft.world.ClientWorld;
import net.minecraft.world.ServerWorld;
import net.minecraft.world.World;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModLoaderMp {
	public static final String NAME = "ModLoaderMP";
	public static final String VERSION;
	private static boolean hasInit = false;

	@Environment(EnvType.CLIENT)
	private static boolean packet230Received;
	@Environment(EnvType.CLIENT)
	private static Map netClientHandlerEntityMap;
	@Environment(EnvType.CLIENT)
	private static Map guiModMap;

	@Environment(EnvType.SERVER)
	private static Map<Class, Pair> entityTrackerMap;
	@Environment(EnvType.SERVER)
	private static Map entityTrackerEntryMap;
	@Environment(EnvType.SERVER)
	private static List bannedMods;

	static {
		if (ApronModLoader.IS_CLIENT) {
			VERSION = "Beta 1.7.3 unofficial";
			packet230Received = false;
			netClientHandlerEntityMap = new HashMap();
			guiModMap = new HashMap();
		} else {
			VERSION = "Beta 1.6.6v4";
			entityTrackerMap = new HashMap();
			entityTrackerEntryMap = new HashMap();
			bannedMods = new ArrayList();
		}
	}

	@Environment(EnvType.CLIENT)
	public static void Init() {
		if (!hasInit) {
			init();
		}

	}

	@Environment(EnvType.CLIENT)
	public static void HandleAllPackets(Packet230ModLoader packet230modloader) {
		if (!hasInit) {
			init();
		}

		packet230Received = true;
		if (packet230modloader.modId == "ModLoaderMP".hashCode()) {
			switch (packet230modloader.packetType) {
				case 0:
					handleModCheck(packet230modloader);
					break;
				case 1:
					handleTileEntityPacket(packet230modloader);
			}
		} else if (packet230modloader.modId == "Spawn".hashCode()) {
			NetClientHandlerEntity netclienthandlerentity = HandleNetClientHandlerEntities(packet230modloader.packetType);
			if (netclienthandlerentity != null && ISpawnable.class.isAssignableFrom(netclienthandlerentity.entityClass)) {
				try {
					Entity entity = (Entity)netclienthandlerentity.entityClass.getConstructor(World.class).newInstance(ModLoader.getMinecraftInstance().world);
					((ISpawnable)entity).spawn(packet230modloader);
					((ClientWorld)ModLoader.getMinecraftInstance().world).forceEntity(entity.id, entity);
				} catch (Exception exception) {
					ModLoader.getLogger().throwing("ModLoader", "handleCustomSpawn", exception);
					ModLoader.ThrowException(String.format("Error initializing entity of type %s.", packet230modloader.packetType), exception);
					return;
				}
			}
		} else {
			for(int i = 0; i < ModLoader.getLoadedMods().size(); ++i) {
				BaseMod basemod = ModLoader.getLoadedMods().get(i);
				if (basemod instanceof BaseModMp) {
					BaseModMp basemodmp = (BaseModMp)basemod;
					if (basemodmp.getId() == packet230modloader.modId) {
						basemodmp.HandlePacket(packet230modloader);
						break;
					}
				}
			}
		}

	}

	@Environment(EnvType.CLIENT)
	public static NetClientHandlerEntity HandleNetClientHandlerEntities(int i) {
		if (!hasInit) {
			init();
		}

		return netClientHandlerEntityMap.containsKey(i) ? (NetClientHandlerEntity)netClientHandlerEntityMap.get(i) : null;
	}

	@Environment(EnvType.CLIENT)
	public static void SendPacket(BaseModMp basemodmp, Packet230ModLoader packet230modloader) {
		if (!hasInit) {
			init();
		}

		if (basemodmp == null) {
			IllegalArgumentException illegalargumentexception = new IllegalArgumentException("baseModMp cannot be null.");
			ModLoader.getLogger().throwing("ModLoaderMp", "SendPacket", illegalargumentexception);
			ModLoader.ThrowException("baseModMp cannot be null.", illegalargumentexception);
		} else {
			packet230modloader.modId = basemodmp.getId();
			sendPacket(packet230modloader);
		}

	}

	@Environment(EnvType.CLIENT)
	public static void RegisterGUI(BaseModMp basemodmp, int i) {
		if (!hasInit) {
			init();
		}

		if (guiModMap.containsKey(i)) {
			Log("RegisterGUI error: inventoryType already registered.");
		} else {
			guiModMap.put(i, basemodmp);
		}

	}

	@Environment(EnvType.CLIENT)
	public static void HandleGUI(OpenScreenS2CPacket packet100openwindow) {
		if (!hasInit) {
			init();
		}

		BaseModMp basemodmp = (BaseModMp)guiModMap.get(packet100openwindow.screenHandlerId);
		Screen guiscreen = basemodmp.HandleGUI(packet100openwindow.screenHandlerId);
		if (guiscreen != null) {
			ModLoader.OpenGUI(ModLoader.getMinecraftInstance().player, guiscreen);
			ModLoader.getMinecraftInstance().player.currentScreenHandler.syncId = packet100openwindow.syncId;
		}

	}

	@Environment(EnvType.CLIENT)
	public static void RegisterNetClientHandlerEntity(Class class1, int i) {
		RegisterNetClientHandlerEntity(class1, false, i);
	}

	@Environment(EnvType.CLIENT)
	public static void RegisterNetClientHandlerEntity(Class class1, boolean flag, int i) {
		if (!hasInit) {
			init();
		}

		if (i > 255) {
			Log("RegisterNetClientHandlerEntity error: entityId cannot be greater than 255.");
		} else if (netClientHandlerEntityMap.containsKey(i)) {
			Log("RegisterNetClientHandlerEntity error: entityId already registered.");
		} else {
			if (i > 127) {
				i -= 256;
			}

			netClientHandlerEntityMap.put(i, new NetClientHandlerEntity(class1, flag));
		}

	}

	@Environment(EnvType.CLIENT)
	public static void SendKey(BaseModMp basemodmp, int i) {
		if (!hasInit) {
			init();
		}

		if (basemodmp == null) {
			IllegalArgumentException illegalargumentexception = new IllegalArgumentException("baseModMp cannot be null.");
			ModLoader.getLogger().throwing("ModLoaderMp", "SendKey", illegalargumentexception);
			ModLoader.ThrowException("baseModMp cannot be null.", illegalargumentexception);
		} else {
			Packet230ModLoader packet230modloader = new Packet230ModLoader();
			packet230modloader.modId = "ModLoaderMP".hashCode();
			packet230modloader.packetType = 1;
			packet230modloader.dataInt = new int[]{basemodmp.getId(), i};
			sendPacket(packet230modloader);
		}

	}

	public static void Log(String s) {
		if (!ApronModLoader.IS_CLIENT) MinecraftServer.LOGGER.info(s);
		System.out.println(s);
		ModLoader.getLogger().fine(s);
	}

	private static void init() {
		hasInit = true;

		try {
			PacketAccessor.invokeRegister(230, true, true, Packet230ModLoader.class);
		} catch (SecurityException |
				 IllegalArgumentException illegalaccessexception) {
			ModLoader.getLogger().throwing("ModLoaderMp", "init", illegalaccessexception);
			ModLoader.ThrowException("An impossible error has occurred!", illegalaccessexception);
		}

		if (!ApronModLoader.IS_CLIENT) {
			try {
				File file = ModLoader.getMinecraftServerInstance().getFile("banned-mods.txt");
				if (!file.exists()) {
					file.createNewFile();
				}

				BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(Files.newInputStream(file.toPath())));

				String s;
				while((s = bufferedreader.readLine()) != null) {
					bannedMods.add(s);
				}
			} catch (IOException ioexception) {
				ModLoader.getLogger().throwing("ModLoader", "init", ioexception);
				ModLoader.ThrowException("ModLoaderMultiplayer", ioexception);
				return;
			}
		}

		Log("ModLoaderMP " + VERSION + " unofficial Initialized");
	}

	@Environment(EnvType.CLIENT)
	private static void handleModCheck(Packet230ModLoader packet230modloader) {
		Packet230ModLoader packet230modloader1 = new Packet230ModLoader();
		packet230modloader1.modId = "ModLoaderMP".hashCode();
		packet230modloader1.packetType = 0;
		packet230modloader1.dataString = new String[ModLoader.getLoadedMods().size()];

		for(int i = 0; i < ModLoader.getLoadedMods().size(); ++i) {
			packet230modloader1.dataString[i] = ModLoader.getLoadedMods().get(i).toString();
		}

		sendPacket(packet230modloader1);
	}

	@Environment(EnvType.CLIENT)
	private static void handleTileEntityPacket(Packet230ModLoader packet230modloader) {
		if (packet230modloader.dataInt != null && packet230modloader.dataInt.length >= 5) {
			int i = packet230modloader.dataInt[0];
			int j = packet230modloader.dataInt[1];
			int k = packet230modloader.dataInt[2];
			int l = packet230modloader.dataInt[3];
			int i1 = packet230modloader.dataInt[4];
			int[] ai = new int[packet230modloader.dataInt.length - 5];
			System.arraycopy(packet230modloader.dataInt, 5, ai, 0, packet230modloader.dataInt.length - 5);
			float[] af = packet230modloader.dataFloat;
			String[] as = packet230modloader.dataString;

			for(int j1 = 0; j1 < ModLoader.getLoadedMods().size(); ++j1) {
				BaseMod basemod = ModLoader.getLoadedMods().get(j1);
				if (basemod instanceof BaseModMp) {
					BaseModMp basemodmp = (BaseModMp)basemod;
					if (basemodmp.getId() == i) {
						basemodmp.HandleTileEntityPacket(j, k, l, i1, ai, af, as);
						break;
					}
				}
			}
		} else {
			Log("Bad TileEntityPacket received.");
		}

	}

	@Environment(EnvType.CLIENT)
	private static void sendPacket(Packet230ModLoader packet230modloader) {
		if (packet230Received && ModLoader.getMinecraftInstance().world != null && ModLoader.getMinecraftInstance().world.isRemote) {
			ModLoader.getMinecraftInstance().getNetworkHandler().sendPacket(packet230modloader);
		}

	}

	@Environment(EnvType.CLIENT)
	public static BaseModMp GetModInstance(Class class1) {
		for(int i = 0; i < ModLoader.getLoadedMods().size(); ++i) {
			BaseMod basemod = ModLoader.getLoadedMods().get(i);
			if (basemod instanceof BaseModMp) {
				BaseModMp basemodmp = (BaseModMp)basemod;
				if (class1.isInstance(basemodmp)) {
					return (BaseModMp)ModLoader.getLoadedMods().get(i);
				}
			}
		}

		return null;
	}

	@Environment(EnvType.SERVER)
	public static void InitModLoaderMp() {
		if (!hasInit) {
			init();
		}
	}

	@Environment(EnvType.SERVER)
	public static void RegisterEntityTracker(Class class1, int i, int j) {
		if (!hasInit) {
			init();
		}

		if (entityTrackerMap.containsKey(class1)) {
			System.out.println("RegisterEntityTracker error: entityClass already registered.");
		} else {
			entityTrackerMap.put(class1, new Pair(i, j));
		}
	}

	@Environment(EnvType.SERVER)
	public static void RegisterEntityTrackerEntry(Class class1, int i) {
		RegisterEntityTrackerEntry(class1, false, i);
	}

	@Environment(EnvType.SERVER)
	public static void RegisterEntityTrackerEntry(Class class1, boolean flag, int i) {
		if (!hasInit) {
			init();
		}

		if (i > 255) {
			System.out.println("RegisterEntityTrackerEntry error: entityId cannot be greater than 255.");
		}

		if (entityTrackerEntryMap.containsKey(class1)) {
			System.out.println("RegisterEntityTrackerEntry error: entityClass already registered.");
		} else {
			entityTrackerEntryMap.put(class1, new EntityTrackerEntry2(i, flag));
		}
	}

	@Environment(EnvType.SERVER)
	public static void HandleAllLogins(ServerPlayerEntity entityplayermp) {
		if (!hasInit) {
			init();
		}

		sendModCheck(entityplayermp);

		for(int i = 0; i < ModLoader.getLoadedMods().size(); ++i) {
			BaseMod basemod = ModLoader.getLoadedMods().get(i);
			if (basemod instanceof BaseModMp) {
				((BaseModMp)basemod).HandleLogin(entityplayermp);
			}
		}
	}

	@Environment(EnvType.SERVER)
	public static void HandleAllPackets(Packet230ModLoader packet230modloader, ServerPlayerEntity entityplayermp) {
		if (!hasInit) {
			init();
		}

		if (packet230modloader.modId == "ModLoaderMP".hashCode()) {
			switch (packet230modloader.packetType) {
				case 0:
					handleModCheckResponse(packet230modloader, entityplayermp);
					break;
				case 1:
					handleSendKey(packet230modloader, entityplayermp);
			}
		} else {
			for(int i = 0; i < ModLoader.getLoadedMods().size(); ++i) {
				BaseMod basemod = (BaseMod)ModLoader.getLoadedMods().get(i);
				if (basemod instanceof BaseModMp) {
					BaseModMp basemodmp = (BaseModMp)basemod;
					if (basemodmp.getId() == packet230modloader.modId) {
						basemodmp.HandlePacket(packet230modloader, entityplayermp);
						break;
					}
				}
			}
		}
	}

	@Environment(EnvType.SERVER)
	public static void HandleEntityTrackers(EntityTracker entitytracker, Entity entity) {
		if (!hasInit) {
			init();
		}

		for(Map.Entry entry : entityTrackerMap.entrySet()) {
			if (((Class)entry.getKey()).isInstance(entity)) {
				entitytracker.startTracking(entity, (Integer)((Pair)entry.getValue()).getLeft(), (Integer)((Pair)entry.getValue()).getRight(), true);
				return;
			}
		}
	}

	@Environment(EnvType.SERVER)
	public static EntityTrackerEntry2 HandleEntityTrackerEntries(Entity entity) {
		if (!hasInit) {
			init();
		}

		return entityTrackerEntryMap.containsKey(entity.getClass()) ? (EntityTrackerEntry2)entityTrackerEntryMap.get(entity.getClass()) : null;
	}

	@Environment(EnvType.SERVER)
	public static void SendPacketToAll(BaseModMp basemodmp, Packet230ModLoader packet230modloader) {
		if (!hasInit) {
			init();
		}

		if (basemodmp == null) {
			IllegalArgumentException illegalargumentexception = new IllegalArgumentException("baseModMp cannot be null.");
			ModLoader.getLogger().throwing("ModLoaderMP", "SendPacketToAll", illegalargumentexception);
			ModLoader.ThrowException("baseModMp cannot be null.", illegalargumentexception);
		} else {
			packet230modloader.modId = basemodmp.getId();
			sendPacketToAll(packet230modloader);
		}
	}

	@Environment(EnvType.SERVER)
	private static void sendPacketToAll(Packet packet) {
		if (packet != null) {
			for(int i = 0; i < ModLoader.getMinecraftServerInstance().playerManager.players.size(); ++i) {
				((ServerPlayerEntity)ModLoader.getMinecraftServerInstance().playerManager.players.get(i)).networkHandler.sendPacket(packet);
			}
		}
	}

	@Environment(EnvType.SERVER)
	public static void SendPacketTo(BaseModMp basemodmp, ServerPlayerEntity entityplayermp, Packet230ModLoader packet230modloader) {
		if (!hasInit) {
			init();
		}

		if (basemodmp == null) {
			IllegalArgumentException illegalargumentexception = new IllegalArgumentException("baseModMp cannot be null.");
			ModLoader.getLogger().throwing("ModLoaderMP", "SendPacketTo", illegalargumentexception);
			ModLoader.ThrowException("baseModMp cannot be null.", illegalargumentexception);
		} else {
			packet230modloader.modId = basemodmp.getId();
			sendPacketTo(entityplayermp, packet230modloader);
		}
	}

	@Environment(EnvType.SERVER)
	public static World GetPlayerWorld(PlayerEntity entityplayer) {
		ServerWorld[] aworldserver = ModLoader.getMinecraftServerInstance().worlds;

		for(int i = 0; i < aworldserver.length; ++i) {
			if (aworldserver[i].players.contains(entityplayer)) {
				return aworldserver[i];
			}
		}

		return null;
	}

	@Environment(EnvType.SERVER)
	private static void sendPacketTo(ServerPlayerEntity entityplayermp, Packet230ModLoader packet230modloader) {
		entityplayermp.networkHandler.sendPacket(packet230modloader);
	}

	@Environment(EnvType.SERVER)
	private static void sendModCheck(ServerPlayerEntity entityplayermp) {
		Packet230ModLoader packet230modloader = new Packet230ModLoader();
		packet230modloader.modId = "ModLoaderMP".hashCode();
		packet230modloader.packetType = 0;
		sendPacketTo(entityplayermp, packet230modloader);
	}

	@Environment(EnvType.SERVER)
	private static void handleModCheckResponse(Packet230ModLoader packet230modloader, ServerPlayerEntity entityplayermp) {
		StringBuilder stringbuilder = new StringBuilder();
		if (packet230modloader.dataString.length != 0) {
			for(int i = 0; i < packet230modloader.dataString.length; ++i) {
				if (packet230modloader.dataString[i].lastIndexOf("mod_") != -1) {
					if (stringbuilder.length() != 0) {
						stringbuilder.append(", ");
					}

					stringbuilder.append(packet230modloader.dataString[i].substring(packet230modloader.dataString[i].lastIndexOf("mod_")));
				}
			}
		} else {
			stringbuilder.append("no mods");
		}

		Log(entityplayermp.name + " joined with " + stringbuilder.toString());
		ArrayList arraylist = new ArrayList();

		for(int j = 0; j < bannedMods.size(); ++j) {
			for(int k = 0; k < packet230modloader.dataString.length; ++k) {
				if (packet230modloader.dataString[k].lastIndexOf("mod_") != -1 && packet230modloader.dataString[k].substring(packet230modloader.dataString[k].lastIndexOf("mod_")).startsWith((String)bannedMods.get(j))) {
					arraylist.add(packet230modloader.dataString[k]);
				}
			}
		}

		ArrayList arraylist1 = new ArrayList();

		for(int l = 0; l < ModLoader.getLoadedMods().size(); ++l) {
			BaseModMp basemodmp = (BaseModMp)ModLoader.getLoadedMods().get(l);
			if (basemodmp.hasClientSide() && basemodmp.toString().lastIndexOf("mod_") != -1) {
				String s = basemodmp.toString().substring(basemodmp.toString().lastIndexOf("mod_"));
				boolean flag = false;

				for(int l1 = 0; l1 < packet230modloader.dataString.length; ++l1) {
					if (packet230modloader.dataString[l1].lastIndexOf("mod_") != -1) {
						String s1 = packet230modloader.dataString[l1].substring(packet230modloader.dataString[l1].lastIndexOf("mod_"));
						if (s.equals(s1)) {
							flag = true;
							break;
						}
					}
				}

				if (!flag) {
					arraylist1.add(s);
				}
			}
		}

		if (!arraylist.isEmpty()) {
			StringBuilder stringbuilder1 = new StringBuilder();

			for(int i1 = 0; i1 < arraylist.size(); ++i1) {
				if (((String)arraylist.get(i1)).lastIndexOf("mod_") != -1) {
					if (stringbuilder1.length() != 0) {
						stringbuilder1.append(", ");
					}

					stringbuilder1.append(((String)arraylist.get(i1)).substring(((String)arraylist.get(i1)).lastIndexOf("mod_")));
				}
			}

			Log(entityplayermp.name + " kicked for having " + stringbuilder1.toString());
			StringBuilder stringbuilder3 = new StringBuilder();

			for(int k1 = 0; k1 < arraylist.size(); ++k1) {
				if (((String)arraylist.get(k1)).lastIndexOf("mod_") != -1) {
					stringbuilder3.append("\n");
					stringbuilder3.append(((String)arraylist.get(k1)).substring(((String)arraylist.get(k1)).lastIndexOf("mod_")));
				}
			}

			entityplayermp.networkHandler.disconnect("The following mods are banned on this server:" + stringbuilder3.toString());
		} else if (!arraylist1.isEmpty()) {
			StringBuilder stringbuilder2 = new StringBuilder();

			for(int j1 = 0; j1 < arraylist1.size(); ++j1) {
				if (((String)arraylist1.get(j1)).lastIndexOf("mod_") != -1) {
					stringbuilder2.append("\n");
					stringbuilder2.append(((String)arraylist1.get(j1)).substring(((String)arraylist1.get(j1)).lastIndexOf("mod_")));
				}
			}

			entityplayermp.networkHandler.disconnect("You are missing the following mods:" + stringbuilder2.toString());
		}

	}

	@Environment(EnvType.SERVER)
	private static void handleSendKey(Packet230ModLoader packet230modloader, ServerPlayerEntity entityplayermp) {
		if (packet230modloader.dataInt.length != 2) {
			System.out.println("SendKey packet received with missing data.");
		} else {
			int i = packet230modloader.dataInt[0];
			int j = packet230modloader.dataInt[1];

			for(int k = 0; k < ModLoader.getLoadedMods().size(); ++k) {
				BaseMod basemod = (BaseMod)ModLoader.getLoadedMods().get(k);
				if (basemod instanceof BaseModMp) {
					BaseModMp basemodmp = (BaseModMp)basemod;
					if (basemodmp.getId() == i) {
						basemodmp.HandleSendKey(entityplayermp, j);
						break;
					}
				}
			}
		}

	}

	@Environment(EnvType.SERVER)
	public static void getCommandInfo(CommandOutput icommandlistener) {
		for(int i = 0; i < ModLoader.getLoadedMods().size(); ++i) {
			BaseMod basemod = (BaseMod)ModLoader.getLoadedMods().get(i);
			if (basemod instanceof BaseModMp) {
				BaseModMp basemodmp = (BaseModMp)basemod;
				basemodmp.GetCommandInfo(icommandlistener);
			}
		}

	}

	@Environment(EnvType.SERVER)
	public static boolean handleCommand(String s, String s1, CommandOutput icommandlistener, ServerCommandHandler consolecommandhandler) {
		boolean flag = false;

		for(int i = 0; i < ModLoader.getLoadedMods().size(); ++i) {
			BaseMod basemod = (BaseMod)ModLoader.getLoadedMods().get(i);
			if (basemod instanceof BaseModMp) {
				BaseModMp basemodmp = (BaseModMp)basemod;
				if (basemodmp.HandleCommand(s, s1, icommandlistener, consolecommandhandler)) {
					flag = true;
				}
			}
		}

		return flag;
	}

	@Environment(EnvType.SERVER)
	public static void sendChatToAll(String s, String s1) {
		String s2 = s + ": " + s1;
		sendChatToAll(s2);
	}

	@Environment(EnvType.SERVER)
	public static void sendChatToAll(String s) {
		List list = ModLoader.getMinecraftServerInstance().playerManager.players;

		for(int i = 0; i < list.size(); ++i) {
			ServerPlayerEntity entityplayermp = (ServerPlayerEntity)list.get(i);
			entityplayermp.networkHandler.sendPacket(new ChatMessagePacket(s));
		}

		MinecraftServer.LOGGER.info(s);
	}

	@Environment(EnvType.SERVER)
	public static void sendChatToOps(String s, String s1) {
		String s2 = "§7(" + s + ": " + s1 + ")";
		sendChatToOps(s2);
	}

	@Environment(EnvType.SERVER)
	public static void sendChatToOps(String s) {
		List list = ModLoader.getMinecraftServerInstance().playerManager.players;

		for(int i = 0; i < list.size(); ++i) {
			ServerPlayerEntity entityplayermp = (ServerPlayerEntity)list.get(i);
			if (ModLoader.getMinecraftServerInstance().playerManager.isOperator(entityplayermp.name)) {
				entityplayermp.networkHandler.sendPacket(new ChatMessagePacket(s));
			}
		}

		MinecraftServer.LOGGER.info(s);
	}

	@Environment(EnvType.SERVER)
	public static Packet GetTileEntityPacket(BaseModMp basemodmp, int i, int j, int k, int l, int[] ai, float[] af, String[] as) {
		Packet230ModLoader packet230modloader = new Packet230ModLoader();
		packet230modloader.modId = "ModLoaderMP".hashCode();
		packet230modloader.packetType = 1;
		packet230modloader.worldPacket = true;
		int i1 = ai != null ? ai.length : 0;
		int[] ai1 = new int[i1 + 5];
		ai1[0] = basemodmp.getId();
		ai1[1] = i;
		ai1[2] = j;
		ai1[3] = k;
		ai1[4] = l;
		if (i1 != 0) {
			System.arraycopy(ai, 0, ai1, 5, ai.length);
		}

		packet230modloader.dataInt = ai1;
		packet230modloader.dataFloat = af;
		packet230modloader.dataString = as;
		return packet230modloader;
	}

	@Environment(EnvType.SERVER)
	public static void SendTileEntityPacket(BlockEntity tileentity) {
		sendPacketToAll(tileentity.createUpdatePacket());
	}

	private ModLoaderMp() {
	}
}
