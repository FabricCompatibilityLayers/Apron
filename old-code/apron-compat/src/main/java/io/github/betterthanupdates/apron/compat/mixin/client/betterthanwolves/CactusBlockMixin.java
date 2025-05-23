package io.github.betterthanupdates.apron.compat.mixin.client.betterthanwolves;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.FCUtilsMisc;
import net.minecraft.block.CactusBlock;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CactusBlock.class)
public class CactusBlockMixin {
	@ModifyReturnValue(method = "canGrow", at = @At(value = "RETURN", ordinal = 4))
	private boolean addBTWCheck(boolean original, @Local World arg, @Local(ordinal = 0) int i, @Local(ordinal = 1) int j, @Local(ordinal = 2) int k) {
		return original || FCUtilsMisc.CanPlantGrowOnBlock(arg, i, j - 1, k, (CactusBlock)(Object)this);
	}
}
