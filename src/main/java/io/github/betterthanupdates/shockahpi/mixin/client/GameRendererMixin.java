package io.github.betterthanupdates.shockahpi.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import shockahpi.SAPI;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.GameRenderer;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
	@Shadow
	private Minecraft client;

	/**
	 * @return ShockAhPI-modified reach
	 * @author ShockAh
	 * @reason implement ShockAhPI function
	 */
	@ModifyConstant(method = "method_1838", constant = @Constant(doubleValue = 3.0d))
	private double shockahpi$modifyReach(double constant) {
		return SAPI.reachGetBlock((float) constant);
	}

	@Inject(method = "tick", at = @At("HEAD"))
	private void sapi$tick(float par1, CallbackInfo ci) {
		if (this.client.world != null) {
			this.client.world.autoSaveInterval = 6000;
		}
	}
}
