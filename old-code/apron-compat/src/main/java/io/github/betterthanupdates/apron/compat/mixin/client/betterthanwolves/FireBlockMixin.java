package io.github.betterthanupdates.apron.compat.mixin.client.betterthanwolves;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.minecraft.FCBlockBBQ;
import net.minecraft.block.Block;
import net.minecraft.block.FireBlock;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.mod_FCBetterThanWolves;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

import io.github.betterthanupdates.apron.compat.betterthanwolves.BTWFireBlock;

@Mixin(FireBlock.class)
public class FireBlockMixin implements BTWFireBlock {
	@ModifyReturnValue(method = "getRenderType", at = @At("RETURN"))
	private int btw$getRenderType(int original) {
		return mod_FCBetterThanWolves.iCustomFireRenderID;
	}

	@Inject(method = "onTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/FireBlock;canPlaceAt(Lnet/minecraft/world/World;III)Z", shift = At.Shift.BY, by = 3), cancellable = true)
	private void addBTWFireCheck(World arg, int i, int j, int k, Random random, CallbackInfo callbackInfo, @Local(ordinal = 3) LocalIntRef var6) {
		if(arg.getBlockId(i, j - 1, k) == mod_FCBetterThanWolves.fcBBQ.id) {
			if(!((FCBlockBBQ)mod_FCBetterThanWolves.fcBBQ).IsBBQLit(arg, i, j - 1, k)) {
				arg.setBlock(i, j, k, 0);
				callbackInfo.cancel();
			}
			var6.set(1);
		} else if(arg.getBlockId(i, j - 1, k) == mod_FCBetterThanWolves.fcStokedFire.id) {
			var6.set(1);
		}
	}

	@Override
	public void OnBlockDestroyedByFire(World world, int i, int j, int k) {
		int iBlockID = world.getBlockId(i, j, k);
		if(iBlockID == mod_FCBetterThanWolves.fcCompanionCube.id) {
			FCBlockCompanionCubeMixin.SpawnHearts(world, i, j, k);
		}
	}

	@Inject(method = "method_1823", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockId(III)I", ordinal = 1))
	private void addBTWMethod(World arg, int i, int j, int k, int l, Random random, int m, CallbackInfo callbackInfo) {
		this.OnBlockDestroyedByFire(arg, i, j, k);
	}

	@ModifyReturnValue(method = "canPlaceAt", at = @At("RETURN"))
	private boolean addBTWStokedFire(boolean original, @Local World arg, @Local(ordinal = 0) int i, @Local(ordinal = 1) int j, @Local(ordinal = 2) int k) {
		return original || arg.getBlockId(i, j - 1, k) == mod_FCBetterThanWolves.fcStokedFire.id;
	}

	@ModifyExpressionValue(method = "neighborUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;method_1780(III)Z"))
	private boolean addBTWStonkedFireCheck_1(boolean original, @Local World arg, @Local(ordinal = 0) int i, @Local(ordinal = 1) int j, @Local(ordinal = 2) int k) {
		return original && arg.getBlockId(i, j - 1, k) != mod_FCBetterThanWolves.fcStokedFire.id;
	}

	@ModifyExpressionValue(method = "onPlaced", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;method_1780(III)Z"))
	private boolean addBTWStonkedFireCheck_2(boolean original, @Local World arg, @Local(ordinal = 0) int i, @Local(ordinal = 1) int j, @Local(ordinal = 2) int k) {
		return original && arg.getBlockId(i, j - 1, k) != mod_FCBetterThanWolves.fcStokedFire.id;
	}

	@Override
	public boolean RenderFire(BlockRenderManager renderBlocks, BlockView iBlockAccess, int i, int j, int k, Block block) {
		Tessellator tessellator = Tessellator.INSTANCE;
		int l = block.getTexture(0);
		if (renderBlocks.textureOverride >= 0) {
			l = renderBlocks.textureOverride;
		}

		float f = block.getLuminance(iBlockAccess, i, j, k);
		tessellator.color(f, f, f);
		int i1 = (l & 15) << 4;
		int j1 = l & 240;
		double d = (double)((float)i1 / 256.0F);
		double d1 = (double)(((float)i1 + 15.99F) / 256.0F);
		double d2 = (double)((float)j1 / 256.0F);
		double d3 = (double)(((float)j1 + 15.99F) / 256.0F);
		float f1 = 1.4F;
		if (!iBlockAccess.method_1780(i, j - 1, k)
				&& !Block.FIRE.method_1824(iBlockAccess, i, j - 1, k)
				&& iBlockAccess.getBlockId(i, j - 1, k) != mod_FCBetterThanWolves.fcStokedFire.id) {
			float f2 = 0.2F;
			float f3 = 0.0625F;
			if ((i + j + k & 1) == 1) {
				d = (double)((float)i1 / 256.0F);
				d1 = (double)(((float)i1 + 15.99F) / 256.0F);
				d2 = (double)((float)(j1 + 16) / 256.0F);
				d3 = (double)(((float)j1 + 15.99F + 16.0F) / 256.0F);
			}

			if ((i / 2 + j / 2 + k / 2 & 1) == 1) {
				double d6 = d1;
				d1 = d;
				d = d6;
			}

			if (Block.FIRE.method_1824(iBlockAccess, i - 1, j, k)) {
				tessellator.vertex((double)((float)i + f2), (double)((float)j + f1 + f3), (double)(k + 1), d1, d2);
				tessellator.vertex((double)(i + 0), (double)((float)(j + 0) + f3), (double)(k + 1), d1, d3);
				tessellator.vertex((double)(i + 0), (double)((float)(j + 0) + f3), (double)(k + 0), d, d3);
				tessellator.vertex((double)((float)i + f2), (double)((float)j + f1 + f3), (double)(k + 0), d, d2);
				tessellator.vertex((double)((float)i + f2), (double)((float)j + f1 + f3), (double)(k + 0), d, d2);
				tessellator.vertex((double)(i + 0), (double)((float)(j + 0) + f3), (double)(k + 0), d, d3);
				tessellator.vertex((double)(i + 0), (double)((float)(j + 0) + f3), (double)(k + 1), d1, d3);
				tessellator.vertex((double)((float)i + f2), (double)((float)j + f1 + f3), (double)(k + 1), d1, d2);
			}

			if (Block.FIRE.method_1824(iBlockAccess, i + 1, j, k)) {
				tessellator.vertex((double)((float)(i + 1) - f2), (double)((float)j + f1 + f3), (double)(k + 0), d, d2);
				tessellator.vertex((double)(i + 1 - 0), (double)((float)(j + 0) + f3), (double)(k + 0), d, d3);
				tessellator.vertex((double)(i + 1 - 0), (double)((float)(j + 0) + f3), (double)(k + 1), d1, d3);
				tessellator.vertex((double)((float)(i + 1) - f2), (double)((float)j + f1 + f3), (double)(k + 1), d1, d2);
				tessellator.vertex((double)((float)(i + 1) - f2), (double)((float)j + f1 + f3), (double)(k + 1), d1, d2);
				tessellator.vertex((double)(i + 1 - 0), (double)((float)(j + 0) + f3), (double)(k + 1), d1, d3);
				tessellator.vertex((double)(i + 1 - 0), (double)((float)(j + 0) + f3), (double)(k + 0), d, d3);
				tessellator.vertex((double)((float)(i + 1) - f2), (double)((float)j + f1 + f3), (double)(k + 0), d, d2);
			}

			if (Block.FIRE.method_1824(iBlockAccess, i, j, k - 1)) {
				tessellator.vertex((double)(i + 0), (double)((float)j + f1 + f3), (double)((float)k + f2), d1, d2);
				tessellator.vertex((double)(i + 0), (double)((float)(j + 0) + f3), (double)(k + 0), d1, d3);
				tessellator.vertex((double)(i + 1), (double)((float)(j + 0) + f3), (double)(k + 0), d, d3);
				tessellator.vertex((double)(i + 1), (double)((float)j + f1 + f3), (double)((float)k + f2), d, d2);
				tessellator.vertex((double)(i + 1), (double)((float)j + f1 + f3), (double)((float)k + f2), d, d2);
				tessellator.vertex((double)(i + 1), (double)((float)(j + 0) + f3), (double)(k + 0), d, d3);
				tessellator.vertex((double)(i + 0), (double)((float)(j + 0) + f3), (double)(k + 0), d1, d3);
				tessellator.vertex((double)(i + 0), (double)((float)j + f1 + f3), (double)((float)k + f2), d1, d2);
			}

			if (Block.FIRE.method_1824(iBlockAccess, i, j, k + 1)) {
				tessellator.vertex((double)(i + 1), (double)((float)j + f1 + f3), (double)((float)(k + 1) - f2), d, d2);
				tessellator.vertex((double)(i + 1), (double)((float)(j + 0) + f3), (double)(k + 1 - 0), d, d3);
				tessellator.vertex((double)(i + 0), (double)((float)(j + 0) + f3), (double)(k + 1 - 0), d1, d3);
				tessellator.vertex((double)(i + 0), (double)((float)j + f1 + f3), (double)((float)(k + 1) - f2), d1, d2);
				tessellator.vertex((double)(i + 0), (double)((float)j + f1 + f3), (double)((float)(k + 1) - f2), d1, d2);
				tessellator.vertex((double)(i + 0), (double)((float)(j + 0) + f3), (double)(k + 1 - 0), d1, d3);
				tessellator.vertex((double)(i + 1), (double)((float)(j + 0) + f3), (double)(k + 1 - 0), d, d3);
				tessellator.vertex((double)(i + 1), (double)((float)j + f1 + f3), (double)((float)(k + 1) - f2), d, d2);
			}

			if (Block.FIRE.method_1824(iBlockAccess, i, j + 1, k)) {
				double d7 = (double)i + 0.5 + 0.5;
				double d9 = (double)i + 0.5 - 0.5;
				double d11 = (double)k + 0.5 + 0.5;
				double d13 = (double)k + 0.5 - 0.5;
				double d15 = (double)i + 0.5 - 0.5;
				double d17 = (double)i + 0.5 + 0.5;
				double d19 = (double)k + 0.5 - 0.5;
				double d20 = (double)k + 0.5 + 0.5;
				double d21 = (double)((float)i1 / 256.0F);
				double d22 = (double)(((float)i1 + 15.99F) / 256.0F);
				double d23 = (double)((float)j1 / 256.0F);
				double d24 = (double)(((float)j1 + 15.99F) / 256.0F);
				++j;
				float f4 = -0.2F;
				if ((i + j + k & 1) == 0) {
					tessellator.vertex(d15, (double)((float)j + f4), (double)(k + 0), d22, d23);
					tessellator.vertex(d7, (double)(j + 0), (double)(k + 0), d22, d24);
					tessellator.vertex(d7, (double)(j + 0), (double)(k + 1), d21, d24);
					tessellator.vertex(d15, (double)((float)j + f4), (double)(k + 1), d21, d23);
					d21 = (double)((float)i1 / 256.0F);
					d22 = (double)(((float)i1 + 15.99F) / 256.0F);
					d23 = (double)((float)(j1 + 16) / 256.0F);
					d24 = (double)(((float)j1 + 15.99F + 16.0F) / 256.0F);
					tessellator.vertex(d17, (double)((float)j + f4), (double)(k + 1), d22, d23);
					tessellator.vertex(d9, (double)(j + 0), (double)(k + 1), d22, d24);
					tessellator.vertex(d9, (double)(j + 0), (double)(k + 0), d21, d24);
					tessellator.vertex(d17, (double)((float)j + f4), (double)(k + 0), d21, d23);
				} else {
					tessellator.vertex((double)(i + 0), (double)((float)j + f4), d20, d22, d23);
					tessellator.vertex((double)(i + 0), (double)(j + 0), d13, d22, d24);
					tessellator.vertex((double)(i + 1), (double)(j + 0), d13, d21, d24);
					tessellator.vertex((double)(i + 1), (double)((float)j + f4), d20, d21, d23);
					d21 = (double)((float)i1 / 256.0F);
					d22 = (double)(((float)i1 + 15.99F) / 256.0F);
					d23 = (double)((float)(j1 + 16) / 256.0F);
					d24 = (double)(((float)j1 + 15.99F + 16.0F) / 256.0F);
					tessellator.vertex((double)(i + 1), (double)((float)j + f4), d19, d22, d23);
					tessellator.vertex((double)(i + 1), (double)(j + 0), d11, d22, d24);
					tessellator.vertex((double)(i + 0), (double)(j + 0), d11, d21, d24);
					tessellator.vertex((double)(i + 0), (double)((float)j + f4), d19, d21, d23);
				}
			}
		} else {
			double d4 = (double)i + 0.5 + 0.2;
			double d5 = (double)i + 0.5 - 0.2;
			double d8 = (double)k + 0.5 + 0.2;
			double d10 = (double)k + 0.5 - 0.2;
			double d12 = (double)i + 0.5 - 0.3;
			double d14 = (double)i + 0.5 + 0.3;
			double d16 = (double)k + 0.5 - 0.3;
			double d18 = (double)k + 0.5 + 0.3;
			tessellator.vertex(d12, (double)((float)j + f1), (double)(k + 1), d1, d2);
			tessellator.vertex(d4, (double)(j + 0), (double)(k + 1), d1, d3);
			tessellator.vertex(d4, (double)(j + 0), (double)(k + 0), d, d3);
			tessellator.vertex(d12, (double)((float)j + f1), (double)(k + 0), d, d2);
			tessellator.vertex(d14, (double)((float)j + f1), (double)(k + 0), d1, d2);
			tessellator.vertex(d5, (double)(j + 0), (double)(k + 0), d1, d3);
			tessellator.vertex(d5, (double)(j + 0), (double)(k + 1), d, d3);
			tessellator.vertex(d14, (double)((float)j + f1), (double)(k + 1), d, d2);
			d = (double)((float)i1 / 256.0F);
			d1 = (double)(((float)i1 + 15.99F) / 256.0F);
			d2 = (double)((float)(j1 + 16) / 256.0F);
			d3 = (double)(((float)j1 + 15.99F + 16.0F) / 256.0F);
			tessellator.vertex((double)(i + 1), (double)((float)j + f1), d18, d1, d2);
			tessellator.vertex((double)(i + 1), (double)(j + 0), d10, d1, d3);
			tessellator.vertex((double)(i + 0), (double)(j + 0), d10, d, d3);
			tessellator.vertex((double)(i + 0), (double)((float)j + f1), d18, d, d2);
			tessellator.vertex((double)(i + 0), (double)((float)j + f1), d16, d1, d2);
			tessellator.vertex((double)(i + 0), (double)(j + 0), d8, d1, d3);
			tessellator.vertex((double)(i + 1), (double)(j + 0), d8, d, d3);
			tessellator.vertex((double)(i + 1), (double)((float)j + f1), d16, d, d2);
			d4 = (double)i + 0.5 - 0.5;
			d5 = (double)i + 0.5 + 0.5;
			d8 = (double)k + 0.5 - 0.5;
			d10 = (double)k + 0.5 + 0.5;
			d12 = (double)i + 0.5 - 0.4;
			d14 = (double)i + 0.5 + 0.4;
			d16 = (double)k + 0.5 - 0.4;
			d18 = (double)k + 0.5 + 0.4;
			tessellator.vertex(d12, (double)((float)j + f1), (double)(k + 0), d, d2);
			tessellator.vertex(d4, (double)(j + 0), (double)(k + 0), d, d3);
			tessellator.vertex(d4, (double)(j + 0), (double)(k + 1), d1, d3);
			tessellator.vertex(d12, (double)((float)j + f1), (double)(k + 1), d1, d2);
			tessellator.vertex(d14, (double)((float)j + f1), (double)(k + 1), d, d2);
			tessellator.vertex(d5, (double)(j + 0), (double)(k + 1), d, d3);
			tessellator.vertex(d5, (double)(j + 0), (double)(k + 0), d1, d3);
			tessellator.vertex(d14, (double)((float)j + f1), (double)(k + 0), d1, d2);
			d = (double)((float)i1 / 256.0F);
			d1 = (double)(((float)i1 + 15.99F) / 256.0F);
			d2 = (double)((float)j1 / 256.0F);
			d3 = (double)(((float)j1 + 15.99F) / 256.0F);
			tessellator.vertex((double)(i + 0), (double)((float)j + f1), d18, d, d2);
			tessellator.vertex((double)(i + 0), (double)(j + 0), d10, d, d3);
			tessellator.vertex((double)(i + 1), (double)(j + 0), d10, d1, d3);
			tessellator.vertex((double)(i + 1), (double)((float)j + f1), d18, d1, d2);
			tessellator.vertex((double)(i + 1), (double)((float)j + f1), d16, d, d2);
			tessellator.vertex((double)(i + 1), (double)(j + 0), d8, d, d3);
			tessellator.vertex((double)(i + 0), (double)(j + 0), d8, d1, d3);
			tessellator.vertex((double)(i + 0), (double)((float)j + f1), d16, d1, d2);
		}

		return true;
	}
}
