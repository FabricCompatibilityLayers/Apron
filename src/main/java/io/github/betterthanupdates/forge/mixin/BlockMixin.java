package io.github.betterthanupdates.forge.mixin;

import io.github.betterthanupdates.forge.block.ForgeBlock;
import net.minecraft.block.Block;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Block.class)
public abstract class BlockMixin implements ForgeBlock {
	@Override
	public float getHardness(int meta) {
		return 0f;
	}
}
