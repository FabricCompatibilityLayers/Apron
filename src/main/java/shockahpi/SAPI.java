package shockahpi;

import java.util.ArrayList;
import java.util.Random;

import net.legacyfabric.fabric.api.logger.v1.Logger;
import playerapi.PlayerAPI;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.achievement.Achievement;
import net.minecraft.world.World;

import io.github.betterthanupdates.Legacy;
import io.github.betterthanupdates.apron.api.ApronApi;

/**
 * ShockAhPi - Adding new possibilities in 3... 2... 1...
 *
 * @author ShockAh
 */
@SuppressWarnings({"unused", "UnusedReturnValue", "BooleanMethodIsAlwaysInverted"})
@Legacy
public class SAPI {
	public static final Logger LOGGER = ApronApi.getLogger("ShockAhPI");

	@Legacy
	public static boolean usingText = false;
	@Legacy
	private static final ArrayList<IInterceptHarvest> harvestIntercepts = new ArrayList<>();
	@Legacy
	private static final ArrayList<IInterceptBlockSet> setIntercepts = new ArrayList<>();
	@Legacy
	private static final ArrayList<IReach> reaches = new ArrayList<>();
	@Legacy
	private static final ArrayList<String> dngMobs = new ArrayList<>();
	@Legacy
	private static final ArrayList<DungeonLoot> dngItems = new ArrayList<>();
	@Legacy
	private static final ArrayList<DungeonLoot> dngGuaranteed = new ArrayList<>();
	@Legacy
	private static boolean dngAddedMobs = false;
	@Legacy
	private static boolean dngAddedItems = false;
	@Legacy
	public static int acCurrentPage = 0;
	@Legacy
	private static final ArrayList<Integer> acHidden = new ArrayList<>();
	@Legacy
	private static final ArrayList<ACPage> acPages = new ArrayList<>();
	@Legacy
	public static final ACPage acDefaultPage = new ACPage();

	@Legacy
	public SAPI() {
	}

	@Legacy
	public static void showText() {
		if (!usingText) {
			LOGGER.info("Using ShockAhPI r5.1");
			usingText = true;
		}
	}

	@Legacy
	public static Minecraft getMinecraftInstance() {
		return (Minecraft) ApronApi.getInstance().getGame();
	}

	@Legacy
	public static void interceptAdd(IInterceptHarvest iinterceptharvest) {
		harvestIntercepts.add(iinterceptharvest);
	}

	@Legacy
	public static boolean interceptHarvest(World world, PlayerEntity entityplayer, Loc loc, int i, int j) {
		for (IInterceptHarvest iinterceptharvest : harvestIntercepts) {
			if (iinterceptharvest.canIntercept(world, entityplayer, loc, i, j)) {
				iinterceptharvest.intercept(world, entityplayer, loc, i, j);
				return true;
			}
		}

		return false;
	}

	@Legacy
	public static void drop(World world, Loc loc, ItemStack itemstack) {
		if (!world.isClient) {
			for (int i = 0; i < itemstack.count; ++i) {
				float f = 0.7F;
				double d = (double) (world.rand.nextFloat() * f) + (double) (1.0F - f) * 0.5;
				double d1 = (double) (world.rand.nextFloat() * f) + (double) (1.0F - f) * 0.5;
				double d2 = (double) (world.rand.nextFloat() * f) + (double) (1.0F - f) * 0.5;
				ItemEntity entityitem = new ItemEntity(
						world, (double) loc.x() + d, (double) loc.y() + d1, (double) loc.z() + d2, new ItemStack(itemstack.itemId, 1, itemstack.getMeta())
				);
				entityitem.pickupDelay = 10;
				world.spawnEntity(entityitem);
			}
		}
	}

	@Legacy
	public static void interceptAdd(IInterceptBlockSet iinterceptblockset) {
		setIntercepts.add(iinterceptblockset);
	}

	@Legacy
	public static int interceptBlockSet(World world, Loc loc, int i) {
		for (IInterceptBlockSet iinterceptblockset : setIntercepts) {
			if (iinterceptblockset.canIntercept(world, loc, i)) {
				return iinterceptblockset.intercept(world, loc, i);
			}
		}

		return i;
	}

	@Legacy
	public static void reachAdd(IReach ireach) {
		reaches.add(ireach);
	}

	@Legacy
	public static float reachGet() {
		ItemStack itemstack = getMinecraftInstance().player.inventory.getHeldItem();

		for (IReach ireach : reaches) {
			if (ireach.reachItemMatches(itemstack)) {
				return ireach.getReach(itemstack);
			}
		}

		return 4.0F;
	}

	@Legacy
	public static void dungeonAddMob(String s) {
		dungeonAddMob(s, 10);
	}

	@Legacy
	public static void dungeonAddMob(String s, int i) {
		for (int j = 0; j < i; ++j) {
			dngMobs.add(s);
		}
	}

	@Legacy
	public static void dungeonRemoveMob(String s) {
		for (int i = 0; i < dngMobs.size(); ++i) {
			if (dngMobs.get(i).equals(s)) {
				dngMobs.remove(i);
				--i;
			}
		}
	}

	@Legacy
	public static void dungeonRemoveAllMobs() {
		dngAddedMobs = true;
		dngMobs.clear();
	}

	@Legacy
	static void dungeonAddDefaultMobs() {
		for (int i = 0; i < 10; ++i) {
			dngMobs.add("Skeleton");
		}

		for (int j = 0; j < 20; ++j) {
			dngMobs.add("Zombie");
		}

		for (int k = 0; k < 10; ++k) {
			dngMobs.add("Spider");
		}
	}

