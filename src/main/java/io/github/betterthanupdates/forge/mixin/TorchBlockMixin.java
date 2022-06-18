package io.github.betterthanupdates.forge.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.TorchBlock;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(TorchBlock.class)
public abstract class TorchBlockMixin extends Block {
	@Shadow
	protected abstract boolean method_1675(World arg, int i, int j, int k);

	protected TorchBlockMixin(int i, Material arg) {
		super(i, arg);
	}

	/**
	 * @author Forge
	 */
	@Overwrite
	private boolean method_1674(World world, int i, int j, int k) {
		return world.isBlockSolidOnSide(i, j, k, 1) || world.getBlockId(i, j, k) == Block.FENCE.id;
	}

	/**
	 * @author Forge
	 */
	@Overwrite
	public boolean canPlaceAt(World world, int i, int j, int k) {
		if (world.isBlockSolidOnSide(i - 1, j, k, 5)) {
			return true;
		} else if (world.isBlockSolidOnSide(i + 1, j, k, 4)) {
			return true;
		} else if (world.isBlockSolidOnSide(i, j, k - 1, 3)) {
			return true;
		} else {
			return world.isBlockSolidOnSide(i, j, k + 1, 2) ? true : this.method_1674(world, i, j - 1, k);
		}
	}

	/**
	 * @author Forge
	 */
	@Overwrite
	public void onBlockPlaced(World world, int i, int j, int k, int l) {
		int i1 = world.getBlockMeta(i, j, k);
		if (l == 1 && this.method_1674(world, i, j - 1, k)) {
			i1 = 5;
		}

		if (l == 2 && world.isBlockSolidOnSide(i, j, k + 1, 2)) {
			i1 = 4;
		}

		if (l == 3 && world.isBlockSolidOnSide(i, j, k - 1, 3)) {
			i1 = 3;
		}

		if (l == 4 && world.isBlockSolidOnSide(i + 1, j, k, 4)) {
			i1 = 2;
		}

		if (l == 5 && world.isBlockSolidOnSide(i - 1, j, k, 5)) {
			i1 = 1;
		}

		world.setBlockMeta(i, j, k, i1);
	}

	/**
	 * @author Forge
	 */
	@Overwrite
	public void onBlockPlaced(World world, int i, int j, int k) {
		if (world.isBlockSolidOnSide(i - 1, j, k, 5)) {
			world.setBlockMeta(i, j, k, 1);
		} else if (world.isBlockSolidOnSide(i + 1, j, k, 4)) {
			world.setBlockMeta(i, j, k, 2);
		} else if (world.isBlockSolidOnSide(i, j, k - 1, 3)) {
			world.setBlockMeta(i, j, k, 3);
		} else if (world.isBlockSolidOnSide(i, j, k + 1, 2)) {
			world.setBlockMeta(i, j, k, 4);
		} else if (this.method_1674(world, i, j - 1, k)) {
			world.setBlockMeta(i, j, k, 5);
		}

		this.method_1675(world, i, j, k);
	}

	/**
	 * @author Forge
	 */
	@Overwrite
	public void onAdjacentBlockUpdate(World world, int i, int j, int k, int l) {
		if (this.method_1675(world, i, j, k)) {
			int i1 = world.getBlockMeta(i, j, k);
			boolean flag = false;
			if (!world.isBlockSolidOnSide(i - 1, j, k, 5) && i1 == 1) {
				flag = true;
			}

			if (!world.isBlockSolidOnSide(i + 1, j, k, 4) && i1 == 2) {
				flag = true;
			}

			if (!world.isBlockSolidOnSide(i, j, k - 1, 3) && i1 == 3) {
				flag = true;
			}

			if (!world.isBlockSolidOnSide(i, j, k + 1, 2) && i1 == 4) {
				flag = true;
			}

			if (!this.method_1674(world, i, j - 1, k) && i1 == 5) {
				flag = true;
			}

			if (flag) {
				this.drop(world, i, j, k, world.getBlockMeta(i, j, k));
				world.setBlock(i, j, k, 0);
			}
		}

	}
}
