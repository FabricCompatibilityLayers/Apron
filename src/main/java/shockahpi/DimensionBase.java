package shockahpi;

import java.util.ArrayList;
import java.util.LinkedList;

import io.github.betterthanupdates.Legacy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MovementManager;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.gui.screen.ingame.DeathScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.NetherTeleporter;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkCache;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.NetherDimension;
import net.minecraft.world.dimension.OverworldDimension;
import net.minecraft.world.dimension.SkyDimension;
import net.minecraft.world.source.WorldSource;

@Legacy
public class DimensionBase {
	@Legacy
	public static ArrayList<DimensionBase> list = new ArrayList<>();
	@Legacy
	public static LinkedList<Integer> order = new LinkedList<>();
	@Legacy
	public final int number;
	@Legacy
	public final Class<? extends Dimension> worldProvider;
	@Legacy
	public final Class<? extends NetherTeleporter> teleporter;
	@Legacy
	public String name = "Dimension";
	@Legacy
	public String soundTrigger = "portal.trigger";
	@Legacy
	public String soundTravel = "portal.travel";

	static {
		new DimensionOverworld();
		new DimensionNether();
	}

	@Legacy
	public static DimensionBase getDimByNumber(int number) {
		for (DimensionBase dim : list) {
			if (dim.number == number) {
				return dim;
			}
		}

		return null;
	}

	@Legacy
	public static DimensionBase getDimByProvider(Class<? extends Dimension> worldProvider) {
		for (DimensionBase dim : list) {
			if (dim.worldProvider.getName().equals(worldProvider.getName())) {
				return dim;
			}
		}

		return null;
	}

	@Legacy
	public Dimension getWorldProvider() {
		try {
			return this.worldProvider.newInstance();
		} catch (InstantiationException | IllegalAccessException ignored) {
		}

		return null;
	}

	@Legacy
	public NetherTeleporter getTeleporter() {
		try {
			if (this.teleporter != null) {
				return this.teleporter.newInstance();
			}
		} catch (InstantiationException | IllegalAccessException ignored) {
		}

		return null;
	}

	@Legacy
	public static void respawn(boolean paramBoolean, int paramInt) {
		Minecraft localMinecraft = SAPI.getMinecraftInstance();
		if (!localMinecraft.world.isClient && !localMinecraft.world.dimension.canPlayerSleep()) {
			usePortal(0, true);
		}

		Vec3i localbp1 = null;
		Vec3i localbp2 = null;
		int i = 1;
		if (localMinecraft.player != null && !paramBoolean) {
			localbp1 = localMinecraft.player.getSpawnPosition();
			if (localbp1 != null) {
				localbp2 = PlayerEntity.method_507(localMinecraft.world, localbp1);
				if (localbp2 == null) {
					localMinecraft.player.sendMessage("tile.bed.notValid");
				}
			}
		}

		if (localbp2 == null) {
			localbp2 = localMinecraft.world.getSpawnPosition();
			i = 0;
		}

		WorldSource localcj = localMinecraft.world.getCache();
		if (localcj instanceof ChunkCache) {
			ChunkCache localkt = (ChunkCache)localcj;
			localkt.method_1242(localbp2.x >> 4, localbp2.z >> 4);
		}

		localMinecraft.world.initSpawnPoint();
		localMinecraft.world.method_295();
		int j = 0;
		if (localMinecraft.player != null) {
			j = localMinecraft.player.entityId;
			localMinecraft.world.removeEntity(localMinecraft.player);
		}

		localMinecraft.viewEntity = null;
		localMinecraft.player = (AbstractClientPlayerEntity)localMinecraft.interactionManager.method_1717(localMinecraft.world);
		localMinecraft.player.dimensionId = paramInt;
		localMinecraft.viewEntity = localMinecraft.player;
		localMinecraft.player.afterSpawn();
		if (i != 0) {
			localMinecraft.player.setPlayerSpawn(localbp1);
			localMinecraft.player
					.setPositionAndAngles((double)((float)localbp2.x + 0.5F), (double)((float)localbp2.y + 0.1F), (double)((float)localbp2.z + 0.5F), 0.0F, 0.0F);
		}

		localMinecraft.interactionManager.rotatePlayer(localMinecraft.player);
		localMinecraft.world.addPlayer(localMinecraft.player);
		localMinecraft.player.playerKeypressManager = new MovementManager(localMinecraft.options);
		localMinecraft.player.entityId = j;
		localMinecraft.player.method_494();
		localMinecraft.interactionManager.method_1718(localMinecraft.player);
		localMinecraft.loadIntoWorld("Respawning");

		if (localMinecraft.currentScreen instanceof DeathScreen) {
			localMinecraft.openScreen(null);
		}

	}

	@Legacy
	public static void usePortal(int dimNumber) {
		usePortal(dimNumber, false);
	}

	@Legacy
	private static void usePortal(int dimNumber, boolean resetOrder) {
		Minecraft game = SAPI.getMinecraftInstance();
		int oldDimension = game.player.dimensionId;
		int newDimension = dimNumber;
		if (oldDimension == dimNumber) {
			newDimension = 0;
		}

		game.world.removeEntity(game.player);
		game.player.removed = false;
		Loc loc = new Loc(game.player.x, game.player.z);
		if (newDimension != 0) {
			order.push(newDimension);
		}

		if (newDimension == 0 && !order.isEmpty()) {
			newDimension = order.pop();
		}

		if (oldDimension == newDimension) {
			newDimension = 0;
		}

		StringBuilder str = new StringBuilder();

		for(Integer dim : order) {
			if (str.length() > 0) {
				str.append(",");
			}

			str.append(dim);
		}

		World world;
		DimensionBase dimOld = getDimByNumber(oldDimension);
		DimensionBase dimNew = getDimByNumber(newDimension);
		loc = dimOld.getDistanceScale(loc, true);
		loc = dimNew.getDistanceScale(loc, false);
		game.player.dimensionId = newDimension;
		game.player.setPositionAndAngles(loc.x, game.player.y, loc.z, game.player.yaw, game.player.pitch);
		game.world.method_193(game.player, false);
		world = new World(game.world, dimNew.getWorldProvider());
		game.showLevelProgress(world, (newDimension == 0 ? "Leaving" : "Entering") + " the " + (newDimension == 0 ? dimOld.name : dimNew.name), game.player);
		game.player.world = game.world;
		game.player.setPositionAndAngles(loc.x, game.player.y, loc.z, game.player.yaw, game.player.pitch);
		game.world.method_193(game.player, false);
		NetherTeleporter teleporter = dimNew.getTeleporter();
		if (teleporter == null) {
			teleporter = dimOld.getTeleporter();
		}

		teleporter.teleport(game.world, game.player);
	}

	@Legacy
	public DimensionBase(int number, Class<? extends Dimension> worldProvider, Class<? extends NetherTeleporter> teleporter) {
		this.number = number;
		this.worldProvider = worldProvider;
		this.teleporter = teleporter;
		list.add(this);
	}

	@Legacy
	public Loc getDistanceScale(Loc loc, boolean goingIn) {
		return loc;
	}

	/*
	* Originally Dimension#getByID(I)Dimension;
	*/
	@Legacy
	public static Dimension getByID(int i) {
		DimensionBase dimensionbase = getDimByNumber(i);
		if (dimensionbase != null) {
			return dimensionbase.getWorldProvider();
		} else {
			return i == -1 ? new NetherDimension() : (i == 0 ? new OverworldDimension() : (i == 1 ? new SkyDimension() : null));
		}
	}
}
