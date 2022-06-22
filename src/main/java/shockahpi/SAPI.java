package shockahpi;

import java.lang.reflect.Field;
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
import net.minecraft.client.Minecraft;
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
public class SAPI {
	private static Minecraft instance;
	public static boolean usingText = false;
	private static ArrayList harvestIntercepts = new ArrayList();
	private static ArrayList setIntercepts = new ArrayList();
	private static ArrayList reaches = new ArrayList();
	private static ArrayList dngMobs = new ArrayList();
	private static ArrayList dngItems = new ArrayList();
	private static ArrayList dngGuaranteed = new ArrayList();
	private static boolean dngAddedMobs = false;
	private static boolean dngAddedItems = false;
	public static int acCurrentPage = 0;
	private static ArrayList acHidden = new ArrayList();
	private static ArrayList acPages = new ArrayList();
	public static final ACPage acDefaultPage = new ACPage();

	public SAPI() {
	}

	public static void showText() {
		if (!usingText) {
			System.out.println("Using ShockAhPI r5.1");
			usingText = true;
		}

	}

	public static Minecraft getMinecraftInstance() {
		if (instance == null) {
			try {
				ThreadGroup threadgroup = Thread.currentThread().getThreadGroup();
				int i = threadgroup.activeCount();
				Thread[] athread = new Thread[i];
				threadgroup.enumerate(athread);

				for(int j = 0; j < athread.length; ++j) {
					if (athread[j].getName().equals("Minecraft main thread")) {
						Field field = Thread.class.getDeclaredField("target");
						field.setAccessible(true);
						instance = (Minecraft)field.get(athread[j]);
						break;
					}
				}
			} catch (Exception var5) {
				var5.printStackTrace();
			}
		}

		return instance;
	}

	public static void interceptAdd(IInterceptHarvest iinterceptharvest) {
		harvestIntercepts.add(iinterceptharvest);
	}

	public static boolean interceptHarvest(World world, PlayerEntity entityplayer, Loc loc, int i, int j) {
		for(IInterceptHarvest iinterceptharvest : harvestIntercepts) {
			if (iinterceptharvest.canIntercept(world, entityplayer, loc, i, j)) {
				iinterceptharvest.intercept(world, entityplayer, loc, i, j);
				return true;
			}
		}

		return false;
	}

	public static void drop(World world, Loc loc, ItemStack itemstack) {
		if (!world.isClient) {
			for(int i = 0; i < itemstack.count; ++i) {
				float f = 0.7F;
				double d = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5;
				double d1 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5;
				double d2 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5;
				ItemEntity entityitem = new ItemEntity(
						world, (double)loc.x() + d, (double)loc.y() + d1, (double)loc.z() + d2, new ItemStack(itemstack.itemId, 1, itemstack.getMeta())
				);
				entityitem.pickupDelay = 10;
				world.spawnEntity(entityitem);
			}

		}
	}

	public static void interceptAdd(IInterceptBlockSet iinterceptblockset) {
		setIntercepts.add(iinterceptblockset);
	}

	public static int interceptBlockSet(World world, Loc loc, int i) {
		for(IInterceptBlockSet iinterceptblockset : setIntercepts) {
			if (iinterceptblockset.canIntercept(world, loc, i)) {
				return iinterceptblockset.intercept(world, loc, i);
			}
		}

		return i;
	}

	public static void reachAdd(IReach ireach) {
		reaches.add(ireach);
	}

	public static float reachGet() {
		ItemStack itemstack = getMinecraftInstance().player.inventory.getHeldItem();

		for(IReach ireach : reaches) {
			if (ireach.reachItemMatches(itemstack)) {
				return ireach.getReach(itemstack);
			}
		}

		return 4.0F;
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
		for(int i = 0; i < 10; ++i) {
			dngMobs.add("Skeleton");
		}

		for(int j = 0; j < 20; ++j) {
			dngMobs.add("Zombie");
		}

		for(int k = 0; k < 10; ++k) {
			dngMobs.add("Spider");
		}

	}

	public static String dungeonGetRandomMob() {
		if (!dngAddedMobs) {
			dungeonAddDefaultMobs();
			dngAddedMobs = true;
		}

		return dngMobs.isEmpty() ? "Pig" : (String)dngMobs.get(new Random().nextInt(dngMobs.size()));
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
		for(int j = 0; j < dngItems.size(); ++j) {
			if (((DungeonLoot)dngItems.get(j)).loot.itemId == i) {
				dngItems.remove(j);
				--j;
			}
		}

		for(int k = 0; k < dngGuaranteed.size(); ++k) {
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
		for(int i = 0; i < 100; ++i) {
			dngItems.add(new DungeonLoot(new ItemStack(Item.SADDLE)));
		}

		for(int j = 0; j < 100; ++j) {
			dngItems.add(new DungeonLoot(new ItemStack(Item.IRON_INGOT), 1, 4));
		}

		for(int k = 0; k < 100; ++k) {
			dngItems.add(new DungeonLoot(new ItemStack(Item.BREAD)));
		}

		for(int l = 0; l < 100; ++l) {
			dngItems.add(new DungeonLoot(new ItemStack(Item.WHEAT), 1, 4));
		}

		for(int i1 = 0; i1 < 100; ++i1) {
			dngItems.add(new DungeonLoot(new ItemStack(Item.GUNPOWDER), 1, 4));
		}

		for(int j1 = 0; j1 < 100; ++j1) {
			dngItems.add(new DungeonLoot(new ItemStack(Item.STRING), 1, 4));
		}

		for(int k1 = 0; k1 < 100; ++k1) {
			dngItems.add(new DungeonLoot(new ItemStack(Item.BUCKET)));
		}

		dngItems.add(new DungeonLoot(new ItemStack(Item.GOLDEN_APPLE)));

		for(int l1 = 0; l1 < 50; ++l1) {
			dngItems.add(new DungeonLoot(new ItemStack(Item.REDSTONE_DUST), 1, 4));
		}

		for(int i2 = 0; i2 < 5; ++i2) {
			dngItems.add(new DungeonLoot(new ItemStack(Item.RECORD_13)));
		}

		for(int j2 = 0; j2 < 5; ++j2) {
			dngItems.add(new DungeonLoot(new ItemStack(Item.RECORD_CAT)));
		}

	}

	public static ItemStack dungeonGetRandomItem() {
		if (!dngAddedItems) {
			dungeonAddDefaultItems();
			dngAddedItems = true;
		}

		return dngItems.isEmpty() ? null : ((DungeonLoot)dngItems.get(new Random().nextInt(dngItems.size()))).getStack();
	}

	public static void acPageAdd(ACPage acpage) {
		acPages.add(acpage);
	}

	public static void acHide(Achievement[] aachievement) {
		for(Achievement achievement : aachievement) {
			acHidden.add(achievement.id);
		}

	}

	public static boolean acIsHidden(Achievement achievement) {
		return acHidden.contains(achievement.id);
	}

	public static ACPage acGetPage(Achievement achievement) {
		if (achievement == null) {
			return null;
		} else {
			for(ACPage acpage : acPages) {
				if (acpage.list.contains(achievement.id)) {
					return acpage;
				}
			}

			return acDefaultPage;
		}
	}

	public static ACPage acGetCurrentPage() {
		return (ACPage)acPages.get(acCurrentPage);
	}

	public static String acGetCurrentPageTitle() {
		return acGetCurrentPage().title;
	}

	public static void acPageNext() {
		++acCurrentPage;
		if (acCurrentPage > acPages.size() - 1) {
			acCurrentPage = 0;
		}

	}

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
