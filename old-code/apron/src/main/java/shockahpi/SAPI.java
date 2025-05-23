package shockahpi;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.fabricmc.loader.api.FabricLoader;
import net.legacyfabric.fabric.api.logger.v1.Logger;
import org.jetbrains.annotations.ApiStatus;
import org.spongepowered.include.com.google.common.collect.ImmutableList;
import playerapi.PlayerAPI;
import net.minecraft.achievement.Achievement;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import io.github.betterthanupdates.Legacy;
import io.github.betterthanupdates.apron.Apron;
import io.github.betterthanupdates.apron.api.ApronApi;

/**
 * ShockAhPi - Adding new possibilities in 3... 2... 1...
 *
 * @author ShockAh
 */
@SuppressWarnings({"unused", "UnusedReturnValue", "BooleanMethodIsAlwaysInverted"})
@Legacy
public class SAPI {
	public static final Logger LOGGER = Apron.getLogger("ShockAhPI");

	public static boolean usingText = false;
	private static final ArrayList<IInterceptHarvest> harvestIntercepts = new ArrayList<>();
	private static final ArrayList<IInterceptBlockSet> setIntercepts = new ArrayList<>();
	private static final ArrayList<IReach> reaches = new ArrayList<>();
	private static final ArrayList<String> dngMobs = new ArrayList<>();
	private static final ArrayList<DungeonLoot> dngItems = new ArrayList<>();
	private static final ArrayList<DungeonLoot> dngGuaranteed = new ArrayList<>();
	private static boolean dngAddedMobs = false;
	private static boolean dngAddedItems = false;
	public static int acCurrentPage = 0;
	private static final ArrayList<Integer> acHidden = new ArrayList<>();
	private static final ArrayList<AchievementPage> ACHIEVEMENT_PAGES = new ArrayList<>();
	public static final AchievementPage acDefaultPage = new AchievementPage();

	public SAPI() {
	}

	public static void showText() {
		if (!usingText) {
			LOGGER.info("Using ShockAhPI r5.1");
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
		for (IInterceptHarvest iinterceptharvest : harvestIntercepts) {
			if (iinterceptharvest.canIntercept(world, entityplayer, loc, i, j)) {
				iinterceptharvest.intercept(world, entityplayer, loc, i, j);
				return true;
			}
		}

		return false;
	}

	public static void drop(World world, Loc loc, ItemStack itemstack) {
		if (!world.isRemote) {
			for (int i = 0; i < itemstack.count; ++i) {
				float f = 0.7F;
				double d = (double) (world.field_214.nextFloat() * f) + (double) (1.0F - f) * 0.5;
				double d1 = (double) (world.field_214.nextFloat() * f) + (double) (1.0F - f) * 0.5;
				double d2 = (double) (world.field_214.nextFloat() * f) + (double) (1.0F - f) * 0.5;
				ItemEntity entityitem = new ItemEntity(
						world, (double) loc.x() + d, (double) loc.y() + d1, (double) loc.z() + d2, new ItemStack(itemstack.itemId, 1, itemstack.getDamage())
				);
				entityitem.pickupDelay = 10;
				world.method_210(entityitem);
			}
		}
	}

	public static void interceptAdd(IInterceptBlockSet iinterceptblockset) {
		setIntercepts.add(iinterceptblockset);
	}

	public static int interceptBlockSet(World world, Loc loc, int i) {
		for (IInterceptBlockSet iinterceptblockset : setIntercepts) {
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
		ItemStack itemstack = getMinecraftInstance().player.inventory.getSelectedItem();

		for (IReach ireach : reaches) {
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
		for (int j = 0; j < i; ++j) {
			dngMobs.add(s);
		}
	}

	public static void dungeonRemoveMob(String s) {
		for (int i = 0; i < dngMobs.size(); ++i) {
			if (dngMobs.get(i).equals(s)) {
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

	public static String dungeonGetRandomMob() {
		if (!dngAddedMobs) {
			dungeonAddDefaultMobs();
			dngAddedMobs = true;
		}

		return dngMobs.isEmpty() ? "Pig" : dngMobs.get(new Random().nextInt(dngMobs.size()));
	}

	public static void dungeonAddItem(DungeonLoot dungeonloot) {
		dungeonAddItem(dungeonloot, 100);
	}

	public static void dungeonAddItem(DungeonLoot dungeonloot, int i) {
		for (int j = 0; j < i; ++j) {
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
		return dngGuaranteed.get(i);
	}

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

	public static void dungeonRemoveAllItems() {
		dngAddedItems = true;
		dngItems.clear();
		dngGuaranteed.clear();
	}

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
			dngItems.add(new DungeonLoot(new ItemStack(Item.REDSTONE), 1, 4));
		}

		for (int i2 = 0; i2 < 5; ++i2) {
			dngItems.add(new DungeonLoot(new ItemStack(Item.RECORD_THIRTEEN)));
		}

		for (int j2 = 0; j2 < 5; ++j2) {
			dngItems.add(new DungeonLoot(new ItemStack(Item.RECORD_CAT)));
		}
	}

	public static ItemStack dungeonGetRandomItem() {
		if (!dngAddedItems) {
			dungeonAddDefaultItems();
			dngAddedItems = true;
		}

		return dngItems.isEmpty() ? null : dngItems.get(new Random().nextInt(dngItems.size())).getStack();
	}

	public static void acPageAdd(AchievementPage acpage) {
		ACHIEVEMENT_PAGES.add(acpage);
	}

	public static void acHide(Achievement[] achievements) {
		for (Achievement achievement : achievements) {
			acHidden.add(achievement.id);
		}
	}

	public static boolean acIsHidden(Achievement achievement) {
		return acHidden.contains(achievement.id);
	}

	public static AchievementPage acGetPage(Achievement achievement) {
		if (achievement == null) {
			LOGGER.debug("Expected Achievement, got null instead.");
			return null;
		} else {
			for (AchievementPage acpage : ACHIEVEMENT_PAGES) {
				if (acpage.list.contains(achievement.id)) {
					return acpage;
				}
			}

			return acDefaultPage;
		}
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
		--acCurrentPage;

		if (acCurrentPage < 0) {
			acCurrentPage = ACHIEVEMENT_PAGES.size() - 1;
		}
	}

	@ApiStatus.Internal
	public static List<AchievementPage> getPages() {
		return ImmutableList.<AchievementPage>builder().addAll(ACHIEVEMENT_PAGES).build();
	}

	static {
		PlayerAPI.RegisterPlayerBase(PlayerBaseSAPI.class);
		showText();
	}
}
