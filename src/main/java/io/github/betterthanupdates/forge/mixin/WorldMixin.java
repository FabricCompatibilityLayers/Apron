package io.github.betterthanupdates.forge.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.block.Block;
import net.minecraft.entity.BlockEntity;
import net.minecraft.util.math.AxixAlignedBoundingBox;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BlockView;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.dimension.Dimension;

import io.github.betterthanupdates.forge.block.ForgeBlock;
import io.github.betterthanupdates.forge.world.ForgeWorld;

@Mixin(World.class)
public abstract class WorldMixin implements BlockView, ForgeWorld {
	@Shadow
	@Final
	public Dimension dimension;

	@Shadow
	public abstract boolean isBlockLoaded(int i, int j, int k);

	@Shadow
	public abstract boolean isAboveGround(int i, int j, int k);

	@Shadow
	public abstract int method_164(LightType arg, int i, int j, int k);

	@Shadow
	public abstract void method_166(LightType arg, int i, int j, int k, int l, int m, int n);

	@Shadow
	public abstract boolean method_155(int i, int j, int k, int l, int m, int n);

	@Shadow
	private boolean field_190;

	@Shadow
	private List field_185;

	@Shadow
	public List blockEntities;

	@Shadow
	public abstract Chunk getChunkFromCache(int i, int j);

	@Shadow
	public abstract boolean canSpawnEntity(AxixAlignedBoundingBox arg);

	/**
	 * @author Forge
	 * @reason
	 */
	@Overwrite
	public boolean isAir(int i, int j, int k) {
		int iBlockID = this.getBlockId(i, j, k);
		return iBlockID == 0 ? true : ((ForgeBlock) Block.BY_ID[iBlockID]).isAirBlock((World) (Object) this, i, j, k);
	}

	/**
	 * @author Forge
	 * @reason
	 */
	@Overwrite
	public void method_165(LightType enumskyblock, int i, int j, int k, int l) {
		if (!this.dimension.halvesMapping || enumskyblock != LightType.field_2757) {
			if (this.isBlockLoaded(i, j, k)) {
				if (enumskyblock == LightType.field_2757) {
					if (this.isAboveGround(i, j, k)) {
						l = 15;
					}
				} else if (enumskyblock == LightType.field_2758) {
					int i1 = this.getBlockId(i, j, k);
					int bl = i1 == 0 ? 0 : ((ForgeBlock) Block.BY_ID[i1]).getLightValue(this, i, j, k);

					if (bl > l) {
						l = bl;
					}
				}

				if (this.method_164(enumskyblock, i, j, k) != l) {
					this.method_166(enumskyblock, i, j, k, i, j, k);
				}
			}
		}
	}

	/**
	 * @author Forge
	 * @reason
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
			for (int k1 = i; k1 < j; ++k1) {
				for (int l1 = k; l1 < l; ++l1) {
					for (int i2 = i1; i2 < j1; ++i2) {
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
	 * @author Forge
	 * @reason
	 */
	@Overwrite
	public void setBlockEntity(int i, int j, int k, BlockEntity tileentity) {
		if (!tileentity.isInvalid()) {
			if (this.field_190) {
				tileentity.x = i;
				tileentity.y = j;
				tileentity.z = k;
				boolean found = false;

				for (Object o : this.field_185) {
					BlockEntity e = (BlockEntity) o;

					if (e.x == i && e.y == j && e.z == k) {
						found = true;
						break;
					}
				}

				if (!found) {
					this.field_185.add(tileentity);
				}
			} else {
				this.blockEntities.add(tileentity);
				Chunk chunk = this.getChunkFromCache(i >> 4, k >> 4);

				if (chunk != null) {
					chunk.placeBlockEntity(i & 15, j, k & 15, tileentity);
				}
			}
		}
	}

	/**
	 * @author Forge
	 * @reason
	 */
	@Overwrite
	public boolean canSuffocate(int i, int j, int k) {
		Block block = Block.BY_ID[this.getBlockId(i, j, k)];
		return block == null ? false : ((ForgeBlock) block).isBlockNormalCube((World) (Object) this, i, j, k);
	}

	@Override
	public boolean isBlockSolidOnSide(int i, int j, int k, int side) {
		Block block = Block.BY_ID[this.getBlockId(i, j, k)];
		return block == null ? false : ((ForgeBlock) block).isBlockSolidOnSide((World) (Object) this, i, j, k, side);
	}

	/**
	 * @author Forge
	 * @reason
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
