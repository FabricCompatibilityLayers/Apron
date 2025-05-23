package modloadermp;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import modloader.BaseMod;
import modloader.ModLoader;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.class_39;
import net.minecraft.class_426;
import net.minecraft.class_454;
import net.minecraft.class_488;
import org.jetbrains.annotations.NotNull;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.play.ChatMessagePacket;
import net.minecraft.network.packet.s2c.play.OpenScreenS2CPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

import io.github.betterthanupdates.Legacy;
import io.github.betterthanupdates.apron.api.ApronApi;

@SuppressWarnings("unused")
@Legacy
public class ModLoaderMp {
	// Apron
	private static final ApronApi APRON = ApronApi.getInstance();
	public static final String NAME = "ModLoaderMP";
	public static final String VERSION = APRON.getModLoaderMPVersion();
	private static boolean hasInit = false;
	@Environment(EnvType.CLIENT)
	private static boolean packet230Received;
	@Environment(EnvType.CLIENT)
	private static Map<Integer, NetClientHandlerEntity> NET_CLIENT_HANDLER_MAP;
	@Environment(EnvType.CLIENT)
	private static Map<Integer, BaseModMp> GUI_MOD_MAP;
	@Environment(EnvType.SERVER)
	private static Map<Class<? extends Entity>, AbstractMap.SimpleEntry<Integer, Integer>> entityTrackerMap;
	@Environment(EnvType.SERVER)
	private static Map<Class<? extends Entity>, EntityTrackerEntry> entityTrackerEntryMap;
	@Environment(EnvType.SERVER)
	private static List<String> bannedMods;

