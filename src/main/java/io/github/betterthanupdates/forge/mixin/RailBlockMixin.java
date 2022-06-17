package io.github.betterthanupdates.forge.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.RailBlock;
import net.minecraft.block.RailBlock.RailMagicStuff;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

@Mixin(RailBlock.class)
public abstract class RailBlockMixin extends Block {
	@Shadow
	@Final
	private boolean field_1262;

	@Shadow
	protected abstract boolean method_1103(World arg, int i, int j, int k, int l, boolean bl, int m);

	@Shadow
	protected abstract void method_1104(World arg, int i, int j, int k, boolean bl);

	protected RailBlockMixin(int i, Material arg) {
		super(i, arg);
	}

	/**
	 * @author Forge
	 */
	@Overwrite
	public static final boolean method_1109(World world, int i, int j, int k) {
		int l = world.getBlockId(i, j, k);
		return Block.BY_ID[l] instanceof RailBlock;
	}

	/**
	 * @author Forge
	 */
	@Overwrite
	public static final boolean isRail(int i) {
		return Block.BY_ID[i] instanceof RailBlock;
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
		if (!world.isClient) {
			int i1 = world.getBlockMeta(i, j, k);
			int j1 = i1;
			if (this.field_1262) {
				j1 = i1 & 7;
			}

			boolean flag = false;
			if (!world.isBlockSolidOnSide(i, j - 1, k, 1)) {
				flag = true;
			}

			if (j1 == 2 && !world.isBlockSolidOnSide(i + 1, j, k, 1)) {
				flag = true;
			}

			if (j1 == 3 && !world.isBlockSolidOnSide(i - 1, j, k, 1)) {
				flag = true;
			}

			if (j1 == 4 && !world.isBlockSolidOnSide(i, j, k - 1, 1)) {
				flag = true;
			}

			if (j1 == 5 && !world.isBlockSolidOnSide(i, j, k + 1, 1)) {
				flag = true;
			}

			if (flag) {
				this.drop(world, i, j, k, world.getBlockMeta(i, j, k));
				world.setBlock(i, j, k, 0);
			} else if (this.id == Block.GOLDEN_RAIL.id) {
				boolean flag1 = world.hasRedstonePower(i, j, k) || world.hasRedstonePower(i, j + 1, k);
				flag1 = flag1 || this.method_1103(world, i, j, k, i1, true, 0) || this.method_1103(world, i, j, k, i1, false, 0);
				boolean flag2 = false;
				if (flag1 && (i1 & 8) == 0) {
					world.setBlockMeta(i, j, k, j1 | 8);
					flag2 = true;
				} else if (!flag1 && (i1 & 8) != 0) {
					world.setBlockMeta(i, j, k, j1);
					flag2 = true;
				}

				if (flag2) {
					world.updateAdjacentBlocks(i, j - 1, k, this.id);
					if (j1 == 2 || j1 == 3 || j1 == 4 || j1 == 5) {
						world.updateAdjacentBlocks(i, j + 1, k, this.id);
					}
				}
			} else if (l > 0
					&& Block.BY_ID[l].getEmitsRedstonePower()
					&& !this.field_1262) {
				try {
					Constructor<RailMagicStuff> ctr = RailMagicStuff.class.getDeclaredConstructor(RailBlock.class, World.class, Integer.class, Integer.class, Integer.class);
					RailMagicStuff railMagicStuff = ctr.newInstance((RailBlock) (Object) this, world, i, j, k);
					if (railMagicStuff.method_1116() == 3) {
						this.method_1104(world, i, j, k, false);
					}
				} catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
						 IllegalAccessException e) {
					e.printStackTrace();
				}
			}

		}
	}

	// idk but keep it in case
	static boolean method_1106(RailBlock blockrail) {
		return ((RailBlockMixin) (Object) blockrail).field_1262;
	}
}