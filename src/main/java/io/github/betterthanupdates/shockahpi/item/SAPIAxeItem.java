package io.github.betterthanupdates.shockahpi.item;

import net.minecraft.block.Block;
import net.minecraft.item.ToolMaterial;

public class SAPIAxeItem
		extends SAPIToolItem {
	private static Block[] field_1681 = new Block[]{Block.WOOD, Block.BOOKSHELF, Block.LOG, Block.CHEST};

	public SAPIAxeItem(int i, ToolMaterial arg) {
		super(i, 3, arg, field_1681);
	}
}
