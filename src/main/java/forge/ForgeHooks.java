/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.1.
 */

package forge;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SleepStatus;

import io.github.betterthanupdates.Legacy;
import io.github.betterthanupdates.apron.block.ApronBlock;
import io.github.betterthanupdates.forge.entity.player.ForgePlayerEntity;
import io.github.betterthanupdates.forge.item.ForgeTool;
import io.github.betterthanupdates.forge.item.ToolEffectiveness;

@SuppressWarnings("unused")
@Legacy
public class ForgeHooks {
	static LinkedList<ICraftingHandler> craftingHandlers = new LinkedList<>();
	static LinkedList<IDestroyToolHandler> destroyToolHandlers = new LinkedList<>();
	static LinkedList<ISleepHandler> sleepHandlers = new LinkedList<>();
	public static final int majorVersion = 1;
	public static final int minorVersion = 0;
	public static final int revisionVersion = 6;
	static boolean toolInit = false;
	static HashMap<Integer, ForgeTool> toolClasses = new HashMap<>();
	static HashMap<ToolEffectiveness, Integer> toolHarvestLevels = new HashMap<>();
	static HashSet<ToolEffectiveness> toolEffectiveness = new HashSet<>();

	public ForgeHooks() {
	}

	public static void onTakenFromCrafting(PlayerEntity player, ItemStack itemStack, Inventory inventory) {
		for (ICraftingHandler handler : craftingHandlers) {
			handler.onTakenFromCrafting(player, itemStack, inventory);
		}
	}

	public static void onDestroyCurrentItem(PlayerEntity player, ItemStack itemStack) {
		for (IDestroyToolHandler handler : destroyToolHandlers) {
			handler.onDestroyCurrentItem(player, itemStack);
		}
	}

	public static SleepStatus sleepInBedAt(PlayerEntity player, int x, int y, int z) {
		for (ISleepHandler handler : sleepHandlers) {
			SleepStatus status = handler.sleepInBedAt(player, x, y, z);

			if (status != null) {
				return status;
			}
		}

		return null;
	}

	public static boolean canHarvestBlock(Block block, PlayerEntity player, int meta) {
		if (block.material.doesRequireTool()) {
			return true;
		} else {
			ItemStack itemstack = player.inventory.getHeldItem();
			return itemstack != null && canToolHarvestBlock(block, meta, itemstack);
		}
	}

	public static boolean canToolHarvestBlock(Block block, int meta, ItemStack itemStack) {
		ForgeTool forgeTool = toolClasses.get(itemStack.itemId);

		if (forgeTool == null) {
			return itemStack.isEffectiveOn(block);
		} else {
			String toolType = forgeTool.toolType;
			int harvestLevel = forgeTool.harvestLevel;

			if (toolType.equalsIgnoreCase("paxel")) {
				return true;
			} else {
				Integer blockHarvestLevel = toolHarvestLevels.get(new ToolEffectiveness(block.id, meta, toolType));

				if (blockHarvestLevel == null) {
					return itemStack.isEffectiveOn(block);
				} else {
					return blockHarvestLevel <= harvestLevel && itemStack.isEffectiveOn(block);
				}
			}
		}
	}

	public static float blockStrength(Block block, PlayerEntity player, int meta) {
		float blockHardness = ((ApronBlock) block).getHardness(meta);

		if (blockHardness < 0.0F) {
			return 0.0F;
		} else {
			return !canHarvestBlock(block, player, meta) ? 1.0F / blockHardness / 100.0F
					: ((ForgePlayerEntity) player).getCurrentPlayerStrVsBlock(block, meta) / blockHardness / 30.0F;
		}
	}

	public static boolean isToolEffective(ItemStack tool, Block block, int meta) {
		ForgeTool forgeTool = toolClasses.get(tool.itemId);

		if (forgeTool == null) {
			return false;
		} else {
			String toolType = forgeTool.toolType;
			return toolType.equalsIgnoreCase("paxel") || toolEffectiveness.contains(new ToolEffectiveness(block.id, meta, toolType));
		}
	}

