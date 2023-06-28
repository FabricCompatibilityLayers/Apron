package shockahpi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import io.github.betterthanupdates.apron.api.ApronApi;
import playerapi.PlayerAPI;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.achievement.Achievement;
import net.minecraft.world.World;

import io.github.betterthanupdates.Legacy;

/**
 * ShockAhPi - Adding new possibilities in 3... 2... 1...
 *
 * @author ShockAh
 */
@SuppressWarnings({"unused", "UnusedReturnValue", "BooleanMethodIsAlwaysInverted"})
@Legacy
public class SAPI {
	private static Minecraft instance;
	public static boolean usingText;
	private static ArrayList harvestIntercepts;
	private static ArrayList setIntercepts;
	private static ArrayList reaches;
	private static ArrayList dngMobs;
	private static ArrayList dngItems;
	private static ArrayList dngGuaranteed;
	private static boolean dngAddedMobs;
	private static boolean dngAddedItems;
	public static int acCurrentPage;
	private static ArrayList acHidden;
	private static ArrayList ACHIEVEMENT_PAGES;
	public static final AchievementPage acDefaultPage;

	public SAPI() {
	}

	public static void showText() {
		if (!usingText) {
			System.out.println("Using ShockAhPI r5.1");
			usingText = true;
		}
	}

	public static Minecraft getMinecraftInstance() {
		return (Minecraft) ApronApi.getInstance().getGame();
	}

	public static void interceptAdd(IInterceptHarvest iinterceptharvest) {
		harvestIntercepts.add(iinterceptharvest);
	}

	public static boolean interceptHarvest(World world, PlayerEntity entityplayer, Loc loc, int i, int j) {
		Iterator iterator = harvestIntercepts.iterator();

		IInterceptHarvest iinterceptharvest;
		do {
			if (!iterator.hasNext()) {
				return false;
			}

			iinterceptharvest = (IInterceptHarvest)iterator.next();
		} while(!iinterceptharvest.canIntercept(world, entityplayer, loc, i, j));

		iinterceptharvest.intercept(world, entityplayer, loc, i, j);
		return true;
	}

	public static void drop(World world, Loc loc, ItemStack itemstack) {
		if (!world.isClient) {
			for(int i = 0; i < itemstack.count; ++i) {
				float f = 0.7F;
				double d = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5;
				double d1 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5;
				double d2 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5;
				ItemEntity entityitem = new ItemEntity(world, (double)loc.x() + d, (double)loc.y() + d1, (double)loc.z() + d2, new ItemStack(itemstack.itemId, 1, itemstack.getMeta()));
				entityitem.pickupDelay = 10;
				world.spawnEntity(entityitem);
			}

		}
	}

	public static void interceptAdd(IInterceptBlockSet iinterceptblockset) {
		setIntercepts.add(iinterceptblockset);
	}

	public static int interceptBlockSet(World world, Loc loc, int i) {
		Iterator iterator = setIntercepts.iterator();

		IInterceptBlockSet iinterceptblockset;
		do {
			if (!iterator.hasNext()) {
				return i;
			}

			iinterceptblockset = (IInterceptBlockSet)iterator.next();
		} while(!iinterceptblockset.canIntercept(world, loc, i));

		return iinterceptblockset.intercept(world, loc, i);
	}

	public static void reachAdd(IReach ireach) {
		reaches.add(ireach);
	}

	public static float reachGet() {
		ItemStack itemstack = getMinecraftInstance().player.inventory.getHeldItem();
		Iterator iterator = reaches.iterator();

		IReach ireach;
		do {
			if (!iterator.hasNext()) {
				return 4.0F;
			}

			ireach = (IReach)iterator.next();
		} while(!ireach.reachItemMatches(itemstack));

		return ireach.getReach(itemstack);
	}

	public static void dungeonAddMob(String s) {
		dungeonAddMob(s, 10);
	}

	public static void dungeonAddMob(String s, int i) {
		for(int j = 0; j < i; ++j) {
			dngMobs.add(s);
		}

	}

	public static void dungeonRemoveMob(String s) {
		for(int i = 0; i < dngMobs.size(); ++i) {
			if (((String)dngMobs.get(i)).equals(s)) {
				dngMobs.remove(i);
				--i;
			}
		}

	}

	public static void dungeonRemoveAllMobs() {
		dngAddedMobs = true;
		dngMobs.clear();
	}

	static void dungeonAddDefaultMobs() {
		int k;
		for(k = 0; k < 10; ++k) {
			dngMobs.add("Skeleton");
		}

		for(k = 0; k < 20; ++k) {
			dngMobs.add("Zombie");
		}

		for(k = 0; k < 10; ++k) {
			dngMobs.add("Spider");
		}

	}

	public static String dungeonGetRandomMob() {
		if (!dngAddedMobs) {
			dungeonAddDefaultMobs();
			dngAddedMobs = true;
		}

		return dngMobs.isEmpty() ? "Pig" : (String)dngMobs.get((new Random()).nextInt(dngMobs.size()));
	}

