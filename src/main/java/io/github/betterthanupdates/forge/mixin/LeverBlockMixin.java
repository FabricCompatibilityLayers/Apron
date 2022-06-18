package io.github.betterthanupdates.forge.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.block.Block;
import net.minecraft.block.LeverBlock;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;

import io.github.betterthanupdates.forge.world.ForgeWorld;

@Mixin(LeverBlock.class)
public abstract class LeverBlockMixin extends Block {
	@Shadow
	protected abstract boolean method_1785(World arg, int i, int j, int k);

	protected LeverBlockMixin(int i, Material arg) {
		super(i, arg);
	}

	/**
	 * @author Forge
	 * @reason
	 */
	@Overwrite
	public boolean canPlaceAt(World world, int i, int j, int k, int l) {
		if (l == 1 && ((ForgeWorld) world).isBlockSolidOnSide(i, j - 1, k, 1)) {
			return true;
		} else if (l == 2 && ((ForgeWorld) world).isBlockSolidOnSide(i, j, k + 1, 2)) {
			return true;
		} else if (l == 3 && ((ForgeWorld) world).isBlockSolidOnSide(i, j, k - 1, 3)) {
			return true;
		} else if (l == 4 && ((ForgeWorld) world).isBlockSolidOnSide(i + 1, j, k, 4)) {
			return true;
		} else {
			return l == 5 && ((ForgeWorld) world).isBlockSolidOnSide(i - 1, j, k, 5);
		}
	}

	/**
	 * @author Forge
	 * @reason
	 */
	@Overwrite
	public boolean canPlaceAt(World world, int i, int j, int k) {
		if (((ForgeWorld) world).isBlockSolidOnSide(i - 1, j, k, 5)) {
			return true;
		} else if (((ForgeWorld) world).isBlockSolidOnSide(i + 1, j, k, 4)) {
			return true;
		} else if (((ForgeWorld) world).isBlockSolidOnSide(i, j, k - 1, 3)) {
			return true;
		} else {
			return ((ForgeWorld) world).isBlockSolidOnSide(i, j, k + 1, 2) ? true : ((ForgeWorld) world).isBlockSolidOnSide(i, j - 1, k, 1);
		}
	}

	/**
	 * @author Forge
	 * @reason
	 */
	@Overwrite
	public void onBlockPlaced(World world, int i, int j, int k, int l) {
		int i1 = world.getBlockMeta(i, j, k);
		int j1 = i1 & 8;
		i1 &= 7;
		i1 = -1;
		if (l == 1 && ((ForgeWorld) world).isBlockSolidOnSide(i, j - 1, k, 1)) {
			i1 = 5 + world.rand.nextInt(2);
		}

		if (l == 2 && ((ForgeWorld) world).isBlockSolidOnSide(i, j, k + 1, 2)) {
			i1 = 4;
		}

		if (l == 3 && ((ForgeWorld) world).isBlockSolidOnSide(i, j, k - 1, 3)) {
			i1 = 3;
		}

		if (l == 4 && ((ForgeWorld) world).isBlockSolidOnSide(i + 1, j, k, 4)) {
			i1 = 2;
		}

		if (l == 5 && ((ForgeWorld) world).isBlockSolidOnSide(i - 1, j, k, 5)) {
			i1 = 1;
		}

		if (i1 == -1) {
			this.drop(world, i, j, k, world.getBlockMeta(i, j, k));
			world.setBlock(i, j, k, 0);
		} else {
			world.setBlockMeta(i, j, k, i1 + j1);
		}
	}

	/**
	 * @author Forge
	 * @reason
	 */
	@Overwrite
	public void onAdjacentBlockUpdate(World world, int i, int j, int k, int l) {
		if (this.method_1785(world, i, j, k)) {
			int i1 = world.getBlockMeta(i, j, k) & 7;
			boolean flag = false;
			if (!((ForgeWorld) world).isBlockSolidOnSide(i - 1, j, k, 5) && i1 == 1) {
				flag = true;
			}

			if (!((ForgeWorld) world).isBlockSolidOnSide(i + 1, j, k, 4) && i1 == 2) {
				flag = true;
			}

			if (!((ForgeWorld) world).isBlockSolidOnSide(i, j, k - 1, 3) && i1 == 3) {
				flag = true;
			}

			if (!((ForgeWorld) world).isBlockSolidOnSide(i, j, k + 1, 2) && i1 == 4) {
				flag = true;
			}

			if (!((ForgeWorld) world).isBlockSolidOnSide(i, j - 1, k, 1) && i1 == 5) {
				flag = true;
			}

			if (!((ForgeWorld) world).isBlockSolidOnSide(i, j - 1, k, 1) && i1 == 6) {
				flag = true;
			}

			if (flag) {
				this.drop(world, i, j, k, world.getBlockMeta(i, j, k));
				world.setBlock(i, j, k, 0);
			}
		}

	}
}
