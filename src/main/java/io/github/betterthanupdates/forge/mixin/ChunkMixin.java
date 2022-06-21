package io.github.betterthanupdates.forge.mixin;

import forge.IOverrideReplace;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.block.Block;
import net.minecraft.class_257;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

@Mixin(Chunk.class)
public abstract class ChunkMixin {
	@Shadow
	public byte[] heightmap;

	@Shadow
	public byte[] blocks;

	@Shadow
	public class_257 field_957;

	@Shadow
	@Final
	public int x;

	@Shadow
	@Final
	public int z;

	@Shadow
	public World world;

	@Shadow
	protected abstract void method_889(int i, int j, int k);

	@Shadow
	protected abstract void method_887(int i, int j);

	@Shadow
	public boolean field_967;

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Overwrite
	public boolean setBlockWithMetadata(int i, int j, int k, int l, int i1) {
		byte byte0 = (byte) l;
		int j1 = this.heightmap[k << 4 | i] & 255;
		int k1 = this.blocks[i << 11 | k << 7 | j] & 255;

		if (k1 == l && this.field_957.method_1703(i, j, k) == i1) {
			return false;
		} else {
			int l1 = this.x * 16 + i;
			int i2 = this.z * 16 + k;

			if (Block.BY_ID[k1] instanceof IOverrideReplace) {
				IOverrideReplace iovr = (IOverrideReplace) Block.BY_ID[k1];

				if (!iovr.canReplaceBlock(this.world, l1, j, i2, l)) {
					return iovr.getReplacedSuccess();
				}
			}

			this.blocks[i << 11 | k << 7 | j] = (byte) (byte0 & 255);

			if (k1 != 0 && !this.world.isClient) {
				Block.BY_ID[k1].onBlockRemoved(this.world, l1, j, i2);
			}

			this.field_957.method_1704(i, j, k, i1);

			if (!this.world.dimension.halvesMapping) {
				if (Block.LIGHT_OPACITY[byte0 & 255] != 0) {
					if (j >= j1) {
						this.method_889(i, j + 1, k);
					}
				} else if (j == j1 - 1) {
					this.method_889(i, j, k);
				}

				this.world.method_166(LightType.field_2757, l1, j, i2, l1, j, i2);
			}

			this.world.method_166(LightType.field_2758, l1, j, i2, l1, j, i2);
			this.method_887(i, k);
			this.field_957.method_1704(i, j, k, i1);

			if (l != 0) {
				Block.BY_ID[l].onBlockPlaced(this.world, l1, j, i2);
			}

			this.field_967 = true;
			return true;
		}
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Overwrite
	public boolean method_860(int i, int j, int k, int l) {
		byte byte0 = (byte) l;
		int i1 = this.heightmap[k << 4 | i] & 255;
		int j1 = this.blocks[i << 11 | k << 7 | j] & 255;

		if (j1 == l) {
			return false;
		} else {
			int k1 = this.x * 16 + i;
			int l1 = this.z * 16 + k;

			if (Block.BY_ID[j1] instanceof IOverrideReplace) {
				IOverrideReplace iovr = (IOverrideReplace) Block.BY_ID[j1];

				if (!iovr.canReplaceBlock(this.world, k1, j, l1, l)) {
					return iovr.getReplacedSuccess();
				}
			}

			this.blocks[i << 11 | k << 7 | j] = (byte) (byte0 & 255);

			if (j1 != 0) {
				Block.BY_ID[j1].onBlockRemoved(this.world, k1, j, l1);
			}

			this.field_957.method_1704(i, j, k, 0);

			if (Block.LIGHT_OPACITY[byte0 & 255] != 0) {
				if (j >= i1) {
					this.method_889(i, j + 1, k);
				}
			} else if (j == i1 - 1) {
				this.method_889(i, j, k);
			}

			this.world.method_166(LightType.field_2757, k1, j, l1, k1, j, l1);
			this.world.method_166(LightType.field_2758, k1, j, l1, k1, j, l1);
			this.method_887(i, k);

			if (l != 0 && !this.world.isClient) {
				Block.BY_ID[l].onBlockPlaced(this.world, k1, j, l1);
			}

			this.field_967 = true;
			return true;
		}
	}
}
