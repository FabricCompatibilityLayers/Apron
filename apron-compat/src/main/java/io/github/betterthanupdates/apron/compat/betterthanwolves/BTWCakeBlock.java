package io.github.betterthanupdates.apron.compat.betterthanwolves;

import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public interface BTWCakeBlock {
	boolean IsRedstoneOn(BlockView iBlockAccess, int i, int j, int k);
	void SetRedstoneOn(World world, int i, int j, int k, boolean bOn);
	int GetEatState(BlockView iBlockAccess, int i, int j, int k);
	void SetEatState(World world, int i, int j, int k, int state);
}