	static {
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
			packet230Received = false;
			NET_CLIENT_HANDLER_MAP = new HashMap<>();
			GUI_MOD_MAP = new HashMap<>();
		} else {
			entityTrackerMap = new HashMap<>();
			entityTrackerEntryMap = new HashMap<>();
			bannedMods = new ArrayList<>();
		}
	}

	@Environment(EnvType.CLIENT)
	public static void Init() {
		init();
	}

	@Environment(EnvType.SERVER)
	public static void InitModLoaderMp() {
		init();
	}

	@Environment(EnvType.CLIENT)
	public static void HandleAllPackets(final ModLoaderPacket packet) {
		init();

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
					class_454 world = (class_454) APRON.getWorld();

					if (world != null) {
						world.method_1495(entity.id, entity);
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
		init();

		if (NET_CLIENT_HANDLER_MAP.containsKey(aInteger1)) {
			return NET_CLIENT_HANDLER_MAP.get(aInteger1);
		}

		return null;
	}

	@Environment(EnvType.CLIENT)
	public static void SendPacket(BaseModMp basemodmp, ModLoaderPacket packet) {
		init();

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
		init();

		if (GUI_MOD_MAP.containsKey(i)) {
			Log("RegisterGUI error: inventoryType already registered.");
		} else {
			GUI_MOD_MAP.put(i, basemodmp);
		}
	}

	@Environment(EnvType.CLIENT)
	public static void HandleGUI(final OpenScreenS2CPacket packet) {
		init();

		final BaseModMp basemodmp = GUI_MOD_MAP.get(packet.screenHandlerId);
		final Screen guiScreen = basemodmp.HandleGUI(packet.screenHandlerId);

		if (guiScreen != null) {
			PlayerEntity player = APRON.getPlayer();

			if (player != null) {
				ModLoader.OpenGUI(player, guiScreen);

				ScreenHandler container = player.container;
				if (container != null) container.syncId = packet.syncId;
			}
		}
	}

	@Environment(EnvType.CLIENT)
	public static void RegisterNetClientHandlerEntity(Class<? extends Entity> class1, int i) {
		RegisterNetClientHandlerEntity(class1, false, i);
	}

	@Environment(EnvType.CLIENT)
	public static void RegisterNetClientHandlerEntity(Class<? extends Entity> class1, boolean flag, int i) {
		init();

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
		init();

		if (basemodmp == null) {
			final IllegalArgumentException e = new IllegalArgumentException("baseModMp cannot be null.");
			ModLoader.getLogger().throwing(NAME, "SendKey", e);
			ModLoader.ThrowException("baseModMp cannot be null.", e);
		} else {
			final ModLoaderPacket modloaderPacket = new ModLoaderPacket();
			modloaderPacket.modId = NAME.hashCode();
			modloaderPacket.packetType = 1;
			modloaderPacket.dataInt = new int[] {basemodmp.getId(), i};
			sendPacket(modloaderPacket);
		}
	}

	public static void Log(final String message) {
		ModLoader.LOGGER.debug(message);
	}

	private static void init() {
		if (hasInit) return;

		Packet.register(230, true, true, ModLoaderPacket.class);

		if (!APRON.isClient()) {
			try {
				MinecraftServer server = ((MinecraftServer) APRON.getGame());

				if (server == null) return;

				File file = server.method_2160("banned-mods.txt");

				if (!file.exists()) {
					file.createNewFile();
				}

				if (!file.exists()) {
					Log("Could not get or create banned mods config file.");
				}

				BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(Files.newInputStream(file.toPath())));

				String s;

				while ((s = bufferedreader.readLine()) != null) {
					bannedMods.add(s);
				}
			} catch (IOException e) {
				ModLoader.getLogger().throwing("ModLoader", "init", e);
				ModLoader.ThrowException("ModLoaderMultiplayer", e);
				return;
			}
		}

		hasInit = true;
		Log(NAME + " " + VERSION + " Initialized");
	}

	@Environment(EnvType.CLIENT)
	private static void handleModCheck(ModLoaderPacket originalPacket) {
		ModLoaderPacket newPacket = new ModLoaderPacket();
		newPacket.modId = "ModLoaderMP".hashCode();
		newPacket.packetType = 0;
		newPacket.dataString = new String[ModLoader.getLoadedMods().size()];

		for (int i = 0; i < ModLoader.getLoadedMods().size(); ++i) {
			newPacket.dataString[i] = ModLoader.getLoadedMods().get(i).toString();
		}

		sendPacket(newPacket);
	}

	@Environment(EnvType.CLIENT)
	private static void handleTileEntityPacket(ModLoaderPacket packet) {
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
	private static void sendPacket(ModLoaderPacket packet) {
		Minecraft client = (Minecraft) APRON.getGame();

		if (client == null) return;

		if (packet230Received && client.world != null && client.world.isRemote) {
			client.getNetworkHandler().sendPacket(packet);
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
		init();

		if (entityTrackerMap.containsKey(class1)) {
			System.out.println("RegisterEntityTracker error: entityClass already registered.");
		} else {
			entityTrackerMap.put(class1, new AbstractMap.SimpleEntry<>(i, j));
		}
	}

	@Environment(EnvType.SERVER)
	public static void RegisterEntityTrackerEntry(Class<? extends Entity> class1, int i) {
		RegisterEntityTrackerEntry(class1, false, i);
	}

	@Environment(EnvType.SERVER)
	public static void RegisterEntityTrackerEntry(Class<? extends Entity> class1, boolean flag, int i) {
		init();

		if (i > 255) {
			System.out.println("RegisterEntityTrackerEntry error: entityId cannot be greater than 255.");
		}

		if (entityTrackerEntryMap.containsKey(class1)) {
			System.out.println("RegisterEntityTrackerEntry error: entityClass already registered.");
		} else {
			entityTrackerEntryMap.put(class1, new EntityTrackerEntry(i, flag));
		}
	}

	@Environment(EnvType.SERVER)
	public static void HandleAllLogins(ServerPlayerEntity entityplayermp) {
		init();

		sendModCheck(entityplayermp);

		for (int i = 0; i < ModLoader.getLoadedMods().size(); ++i) {
			BaseMod basemod = ModLoader.getLoadedMods().get(i);

			if (basemod instanceof BaseModMp) {
				((BaseModMp) basemod).HandleLogin(entityplayermp);
			}
		}
	}

	@Environment(EnvType.SERVER)
	public static void HandleAllPackets(ModLoaderPacket modloaderPacket, ServerPlayerEntity entityplayermp) {
		init();

		if (modloaderPacket.modId == "ModLoaderMP".hashCode()) {
			switch (modloaderPacket.packetType) {
				case 0:
					handleModCheckResponse(modloaderPacket, entityplayermp);
					break;
				case 1:
					handleSendKey(modloaderPacket, entityplayermp);
			}
		} else {
			for (int i = 0; i < ModLoader.getLoadedMods().size(); ++i) {
				BaseMod basemod = ModLoader.getLoadedMods().get(i);

				if (basemod instanceof BaseModMp) {
					BaseModMp basemodmp = (BaseModMp) basemod;

					if (basemodmp.getId() == modloaderPacket.modId) {
						basemodmp.HandlePacket(modloaderPacket, entityplayermp);
						break;
					}
				}
			}
		}
	}

	@Environment(EnvType.SERVER)
	public static void HandleEntityTrackers(class_488 entitytracker, Entity entity) {
		init();

		for (Map.Entry<Class<? extends Entity>, AbstractMap.SimpleEntry<Integer, Integer>> entry : entityTrackerMap.entrySet()) {
			if (entry.getKey().isInstance(entity)) {
				entitytracker.method_1667(entity, entry.getValue().getKey(), entry.getValue().getValue(), true);
				return;
			}
		}
	}

	@Environment(EnvType.SERVER)
	public static EntityTrackerEntry HandleEntityTrackerEntries(Entity entity) {
		init();

		return entityTrackerEntryMap.getOrDefault(entity.getClass(), null);
	}

	@Environment(EnvType.SERVER)
	public static void SendPacketToAll(BaseModMp basemodmp, ModLoaderPacket modloaderPacket) {
		init();

		if (basemodmp == null) {
			IllegalArgumentException illegalargumentexception = new IllegalArgumentException("baseModMp cannot be null.");
			ModLoader.getLogger().throwing("ModLoaderMP", "SendPacketToAll", illegalargumentexception);
			ModLoader.ThrowException("baseModMp cannot be null.", illegalargumentexception);
		} else {
			modloaderPacket.modId = basemodmp.getId();
			sendPacketToAll(modloaderPacket);
		}
	}

	@Environment(EnvType.SERVER)
	private static void sendPacketToAll(Packet packet) {
		MinecraftServer server = (MinecraftServer) APRON.getGame();

		if (server != null && packet != null) {
			server.field_2842.method_559(packet);
		}
	}

	@Environment(EnvType.SERVER)
	public static void SendPacketTo(BaseModMp mod, ServerPlayerEntity player, ModLoaderPacket packet) {
		init();

		if (mod == null) {
			IllegalArgumentException e = new IllegalArgumentException("mod cannot be null.");
			ModLoader.getLogger().throwing("ModLoaderMP", "SendPacketTo", e);
			ModLoader.ThrowException("mod cannot be null.", e);
		} else {
			packet.modId = mod.getId();
			sendPacketTo(player, packet);
		}
	}

	/**
	 * @param player The player to find the world of
	 * @return the world that the player is currently in.
	 */
	@Environment(EnvType.SERVER)
	public static World GetPlayerWorld(@NotNull PlayerEntity player) {
		return player.world; // used to iterate over all worlds, then iterate over all players in each world to check
	}

	@Environment(EnvType.SERVER)
	private static void sendPacketTo(ServerPlayerEntity player, ModLoaderPacket packet) {
		player.field_255.method_835(packet);
	}

	@Environment(EnvType.SERVER)
	private static void sendModCheck(ServerPlayerEntity player) {
		ModLoaderPacket packet = new ModLoaderPacket();
		packet.modId = "ModLoaderMP".hashCode();
		packet.packetType = 0;
		sendPacketTo(player, packet);
	}

	@Environment(EnvType.SERVER)
	private static void handleModCheckResponse(ModLoaderPacket modloaderPacket, ServerPlayerEntity entityplayermp) {
		StringBuilder stringbuilder = new StringBuilder();

		if (modloaderPacket.dataString.length != 0) {
			for (int i = 0; i < modloaderPacket.dataString.length; ++i) {
				if (modloaderPacket.dataString[i].lastIndexOf("mod_") != -1) {
					if (stringbuilder.length() != 0) {
						stringbuilder.append(", ");
					}

					stringbuilder.append(modloaderPacket.dataString[i].substring(modloaderPacket.dataString[i].lastIndexOf("mod_")));
				}
			}
		} else {
			stringbuilder.append("no mods");
		}

		Log(entityplayermp.name + " joined with " + stringbuilder);
		ArrayList<String> arraylist = new ArrayList<>();

		for (String bannedMod : bannedMods) {
			for (int k = 0; k < modloaderPacket.dataString.length; ++k) {
				if (modloaderPacket.dataString[k].lastIndexOf("mod_") != -1
						&& modloaderPacket.dataString[k].substring(modloaderPacket.dataString[k].lastIndexOf("mod_")).startsWith(bannedMod)) {
					arraylist.add(modloaderPacket.dataString[k]);
				}
			}
		}

		ArrayList<String> arraylist1 = new ArrayList<>();

		for (int l = 0; l < ModLoader.getLoadedMods().size(); ++l) {
			BaseModMp basemodmp = (BaseModMp) ModLoader.getLoadedMods().get(l);

			if (basemodmp.hasClientSide() && basemodmp.toString().lastIndexOf("mod_") != -1) {
				String s = basemodmp.toString().substring(basemodmp.toString().lastIndexOf("mod_"));
				boolean flag = false;

				for (int l1 = 0; l1 < modloaderPacket.dataString.length; ++l1) {
					if (modloaderPacket.dataString[l1].lastIndexOf("mod_") != -1) {
						String s1 = modloaderPacket.dataString[l1].substring(modloaderPacket.dataString[l1].lastIndexOf("mod_"));

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

			entityplayermp.field_255.method_833("The following mods are banned on this server:" + stringbuilder3);
		} else if (arraylist1.size() != 0) {
			StringBuilder stringbuilder2 = new StringBuilder();

			for (String s : arraylist1) {
				if (s.lastIndexOf("mod_") != -1) {
					stringbuilder2.append("\n");
					stringbuilder2.append(s.substring(s.lastIndexOf("mod_")));
				}
			}

			entityplayermp.field_255.method_833("You are missing the following mods:" + stringbuilder2);
		}
	}

	@Environment(EnvType.SERVER)
	private static void handleSendKey(ModLoaderPacket modloaderPacket, ServerPlayerEntity entityplayermp) {
		if (modloaderPacket.dataInt.length != 2) {
			System.out.println("SendKey packet received with missing data.");
		} else {
			int i = modloaderPacket.dataInt[0];
			int j = modloaderPacket.dataInt[1];

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
	public static void getCommandInfo(class_39 icommandlistener) {
		for (int i = 0; i < ModLoader.getLoadedMods().size(); ++i) {
			BaseMod basemod = ModLoader.getLoadedMods().get(i);

			if (basemod instanceof BaseModMp) {
				BaseModMp basemodmp = (BaseModMp) basemod;
				basemodmp.GetCommandInfo(icommandlistener);
			}
		}
	}

	@Environment(EnvType.SERVER)
	public static boolean handleCommand(String s, String s1, class_39 icommandlistener, class_426 consolecommandhandler) {
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
	public static void sendChatToAll(String encodedMessage) {
		MinecraftServer server = (MinecraftServer) APRON.getGame();

		if (server != null) {
			server.field_2842.method_559(new ChatMessagePacket(encodedMessage));
			MinecraftServer.field_2837.info(encodedMessage);
		}
	}

	@Environment(EnvType.SERVER)
	public static void sendChatToOps(String s, String s1) {
		String s2 = "§7(" + s + ": " + s1 + ")";
		sendChatToOps(s2);
	}

	@Environment(EnvType.SERVER)
	public static void sendChatToOps(String encodedMessage) {
		MinecraftServer server = (MinecraftServer) APRON.getGame();

		if (server == null) return;

		for (Object object : server.field_2842.field_578) {
			if (object instanceof ServerPlayerEntity) {
				ServerPlayerEntity player = (ServerPlayerEntity) object;

				if (server.field_2842.method_584(player.name)) {
					player.field_255.method_835(new ChatMessagePacket(encodedMessage));
					MinecraftServer.field_2837.info(encodedMessage);
				}
			}
		}
	}

	@Environment(EnvType.SERVER)
	public static Packet GetTileEntityPacket(BaseModMp basemodmp, int i, int j, int k, int l, int[] ai, float[] af, String[] as) {
		ModLoaderPacket modloaderPacket = new ModLoaderPacket();
		modloaderPacket.modId = "ModLoaderMP".hashCode();
		modloaderPacket.packetType = 1;
		modloaderPacket.worldPacket = true;
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

		modloaderPacket.dataInt = ai1;
		modloaderPacket.dataFloat = af;
		modloaderPacket.dataString = as;
		return modloaderPacket;
	}

	@Environment(EnvType.SERVER)
	public static void SendTileEntityPacket(BlockEntity blockEntity) {
		sendPacketToAll(blockEntity.createUpdatePacket());
	}
}
