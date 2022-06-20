package shockahpi;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;

import modloader.BaseMod;
import modloader.MLProp;
import modloader.ModLoader;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.MovementManager;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.io.CompoundTag;
import net.minecraft.util.io.IntTag;
import net.minecraft.util.io.ListTag;
import net.minecraft.util.io.TagInputOutput;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionData;
import net.minecraft.world.dimension.DimensionFile;

import io.github.betterthanupdates.apron.api.ApronApi;

/**
 * AKA "mod_SAPI" after remapping.
 */
public class ShockAhPI extends BaseMod {
	@MLProp
	public static boolean forceLockingDifficulty = true;
	private static World world;
	public static int lockDifficulty = -1;
	private static int counter = 0;

	public ShockAhPI() {
		ModLoader.SetInGameHook(this, true, true);
		ModLoader.SetInGUIHook(this, true, false);
	}

	public boolean OnTickInGame(Minecraft game) {
		if (counter == 1) {
			counter = 0;
			game.openScreen(new FreezeDifficultyScreen(game.world));
		}

		this.tick(game);

		if (!game.world.isClient) {
			if (game.world != world) {
				// If the game's world has changed, save, load the new world, and update the pointer
				if (world != null) saveNBT(world);
				if (game.world != null) loadNBT(game.world);
				world = game.world;
			}

			if (world != null && world.getProperties().getTime() % (long) world.autoSaveInterval == 0L) {
				saveNBT(world);
			}
		}

		return true;
	}

	public boolean OnTickInGUI(@NotNull Minecraft client, @Nullable Screen screen) {
		tick(client);
		world = null;
		return true;
	}

	public void tick(Minecraft game) {
		lockDifficulty(game);
		World world = game.world;

		if (world != null) {
			AbstractClientPlayerEntity player = game.player;

			if (player != null && !(player instanceof SapiClientPlayerEntity)) {
				SapiClientPlayerEntity newPlayer = new SapiClientPlayerEntity((Minecraft) ApronApi.getInstance().getGame(),
						player.world, game.session, player.dimensionId);
				CompoundTag tag = new CompoundTag();
				player.writeNBT(tag);
				newPlayer.readNBT(tag);
				newPlayer.playerKeypressManager = player.playerKeypressManager;
				game.world.method_295();
				int j = 0;

				if (game.player != null) {
					j = game.player.entityId;
					game.world.removeEntity(game.player);
				}

				game.player = newPlayer;
				game.viewEntity = game.player;
				game.player.afterSpawn();
				game.interactionManager.rotatePlayer(game.player);
				game.world.addPlayer(game.player);
				game.player.playerKeypressManager = new MovementManager(game.options);
				game.player.entityId = j;
				game.player.method_494();
				game.interactionManager.method_1718(game.player);
			}
		}
	}

	public static void saveNBT(@NotNull World world) {
		try {
			File saveLocation = GetWorldSaveLocation(world);
			File sapiData = new File(saveLocation, "SAPI.dat");

			if (!sapiData.exists()) {
				TagInputOutput.writeNBT(new CompoundTag(), Files.newOutputStream(sapiData.toPath()));
			}

			CompoundTag tag = TagInputOutput.readNBT(Files.newInputStream(sapiData.toPath()));
			ListTag nbtList = new ListTag();

			for (int i : DimensionBase.order) {
				nbtList.add(new IntTag(i));
			}

			tag.put("DimensionOrder", nbtList);
			tag.put("LockDifficulty", lockDifficulty);
			TagInputOutput.writeNBT(tag, Files.newOutputStream(sapiData.toPath()));
			ArrayList<INBT> list = SAPI.getNBTList();

			for (INBT inbt : list) {
				sapiData = new File(saveLocation, inbt.getFnameNBT());

				if (!sapiData.exists()) {
					TagInputOutput.writeNBT(new CompoundTag(), Files.newOutputStream(sapiData.toPath()));
				}

				tag = TagInputOutput.readNBT(Files.newInputStream(sapiData.toPath()));
				inbt.saveNBT(tag);
				TagInputOutput.writeNBT(tag, Files.newOutputStream(sapiData.toPath()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		updateShouldLock(world);
	}

	public static void loadNBT(World world) {
		try {
			File saveDirectory = GetWorldSaveLocation(world);
			File sapiData = new File(saveDirectory, "SAPI.dat");

			if (!sapiData.exists()) {
				TagInputOutput.writeNBT(new CompoundTag(), Files.newOutputStream(sapiData.toPath()));
			}

			CompoundTag tag = TagInputOutput.readNBT(Files.newInputStream(sapiData.toPath()));
			DimensionBase.order.clear();
			ListTag nbtList = tag.getListTag("DimensionOrder");

			for (int i = 0; i < nbtList.size(); ++i) {
				DimensionBase.order.add(((IntTag) nbtList.get(i)).data);
			}

			lockDifficulty = -1;

			if (tag.containsKey("LockDifficulty")) {
				lockDifficulty = tag.getInt("LockDifficulty");
			}

			ArrayList<INBT> list = SAPI.getNBTList();

			for (INBT inbt : list) {
				sapiData = new File(saveDirectory, inbt.getFnameNBT());

				if (!sapiData.exists()) {
					TagInputOutput.writeNBT(new CompoundTag(), Files.newOutputStream(sapiData.toPath()));
				}

				tag = TagInputOutput.readNBT(Files.newInputStream(sapiData.toPath()));
				inbt.loadNBT(tag);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		updateShouldLock(world);

		if (!SAPI.lockedErrorList().isEmpty() && lockDifficulty == -1) {
			counter = 1;
		}
	}

	@Nullable
	public static File GetWorldSaveLocation(World world) {
		return GetWorldSaveLocation(world.dimensionData);
	}

	@Nullable
	public static File GetWorldSaveLocation(DimensionData handler) {
		return handler instanceof DimensionFile ? ((DimensionFile) handler).getParentFolder() : null;
	}

	public static void updateShouldLock(World world) {
		if (world == null) {
			lockDifficulty = -1;
		}
	}

	public static void lockDifficulty(Minecraft game) {
		if (lockDifficulty != -1) {
			game.options.difficulty = lockDifficulty;
		}
	}

	@Override
	public String toString() {
		return "ShockAhPI " + Version();
	}

	@Override
	@ApiStatus.NonExtendable
	public String Version() {
		return "r8";
	}
}
