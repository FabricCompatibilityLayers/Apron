package io.github.fabriccompatibilitylayers.modloader.mixin.client.modloader;

import com.llamalad7.mixinextras.sugar.Local;
import modloader.ModLoader;
import net.minecraft.block.DispenserBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(DispenserBlock.class)
public class DispenserBlockMixin {
	@Inject(method = "dispense", at = @At(value = "FIELD", target = "Lnet/minecraft/item/Item;ARROW:Lnet/minecraft/item/Item;"), cancellable = true)
	private void modloader$DispenseEntity(World paramfd, int paramInt1, int paramInt2, int paramInt3, Random paramRandom, CallbackInfo ci,
										  @Local(ordinal = 0) double d1,
										  @Local(ordinal = 1) double d2,
										  @Local(ordinal = 2) double d3,
										  @Local(ordinal = 4) int j,
										  @Local(ordinal = 5) int k,
										  @Local() ItemStack localiz) {
		if (ModLoader.DispenseEntity(paramfd, d1, d2, d3, j, k, localiz)) {
			paramfd.worldEvent(2000, paramInt1, paramInt2, paramInt3, j + 1 + (k + 1) * 3);
			ci.cancel();
		}
	}
}
