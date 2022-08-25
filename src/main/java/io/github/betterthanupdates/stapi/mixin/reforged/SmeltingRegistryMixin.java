package io.github.betterthanupdates.stapi.mixin.reforged;

import net.modificationstation.stationapi.api.recipe.SmeltingRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.SmeltingRecipeRegistry;

import io.github.betterthanupdates.reforged.recipe.ReforgedSmeltingRecipeRegistry;

@Mixin(SmeltingRegistry.class)
public class SmeltingRegistryMixin {
	@Inject(method = "getResultFor", at = @At("HEAD"), cancellable = true)
	private static void getReforgedResultsFirst(ItemStack input, CallbackInfoReturnable<ItemStack> cir) {
		ItemStack reforgedStack = ((ReforgedSmeltingRecipeRegistry) SmeltingRecipeRegistry.getInstance()).reforged$getSmeltingResult(input);

		if (reforgedStack != null) {
			cir.setReturnValue(reforgedStack);
		}
	}
}
