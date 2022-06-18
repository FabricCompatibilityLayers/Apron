/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.1.
 */
package forge;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MinecraftForge {
	private static final LinkedList<IBucketHandler> bucketHandlers = new LinkedList<>();

	public MinecraftForge() {
	}

	/**
	 * @deprecated
	 */
	public static void registerCustomBucketHander(IBucketHandler handler) {
		bucketHandlers.add(handler);
	}

	public static void registerCustomBucketHandler(IBucketHandler handler) {
		bucketHandlers.add(handler);
	}

	public static void registerSleepHandler(ISleepHandler handler) {
		ForgeHooks.sleepHandlers.add(handler);
	}

	public static void registerDestroyToolHandler(IDestroyToolHandler handler) {
		ForgeHooks.destroyToolHandlers.add(handler);
	}

	public static void registerCraftingHandler(ICraftingHandler handler) {
		ForgeHooks.craftingHandlers.add(handler);
	}

	public static ItemStack fillCustomBucket(World world, int i, int j, int k) {
		for (IBucketHandler handler : bucketHandlers) {
			ItemStack stack = handler.fillCustomBucket(world, i, j, k);
			if (stack != null) {
				return stack;
			}
		}

		return null;
	}

	public static void setToolClass(Item tool, String tClass, int hLevel) {
		ForgeHooks.initTools();
		ForgeHooks.toolClasses.put(tool.id, Arrays.asList(tClass, hLevel));
	}

	public static void setBlockHarvestLevel(Block bl, int md, String tClass, int hLevel) {
		ForgeHooks.initTools();
		List key = Arrays.asList(bl.id, md, tClass);
		ForgeHooks.toolHarvestLevels.put(key, hLevel);
		ForgeHooks.toolEffectiveness.add(key);
	}

	public static void removeBlockEffectiveness(Block bl, int md, String tClass) {
		ForgeHooks.initTools();
		List key = Arrays.asList(bl.id, md, tClass);
		ForgeHooks.toolEffectiveness.remove(key);
	}

	public static void setBlockHarvestLevel(Block bl, String tClass, int hLevel) {
		ForgeHooks.initTools();

		for (int md = 0; md < 16; ++md) {
			List key = Arrays.asList(bl.id, md, tClass);
			ForgeHooks.toolHarvestLevels.put(key, hLevel);
			ForgeHooks.toolEffectiveness.add(key);
		}
	}

	public static void removeBlockEffectiveness(Block bl, String tClass) {
		ForgeHooks.initTools();

		for (int md = 0; md < 16; ++md) {
			List key = Arrays.asList(bl.id, md, tClass);
			ForgeHooks.toolEffectiveness.remove(key);
		}
	}

	public static void addPickaxeBlockEffectiveAgainst(Block block) {
		setBlockHarvestLevel(block, "pickaxe", 0);
	}

	public static void killMinecraft(String modName, String msg) {
		throw new RuntimeException(modName + ": " + msg);
	}

	public static void versionDetect(String modName, int major, int minor, int revision) {
		if (major != 1) {
			killMinecraft(modName, "MinecraftForge Major Version Mismatch, expecting " + major + ".x.x");
		} else if (minor != 0) {
			if (minor > 0) {
				killMinecraft(modName, "MinecraftForge Too Old, need at least " + major + "." + minor + "." + revision);
			} else {
				System.out
						.println(modName + ": MinecraftForge minor version mismatch, expecting " + major + "." + minor + ".x, may lead to unexpected behavior");
			}
		} else if (revision > 6) {
			killMinecraft(modName, "MinecraftForge Too Old, need at least " + major + "." + minor + "." + revision);
		}
	}

	public static void versionDetectStrict(String modName, int major, int minor, int revision) {
		if (major != 1) {
			killMinecraft(modName, "MinecraftForge Major Version Mismatch, expecting " + major + ".x.x");
		} else if (minor != 0) {
			if (minor > 0) {
				killMinecraft(modName, "MinecraftForge Too Old, need at least " + major + "." + minor + "." + revision);
			} else {
				killMinecraft(modName, "MinecraftForge minor version mismatch, expecting " + major + "." + minor + ".x");
			}
		} else if (revision > 6) {
			killMinecraft(modName, "MinecraftForge Too Old, need at least " + major + "." + minor + "." + revision);
		}
	}
}
