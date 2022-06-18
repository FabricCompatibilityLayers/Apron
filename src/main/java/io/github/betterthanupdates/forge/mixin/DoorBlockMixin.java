package io.github.betterthanupdates.forge.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.block.Block;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;

import io.github.betterthanupdates.forge.world.ForgeWorld;

@Mixin(DoorBlock.class)
public abstract class DoorBlockMixin extends Block {
	@Shadow
	public abstract void method_837(World arg, int i, int j, int k, boolean bl);

	protected DoorBlockMixin(int i, Material arg) {
		super(i, arg);
	}

	/**
	 * @author Forge
	 * @reason
	 */
	@Overwrite
	public void onAdjacentBlockUpdate(World world, int i, int j, int k, int l) {
		int i1 = world.getBlockMeta(i, j, k);

		if ((i1 & 8) != 0) {
			if (world.getBlockId(i, j - 1, k) != this.id) {
				world.setBlock(i, j, k, 0);
			}

			if (l > 0 && Block.BY_ID[l].getEmitsRedstonePower()) {
				this.onAdjacentBlockUpdate(world, i, j - 1, k, l);
			}
		} else {
			boolean flag = false;

			if (world.getBlockId(i, j + 1, k) != this.id) {
				world.setBlock(i, j, k, 0);
				flag = true;
			}

			if (!((ForgeWorld) world).isBlockSolidOnSide(i, j - 1, k, 1)) {
				world.setBlock(i, j, k, 0);
				flag = true;

				if (world.getBlockId(i, j + 1, k) == this.id) {
					world.setBlock(i, j + 1, k, 0);
				}
			}

			if (flag) {
				if (!world.isClient) {
					this.drop(world, i, j, k, i1);
				}
			} else if (l > 0 && Block.BY_ID[l].getEmitsRedstonePower()) {
				boolean flag1 = world.hasRedstonePower(i, j, k) || world.hasRedstonePower(i, j + 1, k);
				this.method_837(world, i, j, k, flag1);
			}
		}
	}

	/**
	 * @author Forge
	 * @reason
	 */
	@Overwrite
	public boolean canPlaceAt(World world, int i, int j, int k) {
		if (j >= 127) {
			return false;
		} else {
			return ((ForgeWorld) world).isBlockSolidOnSide(i, j - 1, k, 1) && super.canPlaceAt(world, i, j, k) && super.canPlaceAt(world, i, j + 1, k);
		}
	}
}