	@Legacy
	public static String dungeonGetRandomMob() {
		if (!dngAddedMobs) {
			dungeonAddDefaultMobs();
			dngAddedMobs = true;
		}

		return dngMobs.isEmpty() ? "Pig" : dngMobs.get(new Random().nextInt(dngMobs.size()));
	}

	@Legacy
	public static void dungeonAddItem(DungeonLoot dungeonloot) {
		dungeonAddItem(dungeonloot, 100);
	}

	@Legacy
	public static void dungeonAddItem(DungeonLoot dungeonloot, int i) {
		for (int j = 0; j < i; ++j) {
			dngItems.add(dungeonloot);
		}
	}

	@Legacy
	public static void dungeonAddGuaranteedItem(DungeonLoot dungeonloot) {
		dngGuaranteed.add(dungeonloot);
	}

	@Legacy
	public static int dungeonGetAmountOfGuaranteed() {
		return dngGuaranteed.size();
	}

	@Legacy
	public static DungeonLoot dungeonGetGuaranteed(int i) {
		return dngGuaranteed.get(i);
	}

	@Legacy
	public static void dungeonRemoveItem(int i) {
		for (int j = 0; j < dngItems.size(); ++j) {
			if (dngItems.get(j).loot.itemId == i) {
				dngItems.remove(j);
				--j;
			}
		}

		for (int k = 0; k < dngGuaranteed.size(); ++k) {
			if (dngGuaranteed.get(k).loot.itemId == i) {
				dngGuaranteed.remove(k);
				--k;
			}
		}
	}

	@Legacy
	public static void dungeonRemoveAllItems() {
		dngAddedItems = true;
		dngItems.clear();
		dngGuaranteed.clear();
	}

	@Legacy
	static void dungeonAddDefaultItems() {
		for (int i = 0; i < 100; ++i) {
			dngItems.add(new DungeonLoot(new ItemStack(Item.SADDLE)));
		}

		for (int j = 0; j < 100; ++j) {
			dngItems.add(new DungeonLoot(new ItemStack(Item.IRON_INGOT), 1, 4));
		}

		for (int k = 0; k < 100; ++k) {
			dngItems.add(new DungeonLoot(new ItemStack(Item.BREAD)));
		}

		for (int l = 0; l < 100; ++l) {
			dngItems.add(new DungeonLoot(new ItemStack(Item.WHEAT), 1, 4));
		}

		for (int i1 = 0; i1 < 100; ++i1) {
			dngItems.add(new DungeonLoot(new ItemStack(Item.GUNPOWDER), 1, 4));
		}

		for (int j1 = 0; j1 < 100; ++j1) {
			dngItems.add(new DungeonLoot(new ItemStack(Item.STRING), 1, 4));
		}

		for (int k1 = 0; k1 < 100; ++k1) {
			dngItems.add(new DungeonLoot(new ItemStack(Item.BUCKET)));
		}

		dngItems.add(new DungeonLoot(new ItemStack(Item.GOLDEN_APPLE)));

		for (int l1 = 0; l1 < 50; ++l1) {
			dngItems.add(new DungeonLoot(new ItemStack(Item.REDSTONE_DUST), 1, 4));
		}

		for (int i2 = 0; i2 < 5; ++i2) {
			dngItems.add(new DungeonLoot(new ItemStack(Item.RECORD_13)));
		}

		for (int j2 = 0; j2 < 5; ++j2) {
			dngItems.add(new DungeonLoot(new ItemStack(Item.RECORD_CAT)));
		}
	}

	@Legacy
	public static ItemStack dungeonGetRandomItem() {
		if (!dngAddedItems) {
			dungeonAddDefaultItems();
			dngAddedItems = true;
		}

		return dngItems.isEmpty() ? null : dngItems.get(new Random().nextInt(dngItems.size())).getStack();
	}

	@Legacy
	public static void acPageAdd(ACPage acpage) {
		acPages.add(acpage);
	}

	@Legacy
	public static void acHide(Achievement[] aachievement) {
		for (Achievement achievement : aachievement) {
			acHidden.add(achievement.id);
		}
	}

	@Legacy
	public static boolean acIsHidden(Achievement achievement) {
		return acHidden.contains(achievement.id);
	}

	@Legacy
	public static ACPage acGetPage(Achievement achievement) {
		if (achievement == null) {
			LOGGER.debug("Expected Achievement, got null instead.");
			return null;
		} else {
			for (ACPage acpage : acPages) {
				if (acpage.list.contains(achievement.id)) {
					return acpage;
				}
			}

			return acDefaultPage;
		}
	}

	@Legacy
	public static ACPage acGetCurrentPage() {
		return acPages.get(acCurrentPage);
	}

	@Legacy
	public static String acGetCurrentPageTitle() {
		return acGetCurrentPage().title;
	}

	@Legacy
	public static void acPageNext() {
		++acCurrentPage;

		if (acCurrentPage > acPages.size() - 1) {
			acCurrentPage = 0;
		}
	}

	@Legacy
	public static void acPagePrev() {
		--acCurrentPage;

		if (acCurrentPage < 0) {
			acCurrentPage = acPages.size() - 1;
		}
	}

	static {
		PlayerAPI.RegisterPlayerBase(PlayerBaseSAPI.class);
		showText();
	}
}
