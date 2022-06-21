package shockahpi;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import modloader.BaseMod;
import modloader.ModLoader;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.block.DispenserBlockEntity;
import net.minecraft.entity.block.FurnaceBlockEntity;
import net.minecraft.entity.block.SignBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.achievement.Achievement;
import net.minecraft.util.SleepStatus;
import net.minecraft.util.io.CompoundTag;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionData;

import io.github.betterthanupdates.apron.api.ApronApi;

/**
 * ShockAhPi - Adding new possibilities in 3... 2... 1...
 * @author ShockAh
 */
@SuppressWarnings({"unused", "UnusedReturnValue", "BooleanMethodIsAlwaysInverted"})
public final class SAPI {
	// Apron
	private static final ApronApi APRON = ApronApi.getInstance();

	private static final ArrayList<IInterceptHarvest> harvestIntercepts = new ArrayList<>();
	private static final ArrayList<IInterceptBlockSet> setIntercepts = new ArrayList<>();
	private static final ArrayList<IReachBlock> reachesBlock = new ArrayList<>();
	private static final ArrayList<IReachEntity> reachesEntity = new ArrayList<>();
	private static final ArrayList<String> DUNGEON_MOBS = new ArrayList<>();
	private static final ArrayList<DungeonLoot> DUNGEON_ITEMS = new ArrayList<>();
	private static final ArrayList<DungeonLoot> DUNGEON_GUARANTEED = new ArrayList<>();

	private static final Random DUNGEON_RANDOM = new Random();

	private static boolean dungeonAddedMobs = false;
	private static boolean dungeonAddedItems = false;
	public static int acCurrentPage = 0;
	private static final ArrayList<Integer> acHidden = new ArrayList<>();
	private static final ArrayList<AchievementPage> ACHIEVEMENT_PAGES = new ArrayList<>();
	public static final AchievementPage acDefaultPage = AchievementPage.DEFAULT;
	private static final ArrayList<INBT> NBT_LIST = new ArrayList<>();
	private static final ArrayList<BaseMod> LOCKS = new ArrayList<>();
	private static final AtomicBoolean INITIALIZED = new AtomicBoolean(false);
	private static final ArrayList<Class<? extends PlayerBase>> PLAYER_BASE_CLASSES = new ArrayList<>();

	public SAPI() {
		if (INITIALIZED.getAndSet(true)) {
			throw new RuntimeException("Already created a SAPI instance");
		}
	}

	public static void interceptAdd(IInterceptHarvest harvestIntercept) {
		harvestIntercepts.add(harvestIntercept);
	}

	public static boolean interceptHarvest(World world, PlayerEntity player, Loc pos, int i, int j) {
		for (IInterceptHarvest harvestInterceptor : harvestIntercepts) {
			if (harvestInterceptor.canIntercept(world, player, pos, i, j)) {
				harvestInterceptor.intercept(world, player, pos, i, j);
				return true;
			}
		}

		return false;
	}

