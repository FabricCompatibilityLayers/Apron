package io.github.betterthanupdates.forge.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.block.Block;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;

import io.github.betterthanupdates.forge.ForgeReflection;
import io.github.betterthanupdates.forge.world.ForgeWorld;

@Mixin(TrapdoorBlock.class)
public abstract class TrapdoorBlockMixin extends Block {
	@Shadow
	public abstract void method_1059(World world, int x, int y, int z, boolean bl);

	protected TrapdoorBlockMixin(int blockId, Material material) {
		super(blockId, material);
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Overwrite
	public void onAdjacentBlockUpdate(World world, int x, int y, int z, int side) {
		if (!world.isClient) {
			int i1 = world.getBlockMeta(x, y, z);
			int j1 = x;
			int k1 = z;

			if ((i1 & 3) == 0) {
				k1 = z + 1;
			}

			if ((i1 & 3) == 1) {
				--k1;
			}

			if ((i1 & 3) == 2) {
				j1 = x + 1;
			}

			if ((i1 & 3) == 3) {
				--j1;
			}

			if (!ForgeReflection.TrapdoorBlock$disableValidation && !((ForgeWorld) world).isBlockSolidOnSide(j1, y, k1, (i1 & 3) + 2)) {
				world.setBlock(x, y, z, 0);
				this.drop(world, x, y, z, i1);
			}

			if (side > 0 && Block.BY_ID[side].getEmitsRedstonePower()) {
				boolean flag = world.hasRedstonePower(x, y, z);
				this.method_1059(world, x, y, z, flag);
			}
		}
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Overwrite
	public boolean canPlaceAt(World world, int i, int j, int k, int l) {
		if (ForgeReflection.TrapdoorBlock$disableValidation) {
			return true;
		} else if (l == 0) {
			return false;
		} else if (l == 1) {
			return false;
		} else {
			if (l == 2) {
				++k;
			}

			if (l == 3) {
				--k;
			}

			if (l == 4) {
				++i;
			}

			if (l == 5) {
				--i;
			}

			return ((ForgeWorld) world).isBlockSolidOnSide(i, j, k, l);
		}
	}
}
