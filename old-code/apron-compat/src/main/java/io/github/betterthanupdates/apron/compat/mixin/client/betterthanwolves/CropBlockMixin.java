package io.github.betterthanupdates.apron.compat.mixin.client.betterthanwolves;

import net.minecraft.FCISoil;
import net.minecraft.FCUtilsMisc;
import net.minecraft.block.Block;
import net.minecraft.block.CropBlock;
import net.minecraft.block.PlantBlock;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(CropBlock.class)
public class CropBlockMixin extends PlantBlock {
	protected CropBlockMixin(int i, int j) {
		super(i, j);
	}
//	@Inject(method = "growCropStage", at = @At(value = "JUMP", opcode = Opcodes.IF_ICMPNE, ordinal = 33), locals = LocalCapture.CAPTURE_FAILSOFT)
//	private void addBTWCheck(World arg, int i, int j, int k, CallbackInfoReturnable<Float> callbackInfoReturnable, @Local int var17, @Local int var18, @Local int var19, @Local float var20) {
//		if (FCUtilsMisc.CanPlantGrowOnBlock(arg, var17, j - 1, var18, (CropBlock)(Object)this)) {
//			var20 = 1.0F;
//			Block blockBelow = Block.BY_ID[var19];
//			if (blockBelow instanceof FCISoil && ((FCISoil) blockBelow).IsBlockHydrated(arg, var17, j - 1, var18)) {
//				var20 = 3.0F;
//			}
//		}
//	}

	/**
	 * @author BTW
	 * @reason difficult to convert
	 */
	@Overwrite
	private float method_997(World world, int i, int j, int k) {
		float f = 1.0F;
		int l = world.getBlockId(i, j, k - 1);
		int i1 = world.getBlockId(i, j, k + 1);
		int j1 = world.getBlockId(i - 1, j, k);
		int k1 = world.getBlockId(i + 1, j, k);
		int l1 = world.getBlockId(i - 1, j, k - 1);
		int i2 = world.getBlockId(i + 1, j, k - 1);
		int j2 = world.getBlockId(i + 1, j, k + 1);
		int k2 = world.getBlockId(i - 1, j, k + 1);
		boolean flag = j1 == this.id || k1 == this.id;
		boolean flag1 = l == this.id || i1 == this.id;
		boolean flag2 = l1 == this.id || i2 == this.id || j2 == this.id || k2 == this.id;

		for(int l2 = i - 1; l2 <= i + 1; ++l2) {
			for(int i3 = k - 1; i3 <= k + 1; ++i3) {
				int j3 = world.getBlockId(l2, j - 1, i3);
				float f1 = 0.0F;
				if (j3 == Block.FARMLAND.id) {
					f1 = 1.0F;
					if (world.getBlockMeta(l2, j - 1, i3) > 0) {
						f1 = 3.0F;
					}
				} else if (FCUtilsMisc.CanPlantGrowOnBlock(world, l2, j - 1, i3, this)) {
					f1 = 1.0F;
					Block blockBelow = Block.BLOCKS[j3];
					if (blockBelow instanceof FCISoil && ((FCISoil)blockBelow).IsBlockHydrated(world, l2, j - 1, i3)) {
						f1 = 3.0F;
					}
				}

				if (l2 != i || i3 != k) {
					f1 /= 4.0F;
				}

				f += f1;
			}
		}

		if (flag2 || flag && flag1) {
			f /= 2.0F;
		}

		return f;
	}
}