	public static void drop(World world, Loc pos, ItemStack stack) {
		if (world.isClient) return;

		for (int i = 0; i < stack.count; i++) {
			float f = 0.7F;
			double d = (double) (world.rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
			double d1 = (double) (world.rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
			double d2 = (double) (world.rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
			ItemEntity itemEntity = new ItemEntity(world,
					pos.x + d, pos.y + d1, pos.z + d2,
					new ItemStack(stack.itemId, 1,
					stack.getDamage()));
			itemEntity.pickupDelay = 10;
			world.spawnEntity(itemEntity);
		}
	}

	public static void interceptAdd(IInterceptBlockSet blockIntercept) {
		setIntercepts.add(blockIntercept);
	}

	public static int interceptBlockSet(World world, Loc pos, int meta) {
		for (IInterceptBlockSet setIntercept : setIntercepts) {
			if (setIntercept.canIntercept(world, pos, meta)) {
				return setIntercept.intercept(world, pos, meta);
			}
		}

		return meta;
	}

	public static void reachAdd(IReachBlock reachBlock) {
		reachesBlock.add(reachBlock);
	}

	public static void reachAdd(IReachEntity reachEntity) {
		reachesEntity.add(reachEntity);
	}

	/**
	 * The original implementation from SAPI. This is kept for legacy purposes.
	 * @return the modified value if modded, or exactly 4f if not.
	 */
	@Deprecated
	@Environment(EnvType.CLIENT)
	public static float reachGetBlock() {
		return reachGetBlock(4f);
	}

	/**
	 * A better implementation for the internal mixin.
	 * @param original the value that would originally be returned
	 * @return the modified value if modded, the original value if not.
	 */
	@ApiStatus.Internal
	@Environment(EnvType.CLIENT)
	public static float reachGetBlock(float original) {
		PlayerEntity player = APRON.getPlayer();

		if (player != null) {
			ItemStack heldStack = player.getHeldItem();

			for (IReachBlock reachBlock : reachesBlock) {
				if (reachBlock.reachBlockItemMatches(heldStack)) {
					return reachBlock.getReachBlock(heldStack);
				}
			}
		}

		return original;
	}

	@Environment(EnvType.CLIENT)
	public static float reachGetEntity() {
		PlayerEntity player = APRON.getPlayer();

		if (player != null) {
			ItemStack heldStack = player.getHeldItem();

			for (IReachEntity reach : reachesEntity) {
				if (reach.reachEntityItemMatches(heldStack)) {
					return reach.getReachEntity(heldStack);
				}
			}
		}

		return 3.0F;
	}

	public static void dungeonAddMob(String mob) {
		dungeonAddMob(mob, 10);
	}

	public static void dungeonAddMob(String mob, int chances) {
		for (int i = 0; i < chances; ++i) {
			DUNGEON_MOBS.add(mob);
		}
	}

	public static void dungeonRemoveMob(String mob) {
		for (int i = 0; i < DUNGEON_MOBS.size(); ++i) {
			if (DUNGEON_MOBS.get(i).equals(mob)) {
				DUNGEON_MOBS.remove(i);
				--i;
			}
		}
	}

	public static void dungeonRemoveAllMobs() {
		dungeonAddedMobs = true;
		DUNGEON_MOBS.clear();
	}

	/**
	 * Adds ten skeletons, ten zombies, and twenty spiders to the dungeon mob spawn list.
	 */
	static void dungeonAddDefaultMobs() {
		int i;

		for (i = 0; i < 10; ++i) {
			DUNGEON_MOBS.add("Skeleton");
			DUNGEON_MOBS.add("Spider");
		}

		for (i = 0; i < 20; ++i) {
			DUNGEON_MOBS.add("Zombie");
		}
	}

	@NotNull
	public static String dungeonGetRandomMob() {
		if (!dungeonAddedMobs) {
			dungeonAddDefaultMobs();
			dungeonAddedMobs = true;
		}

		return DUNGEON_MOBS.isEmpty() ? "Pig" : DUNGEON_MOBS.get((DUNGEON_RANDOM).nextInt(DUNGEON_MOBS.size()));
	}

	public static void dungeonAddItem(DungeonLoot loot) {
		dungeonAddItem(loot, 100);
	}

	public static void dungeonAddItem(DungeonLoot loot, int chances) {
		for (int i = 0; i < chances; ++i) {
			DUNGEON_ITEMS.add(loot);
		}
	}

	public static void dungeonAddGuaranteedItem(DungeonLoot loot) {
		DUNGEON_GUARANTEED.add(loot);
	}

	public static int dungeonGetAmountOfGuaranteed() {
		return DUNGEON_GUARANTEED.size();
	}

	public static DungeonLoot dungeonGetGuaranteed(int index) {
		return DUNGEON_GUARANTEED.get(index);
	}

	public static void dungeonRemoveItem(int id) {
		int i;

		for (i = 0; i < DUNGEON_ITEMS.size(); ++i) {
			if (DUNGEON_ITEMS.get(i).loot.itemId == id) {
				DUNGEON_ITEMS.remove(i--);
			}
		}

		for (i = 0; i < DUNGEON_GUARANTEED.size(); ++i) {
			if (DUNGEON_GUARANTEED.get(i).loot.itemId == id) {
				DUNGEON_GUARANTEED.remove(i--);
			}
		}
	}

	public static void dungeonRemoveAllItems() {
		dungeonAddedItems = true;
		DUNGEON_ITEMS.clear();
		DUNGEON_GUARANTEED.clear();
	}

	/**
	 * Adds one hundred chances to the dungeon items list for 1 saddle, 1-4 iron ingots, 1 bread, 1-4 wheat,
	 * 1-4 gunpowder, 1-4 string, or 1 bucket<br>
	 * <br>
	 * Adds one chance for a golden apple.<br>
	 * <br>
	 * Adds fifty chances for 1-4 redstone dust.<br>
	 * <br>
	 * Adds five chances for record 13 or record cat.<br>
	 */
	static void dungeonAddDefaultItems() {
		int i;

		for (i = 0; i < 100; ++i) {
			DUNGEON_ITEMS.add(new DungeonLoot(new ItemStack(Item.SADDLE)));
			DUNGEON_ITEMS.add(new DungeonLoot(new ItemStack(Item.IRON_INGOT), 1, 4));
			DUNGEON_ITEMS.add(new DungeonLoot(new ItemStack(Item.BREAD)));
			DUNGEON_ITEMS.add(new DungeonLoot(new ItemStack(Item.WHEAT), 1, 4));
			DUNGEON_ITEMS.add(new DungeonLoot(new ItemStack(Item.GUNPOWDER), 1, 4));
			DUNGEON_ITEMS.add(new DungeonLoot(new ItemStack(Item.STRING), 1, 4));
			DUNGEON_ITEMS.add(new DungeonLoot(new ItemStack(Item.BUCKET)));
		}

		for (i = 0; i < 50; ++i) {
			DUNGEON_ITEMS.add(new DungeonLoot(new ItemStack(Item.REDSTONE_DUST), 1, 4));
		}

		for (i = 0; i < 5; ++i) {
			DUNGEON_ITEMS.add(new DungeonLoot(new ItemStack(Item.RECORD_13)));
			DUNGEON_ITEMS.add(new DungeonLoot(new ItemStack(Item.RECORD_CAT)));
		}

		DUNGEON_ITEMS.add(new DungeonLoot(new ItemStack(Item.GOLDEN_APPLE)));
	}

	public static ItemStack dungeonGetRandomItem() {
		if (!dungeonAddedItems) {
			dungeonAddDefaultItems();
			dungeonAddedItems = true;
		}

		return DUNGEON_ITEMS.isEmpty() ? null : DUNGEON_ITEMS.get((new Random()).nextInt(DUNGEON_ITEMS.size())).getStack();
	}

	public static void acPageAdd(AchievementPage page) {
		ACHIEVEMENT_PAGES.add(page);
	}

	public static void acHide(Achievement... achievements) {
		for (Achievement achievement : achievements) {
			acHidden.add(achievement.id);
		}
	}

	public static boolean acIsHidden(Achievement achievement) {
		return acHidden.contains(achievement.id);
	}

	public static AchievementPage acGetPage(Achievement achievement) {
		if (achievement == null) {
			return null;
		} else {
			for (AchievementPage Page : ACHIEVEMENT_PAGES) {
				if (Page.list.contains(achievement.id)) {
					return Page;
				}
			}
		}

		return acDefaultPage;
	}

	public static AchievementPage acGetCurrentPage() {
		return ACHIEVEMENT_PAGES.get(acCurrentPage);
	}

	public static String acGetCurrentPageTitle() {
		return acGetCurrentPage().title;
	}

	public static void acPageNext() {
		++acCurrentPage;

		if (acCurrentPage > ACHIEVEMENT_PAGES.size() - 1) {
			acCurrentPage = 0;
		}
	}

	public static void acPagePrev() {
		if (--acCurrentPage < 0) {
			acCurrentPage = ACHIEVEMENT_PAGES.size() - 1;
		}
	}

	public static void addNBT(INBT nbt) {
		NBT_LIST.add(nbt);
	}

	public static ArrayList<INBT> getNBTList() {
		return new ArrayList<>(NBT_LIST);
	}

	public static void lockDifficulty(BaseMod mod) {
		LOCKS.add(mod);
	}

	public static String lockedErrorList() {
		StringBuilder str = new StringBuilder();

		for (BaseMod mod : LOCKS) {
			str.append(mod.toString().substring(4));

			if (str.length() > 0) {
				str.append(", ");
			}
		}

		return str.toString();
	}

	public static boolean isLocked(DimensionData data) {
		if (!ShockAhPI.forceLockingDifficulty) {
			return false;
		} else {
			return ShockAhPI.lockDifficulty != -1;
		}
	}

	public static void PAPIregisterPlayerBase(Class<? extends PlayerBase> pbClass) {
		PLAYER_BASE_CLASSES.add(pbClass);
	}

	@Nullable
	public static PlayerBase PAPIgetPlayerBase(AbstractClientPlayerEntity player, Class<? extends PlayerBase> pb) {
		SapiClientPlayerEntity clientPlayer = (SapiClientPlayerEntity) player;

		for (PlayerBase base : clientPlayer.playerBases) {
			if (pb.isInstance(base)) {
				return base;
			}
		}

		return null;
	}

	public ArrayList<PlayerBase> PAPIplayerInit(AbstractClientPlayerEntity player) {
		ArrayList<PlayerBase> playerBases = new ArrayList<>();

		for (Class<? extends PlayerBase> pbClass : PLAYER_BASE_CLASSES) {
			try {
				playerBases.add(pbClass.getDeclaredConstructor(AbstractClientPlayerEntity.class).newInstance(player));
			} catch (Exception e) {
				ModLoader.ThrowException("Could not init player for SAPI / PAPI.", e);
			}
		}

		return playerBases;
	}

	public boolean PAPIonLivingUpdate(AbstractClientPlayerEntity player) {
		SapiClientPlayerEntity clientPlayer = (SapiClientPlayerEntity) player;
		boolean override = false;

		for (PlayerBase base : clientPlayer.playerBases) {
			if (base.onLivingUpdate()) {
				override = true;
			}
		}

		return override;
	}

	public boolean PAPIrespawn(AbstractClientPlayerEntity player) {
		SapiClientPlayerEntity clientPlayer = (SapiClientPlayerEntity) player;
		boolean override = false;

		for (PlayerBase base : clientPlayer.playerBases) {
			if (base.respawn()) {
				override = true;
			}
		}

		return override;
	}

	public boolean PAPImoveFlying(AbstractClientPlayerEntity player, float x, float y, float z) {
		SapiClientPlayerEntity clientPlayer = (SapiClientPlayerEntity) player;
		boolean override = false;

		for (PlayerBase base : clientPlayer.playerBases) {
			if (base.moveFlying(x, y, z)) {
				override = true;
			}
		}

		return override;
	}

	public boolean PAPIupdatePlayerActionState(AbstractClientPlayerEntity player) {
		SapiClientPlayerEntity clientPlayer = (SapiClientPlayerEntity) player;
		boolean override = false;

		for (PlayerBase base : clientPlayer.playerBases) {
			if (base.updatePlayerActionState()) {
				override = true;
			}
		}

		return override;
	}

	public boolean PAPIhandleKeyPress(AbstractClientPlayerEntity player, int j, boolean flag) {
		SapiClientPlayerEntity clientPlayer = (SapiClientPlayerEntity) player;
		boolean override = false;

		for (PlayerBase base : clientPlayer.playerBases) {
			if (base.handleKeyPress(j, flag)) {
				override = true;
			}
		}

		return override;
	}

	public boolean PAPIwriteEntityToNBT(AbstractClientPlayerEntity player, CompoundTag tag) {
		SapiClientPlayerEntity clientPlayer = (SapiClientPlayerEntity) player;
		boolean override = false;

		for (PlayerBase base : clientPlayer.playerBases) {
			if (base.writeEntityToNBT(tag)) {
				override = true;
			}
		}

		return override;
	}

	public boolean PAPIreadEntityFromNBT(AbstractClientPlayerEntity player, CompoundTag tag) {
		SapiClientPlayerEntity clientPlayer = (SapiClientPlayerEntity) player;
		boolean override = false;

		for (PlayerBase base : clientPlayer.playerBases) {
			if (base.readEntityFromNBT(tag)) {
				override = true;
			}
		}

		return override;
	}

	public boolean PAPIonExitGUI(AbstractClientPlayerEntity player) {
		SapiClientPlayerEntity clientPlayer = (SapiClientPlayerEntity) player;
		boolean override = false;

		for (PlayerBase base : clientPlayer.playerBases) {
			if (base.onExitGUI()) {
				override = true;
			}
		}

		return override;
	}

	public boolean PAPIsetEntityDead(AbstractClientPlayerEntity player) {
		SapiClientPlayerEntity clientPlayer = (SapiClientPlayerEntity) player;
		boolean override = false;

		for (PlayerBase base : clientPlayer.playerBases) {
			if (base.setEntityDead()) {
				override = true;
			}
		}

		return override;
	}

	public boolean PAPIonDeath(AbstractClientPlayerEntity player, Entity killer) {
		SapiClientPlayerEntity clientPlayer = (SapiClientPlayerEntity) player;
		boolean override = false;

		for (PlayerBase base : clientPlayer.playerBases) {
			if (base.onDeath(killer)) {
				override = true;
			}
		}

		return override;
	}

	public boolean PAPIattackEntityFrom(AbstractClientPlayerEntity player, Entity attacker, int damage) {
		SapiClientPlayerEntity clientPlayer = (SapiClientPlayerEntity) player;
		boolean override = false;

		for (PlayerBase base : clientPlayer.playerBases) {
			if (base.attackEntityFrom(attacker, damage)) {
				override = true;
			}
		}

		return override;
	}

	public double PAPIgetDistanceSq(AbstractClientPlayerEntity player, double d, double d1, double d2, double answer) {
		SapiClientPlayerEntity clientPlayer = (SapiClientPlayerEntity) player;

		for (PlayerBase base : clientPlayer.playerBases) {
			answer = base.getDistanceSq(d, d1, d2, answer);
		}

		return answer;
	}

	public boolean PAPIisInWater(AbstractClientPlayerEntity player, boolean inWater) {
		SapiClientPlayerEntity clientPlayer = (SapiClientPlayerEntity) player;

		for (PlayerBase base : clientPlayer.playerBases) {
			inWater = base.isInWater(inWater);
		}

		return inWater;
	}

	public boolean PAPIcanTriggerWalking(AbstractClientPlayerEntity player, boolean canTrigger) {
		SapiClientPlayerEntity clientPlayer = (SapiClientPlayerEntity) player;

		for (PlayerBase base : clientPlayer.playerBases) {
			canTrigger = base.canTriggerWalking(canTrigger);
		}

		return canTrigger;
	}

	public boolean PAPIheal(AbstractClientPlayerEntity player, int j) {
		SapiClientPlayerEntity clientPlayer = (SapiClientPlayerEntity) player;
		boolean override = false;

		for (PlayerBase base : clientPlayer.playerBases) {
			if (base.heal(j)) {
				override = true;
			}
		}

		return override;
	}

	public int PAPIgetPlayerArmorValue(AbstractClientPlayerEntity player, int armor) {
		SapiClientPlayerEntity clientPlayer = (SapiClientPlayerEntity) player;

		for (PlayerBase base : clientPlayer.playerBases) {
			armor = base.getPlayerArmorValue(armor);
		}

		return armor;
	}

	public float PAPIgetCurrentPlayerStrVsBlock(AbstractClientPlayerEntity player, Block block, float answer) {
		SapiClientPlayerEntity clientPlayer = (SapiClientPlayerEntity) player;

		for (PlayerBase base : clientPlayer.playerBases) {
			answer = base.getCurrentPlayerStrVsBlock(block, answer);
		}

		return answer;
	}

	public boolean PAPImoveEntity(AbstractClientPlayerEntity player, double xVelocity, double yVelocity, double zVelocity) {
		SapiClientPlayerEntity clientPlayer = (SapiClientPlayerEntity) player;
		boolean override = false;

		for (PlayerBase base : clientPlayer.playerBases) {
			if (base.moveEntity(xVelocity, yVelocity, zVelocity)) {
				override = true;
			}
		}

		return override;
	}

	public SleepStatus PAPIsleepInBedAt(AbstractClientPlayerEntity player, int x, int y, int z) {
		SleepStatus status = null;
		SapiClientPlayerEntity clientPlayer = (SapiClientPlayerEntity) player;

		for (PlayerBase base : clientPlayer.playerBases) {
			status = base.sleepInBedAt(x, y, z, status);
		}

		return status;
	}

	public float PAPIgetEntityBrightness(AbstractClientPlayerEntity player, float f, float brightness) {
		SapiClientPlayerEntity clientPlayer = (SapiClientPlayerEntity) player;

		for (PlayerBase base : clientPlayer.playerBases) {
			f = base.getEntityBrightness(f, brightness);
		}

		return f;
	}

	public boolean PAPIpushOutOfBlocks(AbstractClientPlayerEntity player, double d, double d1, double d2) {
		boolean override = false;
		SapiClientPlayerEntity clientPlayer = (SapiClientPlayerEntity) player;

		for (PlayerBase base : clientPlayer.playerBases) {
			if (base.pushOutOfBlocks(d, d1, d2)) {
				override = true;
			}
		}

		return override;
	}

	public boolean PAPIonUpdate(AbstractClientPlayerEntity player) {
		boolean override = false;
		SapiClientPlayerEntity clientPlayer = (SapiClientPlayerEntity) player;

		for (PlayerBase base : clientPlayer.playerBases) {
			if (base.onUpdate()) {
				override = true;
			}
		}

		return override;
	}

	public void PAPIafterUpdate(AbstractClientPlayerEntity player) {
		SapiClientPlayerEntity clientPlayer = (SapiClientPlayerEntity) player;

		for (PlayerBase base : clientPlayer.playerBases) {
			base.afterUpdate();
		}
	}

	public boolean PAPImoveEntityWithHeading(AbstractClientPlayerEntity player, float f, float f1) {
		boolean override = false;
		SapiClientPlayerEntity clientPlayer = (SapiClientPlayerEntity) player;

		for (PlayerBase base : clientPlayer.playerBases) {
			if (base.moveEntityWithHeading(f, f1)) {
				override = true;
			}
		}

		return override;
	}

	public boolean PAPIisOnLadder(AbstractClientPlayerEntity player, boolean onLadder) {
		SapiClientPlayerEntity clientPlayer = (SapiClientPlayerEntity) player;

		for (PlayerBase base : clientPlayer.playerBases) {
			onLadder = base.isOnLadder(onLadder);
		}

		return onLadder;
	}

	public boolean PAPIisInsideOfMaterial(AbstractClientPlayerEntity player, Material material, boolean inMaterial) {
		SapiClientPlayerEntity clientPlayer = (SapiClientPlayerEntity) player;

		for (PlayerBase base : clientPlayer.playerBases) {
			inMaterial = base.isInsideOfMaterial(material, inMaterial);
		}

		return inMaterial;
	}

	public boolean PAPIisSneaking(AbstractClientPlayerEntity player, boolean sneaking) {
		SapiClientPlayerEntity clientPlayer = (SapiClientPlayerEntity) player;

		for (PlayerBase base : clientPlayer.playerBases) {
			sneaking = base.isSneaking(sneaking);
		}

		return sneaking;
	}

	public boolean PAPIdropCurrentItem(AbstractClientPlayerEntity player) {
		boolean override = false;
		SapiClientPlayerEntity clientPlayer = (SapiClientPlayerEntity) player;

		for (PlayerBase base : clientPlayer.playerBases) {
			if (base.dropCurrentItem()) {
				override = true;
			}
		}

		return override;
	}

	public boolean PAPIdropPlayerItem(AbstractClientPlayerEntity player, ItemStack stack) {
		boolean override = false;
		SapiClientPlayerEntity clientPlayer = (SapiClientPlayerEntity) player;

		for (PlayerBase base : clientPlayer.playerBases) {
			if (base.dropPlayerItem(stack)) {
				override = true;
			}
		}

		return override;
	}

	public boolean PAPIdisplayGUIEditSign(AbstractClientPlayerEntity player, SignBlockEntity sign) {
		boolean override = false;
		SapiClientPlayerEntity clientPlayer = (SapiClientPlayerEntity) player;

		for (PlayerBase base : clientPlayer.playerBases) {
			if (base.displayGUIEditSign(sign)) {
				override = true;
			}
		}

		return override;
	}

	public boolean PAPIdisplayGUIChest(AbstractClientPlayerEntity player, Inventory inventory) {
		boolean override = false;
		SapiClientPlayerEntity clientPlayer = (SapiClientPlayerEntity) player;

		for (PlayerBase base : clientPlayer.playerBases) {
			if (base.displayGUIChest(inventory)) {
				override = true;
			}
		}

		return override;
	}

	public boolean PAPIdisplayWorkbenchGUI(AbstractClientPlayerEntity player, int x, int y, int z) {
		boolean override = false;
		SapiClientPlayerEntity clientPlayer = (SapiClientPlayerEntity) player;

		for (PlayerBase base : clientPlayer.playerBases) {
			if (base.displayWorkbenchGUI(x, y, z)) {
				override = true;
			}
		}

		return override;
	}

	public boolean PAPIdisplayGUIFurnace(AbstractClientPlayerEntity player, FurnaceBlockEntity furnace) {
		boolean override = false;
		SapiClientPlayerEntity clientPlayer = (SapiClientPlayerEntity) player;

		for (PlayerBase base : clientPlayer.playerBases) {
			if (base.displayGUIFurnace(furnace)) {
				override = true;
			}
		}

		return override;
	}

	public boolean PAPIdisplayGUIDispenser(AbstractClientPlayerEntity player, DispenserBlockEntity dispenser) {
		boolean override = false;
		SapiClientPlayerEntity clientPlayer = (SapiClientPlayerEntity) player;

		for (PlayerBase base : clientPlayer.playerBases) {
			if (base.displayGUIDispenser(dispenser)) {
				override = true;
			}
		}

		return override;
	}
}
