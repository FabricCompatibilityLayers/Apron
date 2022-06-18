/**
 * Minecraft Forge Public Licence
 * <p>
 * ==============================
 * <p>
 * <p>
 * Version 1.1
 * <p>
 * <p>
 * 0. Definitions
 * <p>
 * --------------
 * <p>
 * <p>
 * Minecraft: Denotes a copy of the Minecraft game licensed by Mojang AB
 * <p>
 * <p>
 * User: Anybody that interact with the software in one of the following ways:
 * <p>
 * - play
 * <p>
 * - decompile
 * <p>
 * - recompile or compile
 * <p>
 * - modify
 * <p>
 * <p>
 * Minecraft Forge: The Minecraft Forge code, in source form, class file form, as
 * <p>
 * obtained in a standalone fashion or as part of a wider distribution.
 * <p>
 * <p>
 * Dependency: Code required to have Minecraft Forge working properly. That can
 * <p>
 * include dependencies required to compile the code as well as modifications in
 * <p>
 * the Minecraft sources that are required to have Minecraft Forge working.
 * <p>
 * <p>
 * 1. Scope
 * <p>
 * --------
 * <p>
 * <p>
 * The present license is granted to any user of Minecraft Forge. As a
 * <p>
 * prerequisite, a user of Minecraft Forge must own a legally aquired copy of
 * <p>
 * Minecraft
 * <p>
 * <p>
 * 2. Play rights
 * <p>
 * --------------
 * <p>
 * <p>
 * The user of Minecraft Forge is allowed to install the software on a client or
 * <p>
 * a server and to play it without restriction.
 * <p>
 * <p>
 * 3. Modification rights
 * <p>
 * ----------------------
 * <p>
 * <p>
 * The user has the right to decompile the source code, look at either the
 * <p>
 * decompiled version or the original source code, and to modify it.
 * <p>
 * <p>
 * 4. Derivation rights
 * <p>
 * --------------------
 * <p>
 * <p>
 * The user has the rights to derive code from Minecraft Forge, that is to say to
 * <p>
 * write code that either extends Minecraft Forge class and interfaces,
 * <p>
 * instantiate the objects declared or calls the functions. This code is known as
 * <p>
 * "derived" code, and can be licensed with conditions different from Minecraft
 * <p>
 * Forge.
 * <p>
 * <p>
 * <p>
 * 5. Distribution rights
 * <p>
 * ----------------------
 * <p>
 * <p>
 * The user of Minecraft Forge is allowed to redistribute Minecraft Forge in
 * <p>
 * partially, in totality, or included in a distribution. When distributing
 * <p>
 * binaries or class files, the user must provide means to obtain the sources of
 * <p>
 * the distributed version of Minecraft Forge at no costs. This includes the
 * <p>
 * files as well as any dependency that the code may rely on, including patches to
 * <p>
 * minecraft original sources.
 * <p>
 * <p>
 * Modification of Minecraft Forge as well as dependencies, including patches to
 * <p>
 * minecraft original sources, has to remain under the terms of the present
 * <p>
 * license.
 */
package forge;

import io.github.betterthanupdates.forge.block.ForgeBlock;
import io.github.betterthanupdates.forge.entity.player.ForgePlayerEntity;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SleepStatus;

import java.util.*;

public class ForgeHooks {
	static LinkedList<ICraftingHandler> craftingHandlers = new LinkedList<>();
	static LinkedList<IDestroyToolHandler> destroyToolHandlers = new LinkedList<>();
	static LinkedList<ISleepHandler> sleepHandlers = new LinkedList<>();
	public static final int majorVersion = 1;
	public static final int minorVersion = 0;
	public static final int revisionVersion = 6;
	static boolean toolInit = false;
	static HashMap<Integer, List> toolClasses = new HashMap<>();
	static HashMap<List, Integer> toolHarvestLevels = new HashMap<>();
	static HashSet<List> toolEffectiveness = new HashSet<>();

	public ForgeHooks() {
	}

	public static void onTakenFromCrafting(PlayerEntity player, ItemStack ist, Inventory craftMatrix) {
		for (forge.ICraftingHandler handler : craftingHandlers) {
			handler.onTakenFromCrafting(player, ist, craftMatrix);
		}

	}

	public static void onDestroyCurrentItem(PlayerEntity player, ItemStack orig) {
		for (forge.IDestroyToolHandler handler : destroyToolHandlers) {
			handler.onDestroyCurrentItem(player, orig);
		}

	}

	public static SleepStatus sleepInBedAt(PlayerEntity player, int i, int j, int k) {
		for (forge.ISleepHandler handler : sleepHandlers) {
			SleepStatus status = handler.sleepInBedAt(player, i, j, k);
			if (status != null) {
				return status;
			}
		}

		return null;
	}

	public static boolean canHarvestBlock(Block bl, PlayerEntity player, int md) {
		if (bl.material.doesRequireTool()) {
			return true;
		} else {
			ItemStack itemstack = player.inventory.getHeldItem();
			if (itemstack == null) {
				return false;
			} else {
				List tc = toolClasses.get(itemstack.itemId);
				if (tc == null) {
					return itemstack.isEffectiveOn(bl);
				} else {
					Object[] ta = tc.toArray();
					String cls = (String) ta[0];
					int hvl = (int) ta[1];
					Integer bhl = toolHarvestLevels.get(Arrays.asList(bl.id, md, cls));
					if (bhl == null) {
						return itemstack.isEffectiveOn(bl);
					} else {
						return bhl <= hvl && itemstack.isEffectiveOn(bl);
					}
				}
			}
		}
	}

	public static float blockStrength(Block bl, PlayerEntity player, int md) {
		float bh = ((ForgeBlock) bl).getHardness(md);
		if (bh < 0.0F) {
			return 0.0F;
		} else {
			return !canHarvestBlock(bl, player, md) ? 1.0F / bh / 100.0F : ((ForgePlayerEntity) player).getCurrentPlayerStrVsBlock(bl, md) / bh / 30.0F;
		}
	}

	public static boolean isToolEffective(ItemStack ist, Block bl, int md) {
		List tc = toolClasses.get(ist.itemId);
		if (tc == null) {
			return false;
		} else {
			Object[] ta = tc.toArray();
			String cls = (String) ta[0];
			return toolEffectiveness.contains(Arrays.asList(bl.id, md, cls));
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
			Block[] pickaxeEffective = new Block[]{
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

			for (Block bl : pickaxeEffective) {
				MinecraftForge.setBlockHarvestLevel(bl, "pickaxe", 0);
			}

		}
	}

	static {
		System.out.printf("MinecraftForge V%d.%d.%d Initialized\n", majorVersion, minorVersion, revisionVersion);
	}
}
