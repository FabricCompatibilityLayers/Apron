package io.github.betterthanupdates.shockahpi.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import shockahpi.SAPI;

import net.minecraft.client.ClientInteractionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MultiplayerClientInteractionManager;

@Mixin(MultiplayerClientInteractionManager.class)
public class MultiplayerClientInteractionManagerMixin extends ClientInteractionManager {
	public MultiplayerClientInteractionManagerMixin(Minecraft minecraft) {
		super(minecraft);
	}

	@Inject(method = "getBlockReachDistance", at = @At("RETURN"), cancellable = true)
	public void shockahpi$getBlockReachDistance(CallbackInfoReturnable<Float> cir) {
		if (cir.getReturnValue() == 4.0F) cir.setReturnValue(SAPI.reachGet());
	}
}
