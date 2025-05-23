package io.github.betterthanupdates.apron.compat.mixin.client.betterthanwolves;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.FCUtilsMisc;
import net.minecraft.block.Block;
import net.minecraft.block.PlantBlock;
import net.minecraft.world.World;

@Mixin(PlantBlock.class)
public class PlantBlockMixin {
	@ModifyExpressionValue(method = "canPlaceAt", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/PlantBlock;method_1683(I)Z"))
	private boolean btw$canPlaceAt(boolean original, @Local World arg, @Local(ordinal = 0) int i, @Local(ordinal = 1) int j, @Local(ordinal = 2) int k) {
		return original || FCUtilsMisc.CanPlantGrowOnBlock(arg, i, j - 1, k, (Block)(Object) this);
	}

	@ModifyExpressionValue(method = "canGrow", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/PlantBlock;method_1683(I)Z"))
	private boolean btw$canGrow(boolean original, @Local World arg, @Local(ordinal = 0) int i, @Local(ordinal = 1) int j, @Local(ordinal = 2) int k) {
		return original || FCUtilsMisc.CanPlantGrowOnBlock(arg, i, j - 1, k, (Block)(Object) this);
	}
}
