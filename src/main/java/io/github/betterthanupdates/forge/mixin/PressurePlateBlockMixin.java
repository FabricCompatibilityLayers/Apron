package io.github.betterthanupdates.forge.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.PressurePlateBlock;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(PressurePlateBlock.class)
public class PressurePlateBlockMixin extends Block {
	protected PressurePlateBlockMixin(int i, Material arg) {
		super(i, arg);
	}

	/**
	 * @author Forge
	 */
	@Overwrite
	public boolean canPlaceAt(World world, int i, int j, int k) {
		return world.isBlockSolidOnSide(i, j - 1, k, 1);
	}

	/**
	 * @author Forge
	 */
	@Overwrite
	public void onAdjacentBlockUpdate(World world, int i, int j, int k, int l) {
		boolean flag = !world.isBlockSolidOnSide(i, j - 1, k, 1);

		if (flag) {
			this.drop(world, i, j, k, world.getBlockMeta(i, j, k));
			world.setBlock(i, j, k, 0);
		}

	}
}
