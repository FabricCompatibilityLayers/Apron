package io.github.betterthanupdates.apron.stapi.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import io.github.betterthanupdates.apron.stapi.ApronStAPICompat;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	@Environment(EnvType.CLIENT)
	@Inject(method = "getHeldItemTexture", at = @At("RETURN"), cancellable = true)
	private void apron$fixItemTextureIndex(ItemStack par1, CallbackInfoReturnable<Integer> cir) {
		Item item = par1.getItem();
		int result = cir.getReturnValue();

		cir.setReturnValue(ApronStAPICompat.fixItemTexture(result, item));
	}
}