	static void initTools() {
		if (!toolInit) {
			toolInit = true;
			MinecraftForge.setToolClass(Item.WOOD_PICKAXE, "pickaxe", 0);
			MinecraftForge.setToolClass(Item.STONE_PICKAXE, "pickaxe", 1);
			MinecraftForge.setToolClass(Item.IRON_PICKAXE, "pickaxe", 2);
			MinecraftForge.setToolClass(Item.GOLD_PICKAXE, "pickaxe", 0);
			MinecraftForge.setToolClass(Item.DIAMOND_PICKAXE, "pickaxe", 3);
			MinecraftForge.setToolClass(Item.WOOD_AXE, "axe", 0);
			MinecraftForge.setToolClass(Item.STONE_AXE, "axe", 1);
			MinecraftForge.setToolClass(Item.IRON_AXE, "axe", 2);
			MinecraftForge.setToolClass(Item.GOLD_AXE, "axe", 0);
			MinecraftForge.setToolClass(Item.DIAMOND_AXE, "axe", 3);
			MinecraftForge.setToolClass(Item.WOOD_SHOVEL, "shovel", 0);
			MinecraftForge.setToolClass(Item.STONE_SHOVEL, "shovel", 1);
			MinecraftForge.setToolClass(Item.IRON_SHOVEL, "shovel", 2);
			MinecraftForge.setToolClass(Item.GOLD_SHOVEL, "shovel", 0);
			MinecraftForge.setToolClass(Item.DIAMOND_SHOVEL, "shovel", 3);
			MinecraftForge.setBlockHarvestLevel(Block.OBSIDIAN, "pickaxe", 3);
			MinecraftForge.setBlockHarvestLevel(Block.DIAMOND_ORE, "pickaxe", 2);
			MinecraftForge.setBlockHarvestLevel(Block.DIAMOND_BLOCK, "pickaxe", 2);
			MinecraftForge.setBlockHarvestLevel(Block.GOLD_ORE, "pickaxe", 2);
			MinecraftForge.setBlockHarvestLevel(Block.GOLD_BLOCK, "pickaxe", 2);
			MinecraftForge.setBlockHarvestLevel(Block.IRON_ORE, "pickaxe", 1);
			MinecraftForge.setBlockHarvestLevel(Block.IRON_BLOCK, "pickaxe", 1);
			MinecraftForge.setBlockHarvestLevel(Block.LAPIS_LAZULI_ORE, "pickaxe", 1);
			MinecraftForge.setBlockHarvestLevel(Block.LAPIS_LAZULI_BLOCK, "pickaxe", 1);
			MinecraftForge.setBlockHarvestLevel(Block.REDSTONE_ORE, "pickaxe", 2);
			MinecraftForge.setBlockHarvestLevel(Block.REDSTONE_ORE_LIT, "pickaxe", 2);
			MinecraftForge.removeBlockEffectiveness(Block.REDSTONE_ORE, "pickaxe");
			MinecraftForge.removeBlockEffectiveness(Block.REDSTONE_ORE_LIT, "pickaxe");

			Block[] pickaxeEffectiveOn = new Block[] {
				Block.COBBLESTONE,
				Block.DOUBLE_STONE_SLAB,
				Block.STONE_SLAB,
				Block.STONE,
				Block.SANDSTONE,
				Block.MOSSY_COBBLESTONE,
				Block.IRON_ORE,
				Block.IRON_BLOCK,
				Block.COAL_ORE,
				Block.GOLD_BLOCK,
				Block.GOLD_ORE,
				Block.DIAMOND_ORE,
				Block.DIAMOND_BLOCK,
				Block.ICE,
				Block.NETHERRACK,
				Block.LAPIS_LAZULI_ORE,
				Block.LAPIS_LAZULI_BLOCK
			};

			for (Block block : pickaxeEffectiveOn) {
				MinecraftForge.setBlockHarvestLevel(block, "pickaxe", 0);
			}
		}
	}

	public static void touch() {
	}

	static {
		MinecraftForge.LOGGER.info("MinecraftForge V%d.%d.%d Initialized", majorVersion, minorVersion, revisionVersion);
	}
}
