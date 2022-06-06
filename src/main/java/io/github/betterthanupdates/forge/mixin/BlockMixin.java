package io.github.betterthanupdates.forge.mixin;

import io.github.betterthanupdates.forge.ForgeBlock;

public abstract class BlockMixin implements ForgeBlock {
	public float getHardness(int meta) {
		return 0f;
	}
}
