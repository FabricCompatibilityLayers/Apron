package modloadermp;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import modloader.BaseMod;
import modloader.ModLoader;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.container.Container;
import net.minecraft.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.packet.AbstractPacket;
import net.minecraft.packet.play.ChatMessagePacket;
import net.minecraft.packet.play.OpenContainerS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.CommandSource;
import net.minecraft.server.entity.player.ServerPlayerEntity;
import net.minecraft.server.network.ServerEntityTracker;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import io.github.betterthanupdates.apron.api.ApronApi;

@SuppressWarnings("unused")
public class ModLoaderMp {
	// Apron
	private static final ApronApi APRON = ApronApi.getInstance();

	public static final String NAME = "ModLoaderMP";
	public static final String VERSION = APRON.getModLoaderMPVersion();
	private static boolean hasInit = false;
	@Environment(EnvType.CLIENT)
	private static boolean packet230Received = false;
	@Environment(EnvType.CLIENT)
	private static final Map<Integer, NetClientHandlerEntity> NET_CLIENT_HANDLER_MAP = new HashMap<>();
	@Environment(EnvType.CLIENT)
	private static final Map<Integer, BaseModMp> GUI_MOD_MAP = new HashMap<>();
	@Environment(EnvType.SERVER)
	private static final Map<Class<? extends Entity>, Pair<Integer, Integer>> entityTrackerMap = new HashMap<>();
	@Environment(EnvType.SERVER)
	private static final Map<Class<? extends Entity>, EntityTrackerEntry2> entityTrackerEntryMap = new HashMap<>();
	@Environment(EnvType.SERVER)
	private static final List<String> bannedMods = new ArrayList<>();

	@Environment(EnvType.CLIENT)
	public static void Init() {
		if (!hasInit) {
			init();
		}
	}

	@Environment(EnvType.SERVER)
	public static void InitModLoaderMp() {
		if (!hasInit) {
			init();
		}
	}

	@Environment(EnvType.CLIENT)
	public static void HandleAllPackets(final Packet230ModLoader packet) {
		if (!hasInit) {
			init();
		}

		packet230Received = true;

		if (packet.modId == NAME.hashCode()) {
			switch (packet.packetType) {
				case 0: {
					handleModCheck(packet);
					break;
				}
				case 1: {
					handleTileEntityPacket(packet);
					break;
				}
			}
		} else if (packet.modId == "Spawn".hashCode()) {
			NetClientHandlerEntity netclienthandlerentity = HandleNetClientHandlerEntities(packet.packetType);

			if (netclienthandlerentity != null && ISpawnable.class.isAssignableFrom(netclienthandlerentity.entityClass)) {
				try {
					Entity entity = netclienthandlerentity.entityClass.getConstructor(World.class).newInstance(APRON.getWorld());
					((ISpawnable) entity).spawn(packet);
					ClientWorld world = (ClientWorld) APRON.getWorld();

					if (world != null) {
						world.method_1495(entity.entityId, entity);
					}
				} catch (Exception e) {
					ModLoader.getLogger().throwing("ModLoader", "handleCustomSpawn", e);
					ModLoader.ThrowException(String.format("Error initializing entity of type %s.", packet.packetType), e);
				}
			}
		} else {
			for (int i = 0; i < ModLoader.getLoadedMods().size(); ++i) {
				BaseMod basemod = ModLoader.getLoadedMods().get(i);

				if (basemod instanceof BaseModMp) {
					BaseModMp basemodmp = (BaseModMp) basemod;

					if (basemodmp.getId() == packet.modId) {
						basemodmp.HandlePacket(packet);
						break;
					}
				}
			}
		}
	}

	@Environment(EnvType.CLIENT)
	public static NetClientHandlerEntity HandleNetClientHandlerEntities(final int aInteger1) {
		if (!hasInit) {
			init();
		}

		if (NET_CLIENT_HANDLER_MAP.containsKey(aInteger1)) {
			return NET_CLIENT_HANDLER_MAP.get(aInteger1);
		}

		return null;
	}

	@Environment(EnvType.CLIENT)
	public static void SendPacket(BaseModMp basemodmp, Packet230ModLoader packet) {
		if (!hasInit) {
			init();
		}

		if (basemodmp == null) {
			IllegalArgumentException e = new IllegalArgumentException("baseModMp cannot be null.");
			ModLoader.getLogger().throwing("ModLoaderMp", "SendPacket", e);
			ModLoader.ThrowException("baseModMp cannot be null.", e);
		} else {
			packet.modId = basemodmp.getId();
			sendPacket(packet);
		}
	}

