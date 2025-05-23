package io.github.betterthanupdates.apron.stapi.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.betterthanupdates.apron.stapi.ApronStAPICompat;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemStack.class)
@Environment(EnvType.CLIENT)
public class ItemStackMixin {
	@WrapOperation(method = "getTextureId", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;getTextureId(Lnet/minecraft/item/ItemStack;)I"))
	private int apron$stapi$fixItemTexture(Item instance, ItemStack itemStack, Operation<Integer> original) {
		var oldItemTexture = original.call(instance, itemStack);

		return ApronStAPICompat.fixItemTexture(oldItemTexture, instance);
	}
}
