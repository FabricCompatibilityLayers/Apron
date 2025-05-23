package io.github.betterthanupdates.apron.compat.mixin.client.betterthanwolves;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.FCISoil;
import net.minecraft.FCUtilsMisc;
import net.minecraft.block.Block;
import net.minecraft.block.SugarCaneBlock;
import net.minecraft.world.World;

@Mixin(SugarCaneBlock.class)
public class SugarCaneBlockMixin {
	@ModifyExpressionValue(method = "canPlaceAt", at = @At(value = "FIELD", target = "Lnet/minecraft/block/GrassBlock;id:I"))
	private int btw$cannotPlaceAt(int original, @Local World arg, @Local(ordinal = 0) int i, @Local(ordinal = 1) int j, @Local(ordinal = 2) int k, @Local(ordinal = 3) int var5) {
		if (FCUtilsMisc.CanPlantGrowOnBlock(arg, i, j - 1, k, (Block)(Object) this)) {
			return var5;
		}

		return original;
	}

	@ModifyReturnValue(method = "canPlaceAt", at = @At(value = "RETURN", ordinal = 5))
	private boolean btw$canPlaceAt(boolean original, @Local World arg, @Local(ordinal = 0) int i, @Local(ordinal = 1) int j, @Local(ordinal = 2) int k, @Local(ordinal = 3) int l) {
		Block blockBelow = Block.BLOCKS[l];
		if (blockBelow instanceof FCISoil && ((FCISoil)blockBelow).IsBlockConsideredNeighbouringWater(arg, i, j - 1, k)) {
			return true;
		} else {
			return original;
		}
	}
}