	@Environment(EnvType.CLIENT)
	public static void RegisterGUI(final BaseModMp basemodmp, final int i) {
		if (!hasInit) {
			init();
		}

		if (GUI_MOD_MAP.containsKey(i)) {
			Log("RegisterGUI error: inventoryType already registered.");
		} else {
			GUI_MOD_MAP.put(i, basemodmp);
		}
	}

	@Environment(EnvType.CLIENT)
	public static void HandleGUI(final OpenContainerS2CPacket packet) {
		if (!hasInit) {
			init();
		}

		final BaseModMp basemodmp = GUI_MOD_MAP.get(packet.inventoryType);
		final Screen guiScreen = basemodmp.HandleGUI(packet.inventoryType);

		if (guiScreen != null) {
			PlayerEntity player = APRON.getPlayer();

			if (player != null) {
				ModLoader.OpenGUI(player, guiScreen);

				Container container = player.container;
				if (container != null) container.currentContainerId = packet.containerId;
			}
		}
	}

	@Environment(EnvType.CLIENT)
	public static void RegisterNetClientHandlerEntity(Class<? extends Entity> class1, int i) {
		RegisterNetClientHandlerEntity(class1, false, i);
	}

	@Environment(EnvType.CLIENT)
	public static void RegisterNetClientHandlerEntity(Class<? extends Entity> class1, boolean flag, int i) {
		if (!hasInit) {
			init();
		}

		if (i > 255) {
			Log("RegisterNetClientHandlerEntity error: entityId cannot be greater than 255.");
		} else if (NET_CLIENT_HANDLER_MAP.containsKey(i)) {
			Log("RegisterNetClientHandlerEntity error: entityId already registered.");
		} else {
			if (i > 127) {
				i -= 256;
			}

			NET_CLIENT_HANDLER_MAP.put(i, new NetClientHandlerEntity(class1, flag));
		}
	}

	@Environment(EnvType.CLIENT)
	public static void SendKey(final BaseModMp basemodmp, final int i) {
		if (!hasInit) {
			init();
		}

		if (basemodmp == null) {
			final IllegalArgumentException e = new IllegalArgumentException("baseModMp cannot be null.");
			ModLoader.getLogger().throwing(NAME, "SendKey", e);
			ModLoader.ThrowException("baseModMp cannot be null.", e);
		} else {
			final Packet230ModLoader packet230modloader = new Packet230ModLoader();
			packet230modloader.modId = NAME.hashCode();
			packet230modloader.packetType = 1;
			packet230modloader.dataInt = new int[] {basemodmp.getId(), i};
			sendPacket(packet230modloader);
		}
	}

	public static void Log(final String s) {
		ModLoader.getLogger().fine(s);
	}

	private static void init() {
		hasInit = true;
		AbstractPacket.register(230, true, true, Packet230ModLoader.class);

		if (!APRON.isClient()) {
			try {
				File file = ModLoader.getMinecraftServerInstance().getFile("banned-mods.txt");

				if (!file.exists()) {
					file.createNewFile();
				}

				BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(Files.newInputStream(file.toPath())));

				String s;

				while ((s = bufferedreader.readLine()) != null) {
					bannedMods.add(s);
				}
			} catch (IOException var9) {
				ModLoader.getLogger().throwing("ModLoader", "init", var9);
				ModLoader.ThrowException("ModLoaderMultiplayer", var9);
				return;
			}
		}

