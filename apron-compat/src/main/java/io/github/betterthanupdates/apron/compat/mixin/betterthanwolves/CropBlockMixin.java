package io.github.betterthanupdates.apron.compat.mixin.betterthanwolves;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.FCISoil;
import net.minecraft.FCUtilsMisc;
import net.minecraft.block.Block;
import net.minecraft.block.CropBlock;
import net.minecraft.world.World;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(CropBlock.class)
public class CropBlockMixin {
	@Inject(method = "growCropStage", at = @At(value = "JUMP", opcode = Opcodes.IF_ICMPNE, ordinal = 33), locals = LocalCapture.CAPTURE_FAILSOFT)
	private void addBTWCheck(World arg, int i, int j, int k, CallbackInfoReturnable<Float> callbackInfoReturnable, @Local int var17, @Local int var18, @Local int var19, @Local float var20) {
		if (FCUtilsMisc.CanPlantGrowOnBlock(arg, var17, j - 1, var18, (CropBlock)(Object)this)) {
			var20 = 1.0F;
			Block blockBelow = Block.BY_ID[var19];
			if (blockBelow instanceof FCISoil && ((FCISoil) blockBelow).IsBlockHydrated(arg, var17, j - 1, var18)) {
				var20 = 3.0F;
			}
		}
	}
}
