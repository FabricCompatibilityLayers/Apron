package io.github.betterthanupdates.apron.compat.betterthanwolves;

import net.minecraft.block.Block;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public interface BTWFireBlock {
	void OnBlockDestroyedByFire(World world, int i, int j, int k);
	boolean RenderFire(BlockRenderer renderBlocks, BlockView iBlockAccess, int i, int j, int k, Block block);
}
