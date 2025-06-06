package io.github.fabriccompatibilitylayers.modloader.mixin.common;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import modloader.ModLoader;
import net.minecraft.block.entity.FurnaceBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FurnaceBlockEntity.class)
public class FurnaceBlockEntityMixin {
	@ModifyReturnValue(method = "getFuelTime", at = @At(value = "RETURN", ordinal = 6))
	private int modloader$AddAllFuel(int original,
									 @Local int j) {
		if (original == 0) {
			return ModLoader.AddAllFuel(j);
		}

		return original;
	}
}
