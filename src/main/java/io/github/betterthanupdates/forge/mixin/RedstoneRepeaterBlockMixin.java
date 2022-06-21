package io.github.betterthanupdates.forge.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.minecraft.block.Block;
import net.minecraft.block.RedstoneRepeaterBlock;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;

import io.github.betterthanupdates.forge.world.ForgeWorld;

@Mixin(RedstoneRepeaterBlock.class)
public class RedstoneRepeaterBlockMixin extends Block {
	protected RedstoneRepeaterBlockMixin(int blockId, Material material) {
		super(blockId, material);
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Overwrite
	public boolean canPlaceAt(World world, int x, int y, int z) {
		return !((ForgeWorld) world).isBlockSolidOnSide(x, y - 1, z, 1) ? false : super.canPlaceAt(world, x, y, z);
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Overwrite
	public boolean canGrow(World world, int x, int y, int z) {
		return !((ForgeWorld) world).isBlockSolidOnSide(x, y - 1, z, 1) ? false : super.canGrow(world, x, y, z);
	}
}
