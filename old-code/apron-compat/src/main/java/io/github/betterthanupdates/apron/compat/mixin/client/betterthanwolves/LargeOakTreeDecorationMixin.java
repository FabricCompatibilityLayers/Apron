package io.github.betterthanupdates.apron.compat.mixin.client.betterthanwolves;

import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.FCUtilsMisc;
import net.minecraft.block.Block;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.LargeOakTreeFeature;

@Mixin(LargeOakTreeFeature.class)
public class LargeOakTreeDecorationMixin {
	@Shadow
	@Final
	static byte[] field_645;

	@Shadow
	World world;

	@Shadow
	int[] field_648;

	/**
	 * @author BTW
	 * @reason difficult to convert
	 */
	@Overwrite
	void method_615(int i, int j, int k, float f, byte byte0, int l) {
		int i1 = (int)((double)f + 0.618);
		byte byte1 = field_645[byte0];
		byte byte2 = field_645[byte0 + 3];
		int[] ai = new int[]{i, j, k};
		int[] ai1 = new int[]{0, 0, 0};
		int j1 = -i1;
		int k1 = -i1;

		for(ai1[byte0] = ai[byte0]; j1 <= i1; ++j1) {
			ai1[byte1] = ai[byte1] + j1;
			int l1 = -i1;

			while(l1 <= i1) {
				double d = Math.sqrt(Math.pow((double)Math.abs(j1) + 0.5, 2.0) + Math.pow((double)Math.abs(l1) + 0.5, 2.0));
				if (d > (double)f) {
					++l1;
				} else {
					ai1[byte2] = ai[byte2] + l1;
					int i2 = this.world.getBlockId(ai1[0], ai1[1], ai1[2]);
					if (!this.world.method_234(ai1[0], ai1[1], ai1[2]) && i2 != 18) {
						++l1;
					} else {
						this.world.method_200(ai1[0], ai1[1], ai1[2], l);
						++l1;
					}
				}
			}
		}
	}

	/**
	 * @author BTW
	 * @reason difficult to convert
	 */
	@Overwrite
	int method_616(int[] ai, int[] ai1) {
		int[] ai2 = new int[]{0, 0, 0};
		byte byte0 = 0;

		int i;
		for(i = 0; byte0 < 3; ++byte0) {
			ai2[byte0] = ai1[byte0] - ai[byte0];
			if (Math.abs(ai2[byte0]) > Math.abs(ai2[i])) {
				i = byte0;
			}
		}

		if (ai2[i] == 0) {
			return -1;
		} else {
			byte byte1 = field_645[i];
			byte byte2 = field_645[i + 3];
			byte byte3;
			if (ai2[i] > 0) {
				byte3 = 1;
			} else {
				byte3 = -1;
			}

			double d = (double)ai2[byte1] / (double)ai2[i];
			double d1 = (double)ai2[byte2] / (double)ai2[i];
			int[] ai3 = new int[]{0, 0, 0};
			int j = 0;

			int k;
			for(k = ai2[i] + byte3; j != k; j += byte3) {
				ai3[i] = ai[i] + j;
				ai3[byte1] = MathHelper.floor((double)ai[byte1] + (double)j * d);
				ai3[byte2] = MathHelper.floor((double)ai[byte2] + (double)j * d1);
				int l = this.world.getBlockId(ai3[0], ai3[1], ai3[2]);
				if (!this.world.method_234(ai3[0], ai3[1], ai3[2]) && l != 18) {
					break;
				}
			}

			return j == k ? -1 : Math.abs(j);
		}
	}

	@ModifyConstant(method = "method_611", constant = @Constant(intValue = 3))
	private int btw$method_611(int three, @Local(ordinal = 0) int i) {
		if (FCUtilsMisc.CanPlantGrowOnBlock(this.world, this.field_648[0], this.field_648[1] - 1, this.field_648[2], Block.SAPLING)) {
			return i;
		}

		return three;
	}
}
