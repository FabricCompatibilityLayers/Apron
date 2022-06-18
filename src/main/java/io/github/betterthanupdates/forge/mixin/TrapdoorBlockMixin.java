package io.github.betterthanupdates.forge.mixin;

import io.github.betterthanupdates.forge.ForgeReflection;
import io.github.betterthanupdates.forge.world.ForgeWorld;
import net.minecraft.block.Block;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(TrapdoorBlock.class)
public abstract class TrapdoorBlockMixin extends Block {

	@Shadow
	public abstract void method_1059(World arg, int i, int j, int k, boolean bl);

	protected TrapdoorBlockMixin(int i, Material arg) {
		super(i, arg);
	}

	/**
	 * @author Forge
	 * @reason
	 */
	@Overwrite
	public void onAdjacentBlockUpdate(World world, int i, int j, int k, int l) {
		if (!world.isClient) {
			int i1 = world.getBlockMeta(i, j, k);
			int j1 = i;
			int k1 = k;
			if ((i1 & 3) == 0) {
				k1 = k + 1;
			}

			if ((i1 & 3) == 1) {
				--k1;
			}

			if ((i1 & 3) == 2) {
				j1 = i + 1;
			}

			if ((i1 & 3) == 3) {
				--j1;
			}

			if (!ForgeReflection.TrapdoorBlock$disableValidation && !((ForgeWorld)world).isBlockSolidOnSide(j1, j, k1, (i1 & 3) + 2)) {
				world.setBlock(i, j, k, 0);
				this.drop(world, i, j, k, i1);
			}

			if (l > 0 && Block.BY_ID[l].getEmitsRedstonePower()) {
				boolean flag = world.hasRedstonePower(i, j, k);
				this.method_1059(world, i, j, k, flag);
			}

		}
	}

	/**
	 * @author Forge
	 * @reason
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

			return ((ForgeWorld)world).isBlockSolidOnSide(i, j, k, l);
		}
	}
}
