/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.1.
 */

package forge;

import java.util.LinkedList;

import net.legacyfabric.fabric.api.logger.v1.Logger;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import io.github.betterthanupdates.forge.item.ForgeTool;
import io.github.betterthanupdates.forge.item.ToolEffectiveness;

@SuppressWarnings("unused")
public class MinecraftForge {
	static final Logger LOGGER = Logger.get("Babricated Forge", "Minecraft Forge");

	private static final LinkedList<IBucketHandler> bucketHandlers = new LinkedList<>();

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

	public static void setToolClass(Item tool, String toolClass, int harvestLevel) {
		ForgeHooks.initTools();
		ForgeHooks.toolClasses.put(tool.id, new ForgeTool(toolClass, harvestLevel));
	}

	public static void setBlockHarvestLevel(Block block, int meta, String toolClass, int harvestLevel) {
		ForgeHooks.initTools();
		ToolEffectiveness key = new ToolEffectiveness(block.id, meta, toolClass);
		ForgeHooks.toolHarvestLevels.put(key, harvestLevel);
		ForgeHooks.toolEffectiveness.add(key);
	}

	public static void removeBlockEffectiveness(Block block, int meta, String toolClass) {
		ForgeHooks.initTools();
		ToolEffectiveness key = new ToolEffectiveness(block.id, meta, toolClass);
		ForgeHooks.toolEffectiveness.remove(key);
	}

	public static void setBlockHarvestLevel(Block block, String toolClass, int harvestLevel) {
		ForgeHooks.initTools();

		for (int meta = 0; meta < 16; ++meta) {
			ToolEffectiveness key = new ToolEffectiveness(block.id, meta, toolClass);
			ForgeHooks.toolHarvestLevels.put(key, harvestLevel);
			ForgeHooks.toolEffectiveness.add(key);
		}
	}

	public static void removeBlockEffectiveness(Block block, String toolClass) {
		ForgeHooks.initTools();

		for (int meta = 0; meta < 16; ++meta) {
			ToolEffectiveness key = new ToolEffectiveness(block.id, meta, toolClass);
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
				LOGGER.warn(modName + ": MinecraftForge minor version mismatch, expecting " + major + "." + minor + ".x, may lead to unexpected behavior");
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
