package io.github.betterthanupdates.shockahpi.item;

import net.minecraft.block.Block;
import net.minecraft.item.ToolMaterial;

public class ShockAhPIShovelItem extends ShockAhPIToolItem {
	private static Block[] field_2088 = new Block[] {Block.GRASS, Block.DIRT, Block.SAND, Block.GRAVEL, Block.SNOW, Block.SNOW_BLOCK, Block.CLAY, Block.FARMLAND};

	public ShockAhPIShovelItem(int i, ToolMaterial arg) {
		super(i, 1, arg, field_2088);
	}

	@Override
	public boolean isEffectiveOn(Block arg) {
		if (arg == Block.SNOW) {
			return true;
		}

		return arg == Block.SNOW_BLOCK;
	}
}
