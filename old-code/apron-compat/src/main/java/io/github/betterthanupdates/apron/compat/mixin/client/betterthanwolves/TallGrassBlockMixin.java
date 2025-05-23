package io.github.betterthanupdates.apron.compat.mixin.client.betterthanwolves;

import java.util.Random;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.mod_FCBetterThanWolves;

@Mixin(TallPlantBlock.class)
public class TallGrassBlockMixin {
	@ModifyReturnValue(method = "getDroppedItemId", at = @At("RETURN"))
	private int btw$getDropId(int value, @Local Random random) {
		if (value == -1) {
			return random.nextInt(50) == 0 ? mod_FCBetterThanWolves.fcHempSeeds.id : -1;
		}

		return value;
	}
}
