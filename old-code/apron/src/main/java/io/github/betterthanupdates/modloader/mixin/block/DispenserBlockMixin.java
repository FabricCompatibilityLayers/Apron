package io.github.betterthanupdates.modloader.mixin.block;

import java.util.Random;

import com.llamalad7.mixinextras.sugar.Local;
import modloader.ModLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.DispenserBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@Mixin(DispenserBlock.class)
public abstract class DispenserBlockMixin {
	/**
	 * @author Risugami
	 * @reason Implements {@link ModLoader#DispenseEntity(World, double, double, double, int, int, ItemStack)
	 * ModLoader's dispense entity callback}
	 */
	@Inject(method = "method_1774", cancellable = true, at = @At(value = "FIELD", target = "Lnet/minecraft/item/ItemStack;itemId:I", ordinal = 0))
	private void handleDispense(World i, int x, int y, int z, Random random, CallbackInfo ci,
								@Local(ordinal = 0) World world, @Local(ordinal = 0) double posX, @Local(ordinal = 1) double posY,
								@Local(ordinal = 2) double posZ, @Local(ordinal = 4) int j, @Local(ordinal = 5) int k, @Local(ordinal = 0) ItemStack itemStack) {
		if (ModLoader.DispenseEntity(world, posX, posY, posZ, j, k, itemStack)) {
			world.method_230(2000, x, y, z, j + 1 + (k + 1) * 3);
			ci.cancel();
		}
	}
}
