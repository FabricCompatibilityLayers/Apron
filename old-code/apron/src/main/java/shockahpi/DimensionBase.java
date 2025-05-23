package shockahpi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import io.github.betterthanupdates.apron.LifecycleUtils;
import net.minecraft.class_390;
import net.minecraft.class_413;
import net.minecraft.class_467;
import net.minecraft.class_51;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkCache;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.gen.feature.DeadBushPatchFeature;

import io.github.betterthanupdates.Legacy;

@Legacy
public class DimensionBase {
	public static ArrayList<DimensionBase> list = new ArrayList<>();
	public static LinkedList<Integer> order = new LinkedList<>();
	public final int number;
	public final Class<? extends Dimension> worldProvider;
	public final Class<? extends class_467> teleporter;
	public String name = "Dimension";
	public String soundTrigger = "portal.trigger";
	public String soundTravel = "portal.travel";

	static {
		new DimensionOverworld();
		new DimensionNether();
	}

	public static DimensionBase getDimByNumber(int number) {
		for (DimensionBase dim : list) {
			if (dim.number == number) {
				return dim;
			}
		}

		return null;
	}

	public static DimensionBase getDimByProvider(Class<? extends Dimension> worldProvider) {
		for (DimensionBase dim : list) {
			if (dim.worldProvider.getName().equals(worldProvider.getName())) {
				return dim;
			}
		}

		return null;
	}

	public Dimension getWorldProvider() {
		try {
			return this.worldProvider.newInstance();
		} catch (InstantiationException | IllegalAccessException ignored) {
		}

		return null;
	}

	public class_467 getTeleporter() {
		try {
			if (this.teleporter != null) {
				return this.teleporter.newInstance();
			}
		} catch (InstantiationException | IllegalAccessException ignored) {
		}

		return null;
	}

	public static void respawn(boolean paramBoolean, int paramInt) {
		Minecraft localMinecraft = SAPI.getMinecraftInstance();
		if (!localMinecraft.world.isRemote && !localMinecraft.world.dimension.method_1766()) {
			usePortal(0, true);
		}

		Vec3i localbp1 = null;
		Vec3i localbp2 = null;
		boolean i = true;
		if (localMinecraft.player != null && !paramBoolean) {
			localbp1 = localMinecraft.player.method_505();
			if (localbp1 != null) {
				localbp2 = PlayerEntity.method_507(localMinecraft.world, localbp1);
				if (localbp2 == null) {
					localMinecraft.player.method_490("tile.bed.notValid");
				}
			}
		}

		if (localbp2 == null) {
			localbp2 = localMinecraft.world.getSpawnPos();
			i = false;
		}

		class_51 localcj = localMinecraft.world.method_259();
		if (localcj instanceof class_390) {
			class_390 localkt = (class_390)localcj;
			localkt.method_1242(localbp2.x >> 4, localbp2.z >> 4);
		}

		localMinecraft.world.method_283();
		localMinecraft.world.method_295();
		int j = 0;
		if (localMinecraft.player != null) {
			j = localMinecraft.player.id;
			localMinecraft.world.method_231(localMinecraft.player);
		}

		localMinecraft.field_2807 = null;
		localMinecraft.player = (ClientPlayerEntity)localMinecraft.interactionManager.method_1717(localMinecraft.world);
		localMinecraft.player.dimensionId = paramInt;
		localMinecraft.field_2807 = localMinecraft.player;
		localMinecraft.player.method_1315();
		if (i) {
			localMinecraft.player.method_506(localbp1);
			localMinecraft.player.method_1341((double)((float)localbp2.x + 0.5F), (double)((float)localbp2.y + 0.1F), (double)((float)localbp2.z + 0.5F), 0.0F, 0.0F);
		}

		localMinecraft.interactionManager.method_1711(localMinecraft.player);
		localMinecraft.world.method_277(localMinecraft.player);
		localMinecraft.player.field_161 = new class_413(localMinecraft.options);
		localMinecraft.player.id = j;
		localMinecraft.player.method_494();
		localMinecraft.interactionManager.method_1718(localMinecraft.player);
		localMinecraft.method_2130("Respawning");

		if (localMinecraft.currentScreen instanceof DeathScreen) {
			localMinecraft.setScreen((Screen)null);
		}

	}

	public static void usePortal(int dimNumber) {
		usePortal(dimNumber, false);
	}

	private static void usePortal(int dimNumber, boolean resetOrder) {
		Minecraft game = SAPI.getMinecraftInstance();
		int oldDimension = game.player.dimensionId;
		int newDimension = dimNumber;
		if (oldDimension == dimNumber) {
			newDimension = 0;
		}

		game.world.method_231(game.player);
		game.player.dead = false;
		Loc loc = new Loc(game.player.x, game.player.z);
		if (newDimension != 0) {
			order.push(newDimension);
		}

		if (newDimension == 0 && !order.isEmpty()) {
			newDimension = (Integer)order.pop();
		}

		if (oldDimension == newDimension) {
			newDimension = 0;
		}

		String str = "";

		Integer dim;
		for(Iterator var8 = order.iterator(); var8.hasNext(); str = str + dim) {
			dim = (Integer)var8.next();
			if (!str.isEmpty()) {
				str = str + ",";
			}
		}

		dim = null;
		DimensionBase dimOld = getDimByNumber(oldDimension);
		DimensionBase dimNew = getDimByNumber(newDimension);
		loc = dimOld.getDistanceScale(loc, true);
		loc = dimNew.getDistanceScale(loc, false);
		game.player.dimensionId = newDimension;
		game.player.method_1341(loc.x, game.player.y, loc.z, game.player.yaw, game.player.pitch);
		game.world.method_193(game.player, false);
		World world = new World(game.world, dimNew.getWorldProvider());
		game.method_2115(world, (newDimension == 0 ? "Leaving" : "Entering") + " the " + (newDimension == 0 ? dimOld.name : dimNew.name), game.player);
		game.player.world = game.world;
		game.player.method_1341(loc.x, game.player.y, loc.z, game.player.yaw, game.player.pitch);
		game.world.method_193(game.player, false);
		class_467 teleporter = dimNew.getTeleporter();
		if (teleporter == null) {
			teleporter = dimOld.getTeleporter();
		}

		teleporter.method_1530(game.world, game.player);
	}

	public DimensionBase(int number, Class<? extends Dimension> worldProvider, Class<? extends class_467> teleporter) {
		this.number = number;
		this.worldProvider = worldProvider;
		this.teleporter = teleporter;
		list.add(this);

		if (!LifecycleUtils.SHOCKAHPI_DIMENSIONS.containsKey(LifecycleUtils.CURRENT_MOD)) {
			LifecycleUtils.SHOCKAHPI_DIMENSIONS.put(LifecycleUtils.CURRENT_MOD, new ArrayList<>());
		}

		LifecycleUtils.SHOCKAHPI_DIMENSIONS.get(LifecycleUtils.CURRENT_MOD).add(this);
	}

	public Loc getDistanceScale(Loc loc, boolean goingIn) {
		return loc;
	}
}
