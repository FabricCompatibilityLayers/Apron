package modloadermp;

import io.github.fabriccompatibilitylayers.modloader.mixin.client.modloadermp.PacketAccessor;
import modloader.BaseMod;
import modloader.ModLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.s2c.play.OpenScreenS2CPacket;
import net.minecraft.world.ClientWorld;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class ModLoaderMp {
	public static final String NAME = "ModLoaderMP";
	public static final String VERSION = "Beta 1.7.3 unofficial";
	private static boolean hasInit = false;
	private static boolean packet230Received = false;
	private static Map netClientHandlerEntityMap = new HashMap();
	private static Map guiModMap = new HashMap();

	public static void Init() {
		if (!hasInit) {
			init();
		}

	}

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

	public static NetClientHandlerEntity HandleNetClientHandlerEntities(int i) {
		if (!hasInit) {
			init();
		}

		return netClientHandlerEntityMap.containsKey(i) ? (NetClientHandlerEntity)netClientHandlerEntityMap.get(i) : null;
	}

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

	public static void RegisterNetClientHandlerEntity(Class class1, int i) {
		RegisterNetClientHandlerEntity(class1, false, i);
	}

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

		Log("ModLoaderMP Beta 1.7.3 unofficial Initialized");
	}

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

	private static void sendPacket(Packet230ModLoader packet230modloader) {
		if (packet230Received && ModLoader.getMinecraftInstance().world != null && ModLoader.getMinecraftInstance().world.isRemote) {
			ModLoader.getMinecraftInstance().getNetworkHandler().sendPacket(packet230modloader);
		}

	}

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

	private ModLoaderMp() {
	}
}
