package io.github.betterthanupdates.forge;

import net.minecraft.block.Block;

public interface ForgePlayerEntity {
	float getCurrentPlayerStrVsBlock(Block block, int meta);
}
