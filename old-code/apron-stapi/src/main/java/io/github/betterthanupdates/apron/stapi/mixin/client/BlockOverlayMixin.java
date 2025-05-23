package io.github.betterthanupdates.apron.stapi.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.betterthanupdates.apron.stapi.ApronStAPICompat;
import net.minecraft.block.Block;
import net.minecraft.class_556;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(class_556.class)
public class BlockOverlayMixin {
	@WrapOperation(method = "method_1864", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getTexture(I)I"))
	private int apron$stapi$fixBlockTextureIndex(Block instance, int i, Operation<Integer> original) {
		var oldTexture = original.call(instance, i);

		return ApronStAPICompat.fixBlockTexture(oldTexture, instance);
	}
}
