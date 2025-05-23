package io.github.betterthanupdates.apron.stapi.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.betterthanupdates.apron.stapi.ApronStAPICompat;
import net.minecraft.client.particle.ItemParticle;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemParticle.class)
public class ItemParticleMixin {
	@WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;getTextureId(I)I"))
	private int apron$stapi$fixItemTexture(Item instance, int i, Operation<Integer> original) {
		var oldTextureId = original.call(instance, i);

		return ApronStAPICompat.fixItemTexture(oldTextureId, instance);
	}
}
