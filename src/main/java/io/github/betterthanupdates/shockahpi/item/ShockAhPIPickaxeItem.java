package io.github.betterthanupdates.shockahpi.item;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ToolMaterial;

public class ShockAhPIPickaxeItem extends ShockAhPIToolItem {
	private static Block[] field_352 = new Block[] {Block.COBBLESTONE, Block.DOUBLE_STONE_SLAB, Block.STONE_SLAB, Block.STONE, Block.SANDSTONE, Block.MOSSY_COBBLESTONE, Block.IRON_ORE, Block.IRON_BLOCK, Block.COAL_ORE, Block.GOLD_BLOCK, Block.GOLD_ORE, Block.DIAMOND_ORE, Block.DIAMOND_BLOCK, Block.ICE, Block.NETHERRACK, Block.LAPIS_LAZULI_ORE, Block.LAPIS_LAZULI_BLOCK};

	public ShockAhPIPickaxeItem(int i, ToolMaterial arg) {
		super(i, 2, arg, field_352);
	}

	@Override
	public boolean isEffectiveOn(Block arg) {
		if (arg == Block.OBSIDIAN) {
			return this.field_2711.getMiningLevel() == 3;
		}

		if (arg == Block.DIAMOND_BLOCK || arg == Block.DIAMOND_ORE) {
			return this.field_2711.getMiningLevel() >= 2;
		}

		if (arg == Block.GOLD_BLOCK || arg == Block.GOLD_ORE) {
			return this.field_2711.getMiningLevel() >= 2;
		}

		if (arg == Block.IRON_BLOCK || arg == Block.IRON_ORE) {
			return this.field_2711.getMiningLevel() >= 1;
		}

		if (arg == Block.LAPIS_LAZULI_BLOCK || arg == Block.LAPIS_LAZULI_ORE) {
			return this.field_2711.getMiningLevel() >= 1;
		}

		if (arg == Block.REDSTONE_ORE || arg == Block.REDSTONE_ORE_LIT) {
			return this.field_2711.getMiningLevel() >= 2;
		}

		if (arg.material == Material.STONE) {
			return true;
		}

		return arg.material == Material.METAL;
	}
}
