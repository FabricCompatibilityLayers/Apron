package io.github.betterthanupdates.forge.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.render.Tessellator;

@Mixin(Tessellator.class)
public class VanillaTessellatorMixin {
	/**
	 * @author Eloraam
	 * @reason method instruction order is different from vanilla.
	 */
	@Inject(method = "addVertex", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Tessellator;tessellate()V"), cancellable = true)
	private void forge$addVertex$2(double d, double d1, double d2, CallbackInfo ci) {
		ci.cancel();
	}
}