		Log(NAME + " " + VERSION + " Initialized");
	}

	@Environment(EnvType.CLIENT)
	private static void handleModCheck(Packet230ModLoader originalPacket) {
		Packet230ModLoader newPacket = new Packet230ModLoader();
		newPacket.modId = "ModLoaderMP".hashCode();
		newPacket.packetType = 0;
		newPacket.dataString = new String[ModLoader.getLoadedMods().size()];

		for (int i = 0; i < ModLoader.getLoadedMods().size(); ++i) {
			newPacket.dataString[i] = ModLoader.getLoadedMods().get(i).toString();
		}

		sendPacket(newPacket);
	}

	@Environment(EnvType.CLIENT)
	private static void handleTileEntityPacket(Packet230ModLoader packet) {
		if (packet.dataInt != null && packet.dataInt.length >= 5) {
			int i = packet.dataInt[0];
			int j = packet.dataInt[1];
			int k = packet.dataInt[2];
			int l = packet.dataInt[3];
			int i1 = packet.dataInt[4];
			int[] ai = new int[packet.dataInt.length - 5];
			System.arraycopy(packet.dataInt, 5, ai, 0, packet.dataInt.length - 5);
			float[] af = packet.dataFloat;
			String[] as = packet.dataString;

			for (int j1 = 0; j1 < ModLoader.getLoadedMods().size(); ++j1) {
				BaseMod basemod = ModLoader.getLoadedMods().get(j1);

				if (basemod instanceof BaseModMp) {
					BaseModMp basemodmp = (BaseModMp) basemod;

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
	private static void sendPacket(Packet230ModLoader packet) {
		if (!packet230Received) return;

		World world = APRON.getWorld();

		if (world != null && world.isClient) {
			((Minecraft) Objects.requireNonNull(APRON.getGame())).getPacketHandler().sendPacket(packet);
		} else {
			((MinecraftServer) Objects.requireNonNull(APRON.getGame())).serverPlayerConnectionManager.sendToAll(packet);
		}
	}

	public static BaseModMp GetModInstance(final Class<? extends BaseModMp> v1) {
		for (BaseMod basemod : ModLoader.getLoadedMods()) {
			if (basemod instanceof BaseModMp) {
				final BaseModMp basemodmp = (BaseModMp) basemod;

				if (v1.isInstance(basemodmp)) {
					return basemodmp;
				}
			}
		}

		return null;
	}

	public ModLoaderMp() {
	}

	@Environment(EnvType.SERVER)
	public static void RegisterEntityTracker(Class<? extends Entity> class1, int i, int j) {
		if (!hasInit) {
			init();
		}

		if (entityTrackerMap.containsKey(class1)) {
			System.out.println("RegisterEntityTracker error: entityClass already registered.");
		} else {
			entityTrackerMap.put(class1, new Pair<>(i, j));
		}
	}

	@Environment(EnvType.SERVER)
	public static void RegisterEntityTrackerEntry(Class<? extends Entity> class1, int i) {
		RegisterEntityTrackerEntry(class1, false, i);
	}

	@Environment(EnvType.SERVER)
	public static void RegisterEntityTrackerEntry(Class<? extends Entity> class1, boolean flag, int i) {
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

		for (int i = 0; i < ModLoader.getLoadedMods().size(); ++i) {
			BaseMod basemod = ModLoader.getLoadedMods().get(i);

			if (basemod instanceof BaseModMp) {
				((BaseModMp) basemod).HandleLogin(entityplayermp);
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
			for (int i = 0; i < ModLoader.getLoadedMods().size(); ++i) {
				BaseMod basemod = ModLoader.getLoadedMods().get(i);

				if (basemod instanceof BaseModMp) {
					BaseModMp basemodmp = (BaseModMp) basemod;

					if (basemodmp.getId() == packet230modloader.modId) {
						basemodmp.HandlePacket(packet230modloader, entityplayermp);
						break;
					}
				}
			}
		}
	}

	@Environment(EnvType.SERVER)
	public static void HandleEntityTrackers(ServerEntityTracker entitytracker, Entity entity) {
		if (!hasInit) {
			init();
		}

		for (Map.Entry<Class<? extends Entity>, Pair<Integer, Integer>> entry : entityTrackerMap.entrySet()) {
			if (entry.getKey().isInstance(entity)) {
				entitytracker.trackEntity(entity, entry.getValue().getLeft(), entry.getValue().getRight(), true);
				return;
			}
		}
	}

	@Environment(EnvType.SERVER)
	public static EntityTrackerEntry2 HandleEntityTrackerEntries(Entity entity) {
		if (!hasInit) {
			init();
		}

		return entityTrackerEntryMap.getOrDefault(entity.getClass(), null);
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
	private static void sendPacketToAll(AbstractPacket packet) {
		if (packet != null) {
			for (int i = 0; i < ModLoader.getMinecraftServerInstance().serverPlayerConnectionManager.players.size(); ++i) {
				((ServerPlayerEntity) ModLoader.getMinecraftServerInstance().serverPlayerConnectionManager.players.get(i)).packetHandler.send(packet);
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

		for (ServerWorld serverWorld : aworldserver) {
			if (serverWorld.players.contains(entityplayer)) {
				return serverWorld;
			}
		}

		return null;
	}

	@Environment(EnvType.SERVER)
	private static void sendPacketTo(ServerPlayerEntity entityplayermp, Packet230ModLoader packet230modloader) {
		entityplayermp.packetHandler.send(packet230modloader);
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
			for (int i = 0; i < packet230modloader.dataString.length; ++i) {
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

		Log(entityplayermp.name + " joined with " + stringbuilder);
		ArrayList<String> arraylist = new ArrayList<>();

		for (String bannedMod : bannedMods) {
			for (int k = 0; k < packet230modloader.dataString.length; ++k) {
				if (packet230modloader.dataString[k].lastIndexOf("mod_") != -1
						&& packet230modloader.dataString[k].substring(packet230modloader.dataString[k].lastIndexOf("mod_")).startsWith(bannedMod)) {
					arraylist.add(packet230modloader.dataString[k]);
				}
			}
		}

		ArrayList<String> arraylist1 = new ArrayList<>();

		for (int l = 0; l < ModLoader.getLoadedMods().size(); ++l) {
			BaseModMp basemodmp = (BaseModMp) ModLoader.getLoadedMods().get(l);

			if (basemodmp.hasClientSide() && basemodmp.toString().lastIndexOf("mod_") != -1) {
				String s = basemodmp.toString().substring(basemodmp.toString().lastIndexOf("mod_"));
				boolean flag = false;

				for (int l1 = 0; l1 < packet230modloader.dataString.length; ++l1) {
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

		if (arraylist.size() != 0) {
			StringBuilder stringbuilder1 = new StringBuilder();

			for (String s : arraylist) {
				if (s.lastIndexOf("mod_") != -1) {
					if (stringbuilder1.length() != 0) {
						stringbuilder1.append(", ");
					}

					stringbuilder1.append(s.substring(s.lastIndexOf("mod_")));
				}
			}

			Log(entityplayermp.name + " kicked for having " + stringbuilder1);
			StringBuilder stringbuilder3 = new StringBuilder();

			for (String s : arraylist) {
				if (s.lastIndexOf("mod_") != -1) {
					stringbuilder3.append("\n");
					stringbuilder3.append(s.substring(s.lastIndexOf("mod_")));
				}
			}

			entityplayermp.packetHandler.kick("The following mods are banned on this server:" + stringbuilder3);
		} else if (arraylist1.size() != 0) {
			StringBuilder stringbuilder2 = new StringBuilder();

			for (String s : arraylist1) {
				if (s.lastIndexOf("mod_") != -1) {
					stringbuilder2.append("\n");
					stringbuilder2.append(s.substring(s.lastIndexOf("mod_")));
				}
			}

			entityplayermp.packetHandler.kick("You are missing the following mods:" + stringbuilder2);
		}
	}

	@Environment(EnvType.SERVER)
	private static void handleSendKey(Packet230ModLoader packet230modloader, ServerPlayerEntity entityplayermp) {
		if (packet230modloader.dataInt.length != 2) {
			System.out.println("SendKey packet received with missing data.");
		} else {
			int i = packet230modloader.dataInt[0];
			int j = packet230modloader.dataInt[1];

			for (int k = 0; k < ModLoader.getLoadedMods().size(); ++k) {
				BaseMod basemod = ModLoader.getLoadedMods().get(k);

				if (basemod instanceof BaseModMp) {
					BaseModMp basemodmp = (BaseModMp) basemod;

					if (basemodmp.getId() == i) {
						basemodmp.HandleSendKey(entityplayermp, j);
						break;
					}
				}
			}
		}
	}

	@Environment(EnvType.SERVER)
	public static void getCommandInfo(CommandSource icommandlistener) {
		for (int i = 0; i < ModLoader.getLoadedMods().size(); ++i) {
			BaseMod basemod = ModLoader.getLoadedMods().get(i);

			if (basemod instanceof BaseModMp) {
				BaseModMp basemodmp = (BaseModMp) basemod;
				basemodmp.GetCommandInfo(icommandlistener);
			}
		}
	}

	@Environment(EnvType.SERVER)
	public static boolean handleCommand(String s, String s1, CommandSource icommandlistener, CommandManager consolecommandhandler) {
		boolean flag = false;

		for (int i = 0; i < ModLoader.getLoadedMods().size(); ++i) {
			BaseMod basemod = ModLoader.getLoadedMods().get(i);

			if (basemod instanceof BaseModMp) {
				BaseModMp basemodmp = (BaseModMp) basemod;

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
		List<ServerPlayerEntity> list = ModLoader.getMinecraftServerInstance().serverPlayerConnectionManager.players;

		for (ServerPlayerEntity entityplayermp : list) {
			entityplayermp.packetHandler.send(new ChatMessagePacket(s));
		}

		MinecraftServer.logger.info(s);
	}

	@Environment(EnvType.SERVER)
	public static void sendChatToOps(String s, String s1) {
		String s2 = "§7(" + s + ": " + s1 + ")";
		sendChatToOps(s2);
	}

	@Environment(EnvType.SERVER)
	public static void sendChatToOps(String s) {
		List<ServerPlayerEntity> list = ModLoader.getMinecraftServerInstance().serverPlayerConnectionManager.players;

		for (ServerPlayerEntity entityplayermp : list) {
			if (ModLoader.getMinecraftServerInstance().serverPlayerConnectionManager.isOp(entityplayermp.name)) {
				entityplayermp.packetHandler.send(new ChatMessagePacket(s));
			}
		}

		MinecraftServer.logger.info(s);
	}

	@Environment(EnvType.SERVER)
	public static AbstractPacket GetTileEntityPacket(BaseModMp basemodmp, int i, int j, int k, int l, int[] ai, float[] af, String[] as) {
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
		sendPacketToAll(tileentity.getPacketContents());
	}
}
