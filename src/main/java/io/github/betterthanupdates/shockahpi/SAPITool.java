package io.github.betterthanupdates.shockahpi;

import net.minecraft.block.Block;

public interface SAPITool {
	float getPower();

	boolean canHarvest(Block block);
}
