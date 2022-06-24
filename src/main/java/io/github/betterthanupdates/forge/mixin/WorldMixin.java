package io.github.betterthanupdates.forge.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.block.Block;
import net.minecraft.util.math.AxixAlignedBoundingBox;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import io.github.betterthanupdates.forge.block.ForgeBlock;
import io.github.betterthanupdates.forge.world.ForgeWorld;

@Mixin(World.class)
public abstract class WorldMixin implements BlockView, ForgeWorld {

	@Shadow
	public abstract boolean canSpawnEntity(AxixAlignedBoundingBox box);

	@Shadow
	public abstract boolean method_155(int i, int j, int k, int l, int m, int n);

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Overwrite
	public boolean isAir(int i, int j, int k) {
		int l = this.getBlockId(i, j, k);
		return l == 0 || ((ForgeBlock) Block.BY_ID[l]).isAirBlock((World) (Object) this, i, j, k);
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Overwrite
	public boolean method_225(AxixAlignedBoundingBox axisalignedbb) {
		int i = MathHelper.floor(axisalignedbb.minX);
		int j = MathHelper.floor(axisalignedbb.maxX + 1.0);
		int k = MathHelper.floor(axisalignedbb.minY);
		int l = MathHelper.floor(axisalignedbb.maxY + 1.0);
		int i1 = MathHelper.floor(axisalignedbb.minZ);
		int j1 = MathHelper.floor(axisalignedbb.maxZ + 1.0);
		if (this.method_155(i, k, i1, j, l, j1)) {
			for(int k1 = i; k1 < j; ++k1) {
				for(int l1 = k; l1 < l; ++l1) {
					for(int i2 = i1; i2 < j1; ++i2) {
						int j2 = this.getBlockId(k1, l1, i2);
						if (j2 == Block.FIRE.id || j2 == Block.FLOWING_LAVA.id || j2 == Block.STILL_LAVA.id) {
							return true;
						}

						if (j2 > 0 && ((ForgeBlock) Block.BY_ID[j2]).isBlockBurning((World) (Object) this, k1, l1, i2)) {
							return true;
						}
					}
				}
			}
		}

		return false;
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Overwrite
	public boolean canSuffocate(int i, int j, int k) {
		Block block = Block.BY_ID[this.getBlockId(i, j, k)];
		return block != null && ((ForgeBlock) block).isBlockNormalCube((World) (Object) this, i, j, k);
	}

	@Override
	public boolean isBlockSolidOnSide(int i, int j, int k, int l) {
		Block block = Block.BY_ID[this.getBlockId(i, j, k)];
		return block != null && ((ForgeBlock) block).isBlockSolidOnSide((World) (Object) this, i, j, k, l);
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Overwrite
	public boolean canPlaceBlock(int i, int j, int k, int l, boolean flag, int i1) {
		int j1 = this.getBlockId(j, k, l);
		Block block = Block.BY_ID[j1];
		Block block1 = Block.BY_ID[i];
		AxixAlignedBoundingBox axisalignedbb = block1.getCollisionShape((World) (Object) this, j, k, l);
		if (flag) {
			axisalignedbb = null;
		}

		if (axisalignedbb != null && !this.canSpawnEntity(axisalignedbb)) {
			return false;
		} else {
			if (block == Block.FLOWING_WATER
					|| block == Block.STILL_WATER
					|| block == Block.FLOWING_LAVA
					|| block == Block.STILL_LAVA
					|| block == Block.FIRE
					|| block == Block.SNOW) {
				block = null;
			}

			if (block != null && ((ForgeBlock) block).isBlockReplaceable((World) (Object) this, j, k, l)) {
				block = null;
			}

			return i > 0 && block == null && block1.canPlaceAt((World) (Object) this, j, k, l, i1);
		}
	}
}
