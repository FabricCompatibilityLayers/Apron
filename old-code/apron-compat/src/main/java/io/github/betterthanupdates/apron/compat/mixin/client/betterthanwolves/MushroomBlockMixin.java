package io.github.betterthanupdates.apron.compat.mixin.client.betterthanwolves;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.FCUtilsMisc;
import net.minecraft.block.Block;
import net.minecraft.block.MushroomPlantBlock;
import net.minecraft.world.World;

@Mixin(MushroomPlantBlock.class)
public class MushroomBlockMixin {
	@ModifyExpressionValue(method = "canGrow", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/MushroomPlantBlock;method_1683(I)Z"))
	private boolean btw$canGrow(boolean original, @Local World arg, @Local(ordinal = 0) int i, @Local(ordinal = 1) int j, @Local(ordinal = 2) int k) {
		return original || FCUtilsMisc.CanPlantGrowOnBlock(arg, i, j - 1, k, (Block)(Object) this);
	}
}
