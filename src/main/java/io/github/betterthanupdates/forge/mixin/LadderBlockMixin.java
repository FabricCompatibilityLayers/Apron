package io.github.betterthanupdates.forge.mixin;

import io.github.betterthanupdates.forge.block.ForgeBlock;
import net.minecraft.block.Block;
import net.minecraft.block.LadderBlock;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(LadderBlock.class)
public abstract class LadderBlockMixin extends Block implements ForgeBlock {
	protected LadderBlockMixin(int i, Material arg) {
		super(i, arg);
	}

	@Override
	public boolean isLadder() {
		return true;
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
		} else {
			return world.isBlockSolidOnSide(i, j, k - 1, 3) ? true : world.isBlockSolidOnSide(i, j, k + 1, 2);
		}
	}

	/**
	 * @author Forge
	 */
	@Overwrite
	public void onBlockPlaced(World world, int i, int j, int k, int l) {
		int i1 = world.getBlockMeta(i, j, k);
		if ((i1 == 0 || l == 2) && world.isBlockSolidOnSide(i, j, k + 1, 2)) {
			i1 = 2;
		}

		if ((i1 == 0 || l == 3) && world.isBlockSolidOnSide(i, j, k - 1, 3)) {
			i1 = 3;
		}

		if ((i1 == 0 || l == 4) && world.isBlockSolidOnSide(i + 1, j, k, 4)) {
			i1 = 4;
		}

		if ((i1 == 0 || l == 5) && world.isBlockSolidOnSide(i - 1, j, k, 5)) {
			i1 = 5;
		}

		world.setBlockMeta(i, j, k, i1);
	}

	/**
	 * @author Forge
	 */
	@Overwrite
	public void onAdjacentBlockUpdate(World world, int i, int j, int k, int l) {
		int i1 = world.getBlockMeta(i, j, k);
		boolean flag = false;
		if (i1 == 2 && world.isBlockSolidOnSide(i, j, k + 1, 2)) {
			flag = true;
		}

		if (i1 == 3 && world.isBlockSolidOnSide(i, j, k - 1, 3)) {
			flag = true;
		}

		if (i1 == 4 && world.isBlockSolidOnSide(i + 1, j, k, 4)) {
			flag = true;
		}

		if (i1 == 5 && world.isBlockSolidOnSide(i - 1, j, k, 5)) {
			flag = true;
		}

		if (!flag) {
			this.drop(world, i, j, k, i1);
			world.setBlock(i, j, k, 0);
		}

		super.onAdjacentBlockUpdate(world, i, j, k, l);
	}
}
