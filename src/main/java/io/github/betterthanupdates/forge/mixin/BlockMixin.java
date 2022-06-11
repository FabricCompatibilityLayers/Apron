package io.github.betterthanupdates.forge.mixin;

import io.github.betterthanupdates.forge.block.ForgeBlock;
import net.minecraft.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * Default implementation of the new methods provided by Minecraft Forge
 */
@Mixin(Block.class)
public abstract class BlockMixin implements ForgeBlock {
	@Shadow public abstract float getHardness();

	/**
	 * Gets hardness for a block, taking into account its hardness.<br>
	 * By default, this has the same behavior as {@link Block#getHardness()}
	 * @param meta the meta value of the block state
	 * @return the hardness (time to break) of the block
	 */
	@Override
	public float getHardness(int meta) {
		return this.getHardness();
	}
}