	public static void dungeonAddItem(DungeonLoot dungeonloot) {
		dungeonAddItem(dungeonloot, 100);
	}

	public static void dungeonAddItem(DungeonLoot dungeonloot, int i) {
		for(int j = 0; j < i; ++j) {
			dngItems.add(dungeonloot);
		}

	}

	public static void dungeonAddGuaranteedItem(DungeonLoot dungeonloot) {
		dngGuaranteed.add(dungeonloot);
	}

	public static int dungeonGetAmountOfGuaranteed() {
		return dngGuaranteed.size();
	}

	public static DungeonLoot dungeonGetGuaranteed(int i) {
		return (DungeonLoot)dngGuaranteed.get(i);
	}

	public static void dungeonRemoveItem(int i) {
		int k;
		for(k = 0; k < dngItems.size(); ++k) {
			if (((DungeonLoot)dngItems.get(k)).loot.itemId == i) {
				dngItems.remove(k);
				--k;
			}
		}

		for(k = 0; k < dngGuaranteed.size(); ++k) {
			if (((DungeonLoot)dngGuaranteed.get(k)).loot.itemId == i) {
				dngGuaranteed.remove(k);
				--k;
			}
		}

	}

	public static void dungeonRemoveAllItems() {
		dngAddedItems = true;
		dngItems.clear();
		dngGuaranteed.clear();
	}

	static void dungeonAddDefaultItems() {
		int j2;
		for(j2 = 0; j2 < 100; ++j2) {
			dngItems.add(new DungeonLoot(new ItemStack(Item.SADDLE)));
		}

		for(j2 = 0; j2 < 100; ++j2) {
			dngItems.add(new DungeonLoot(new ItemStack(Item.IRON_INGOT), 1, 4));
		}

		for(j2 = 0; j2 < 100; ++j2) {
			dngItems.add(new DungeonLoot(new ItemStack(Item.BREAD)));
		}

		for(j2 = 0; j2 < 100; ++j2) {
			dngItems.add(new DungeonLoot(new ItemStack(Item.WHEAT), 1, 4));
		}

		for(j2 = 0; j2 < 100; ++j2) {
			dngItems.add(new DungeonLoot(new ItemStack(Item.GUNPOWDER), 1, 4));
		}

		for(j2 = 0; j2 < 100; ++j2) {
			dngItems.add(new DungeonLoot(new ItemStack(Item.STRING), 1, 4));
		}

		for(j2 = 0; j2 < 100; ++j2) {
			dngItems.add(new DungeonLoot(new ItemStack(Item.BUCKET)));
		}

		dngItems.add(new DungeonLoot(new ItemStack(Item.GOLDEN_APPLE)));

		for(j2 = 0; j2 < 50; ++j2) {
			dngItems.add(new DungeonLoot(new ItemStack(Item.REDSTONE_DUST), 1, 4));
		}

		for(j2 = 0; j2 < 5; ++j2) {
			dngItems.add(new DungeonLoot(new ItemStack(Item.RECORD_13)));
		}

		for(j2 = 0; j2 < 5; ++j2) {
			dngItems.add(new DungeonLoot(new ItemStack(Item.RECORD_CAT)));
		}

	}

	public static ItemStack dungeonGetRandomItem() {
		if (!dngAddedItems) {
			dungeonAddDefaultItems();
			dngAddedItems = true;
		}

		return dngItems.isEmpty() ? null : ((DungeonLoot)dngItems.get((new Random()).nextInt(dngItems.size()))).getStack();
	}

	public static void acPageAdd(AchievementPage acpage) {
		ACHIEVEMENT_PAGES.add(acpage);
	}

	public static void acHide(Achievement[] aachievement) {
		Achievement[] aachievement1 = aachievement;
		int j = aachievement.length;

		for(int i = 0; i < j; ++i) {
			Achievement achievement = aachievement1[i];
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
			Iterator iterator = ACHIEVEMENT_PAGES.iterator();

			AchievementPage acpage;
			do {
				if (!iterator.hasNext()) {
					return acDefaultPage;
				}

				acpage = (AchievementPage)iterator.next();
			} while(!acpage.list.contains(achievement.id));

			return acpage;
		}
	}

	public static AchievementPage acGetCurrentPage() {
		return (AchievementPage) ACHIEVEMENT_PAGES.get(acCurrentPage);
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
		--acCurrentPage;
		if (acCurrentPage < 0) {
			acCurrentPage = ACHIEVEMENT_PAGES.size() - 1;
		}

	}

	static {
		PlayerAPI.RegisterPlayerBase(PlayerBaseSAPI.class);
		usingText = false;
		harvestIntercepts = new ArrayList();
		setIntercepts = new ArrayList();
		reaches = new ArrayList();
		dngMobs = new ArrayList();
		dngItems = new ArrayList();
		dngGuaranteed = new ArrayList();
		dngAddedMobs = false;
		dngAddedItems = false;
		acCurrentPage = 0;
		acHidden = new ArrayList();
		ACHIEVEMENT_PAGES = new ArrayList();
		acDefaultPage = new AchievementPage();
		showText();
	}
}
