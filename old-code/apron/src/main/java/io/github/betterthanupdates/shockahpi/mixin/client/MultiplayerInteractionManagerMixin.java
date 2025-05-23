package io.github.betterthanupdates.shockahpi.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import shockahpi.SAPI;
import net.minecraft.MultiplayerInteractionManager;
import net.minecraft.client.InteractionManager;
import net.minecraft.client.Minecraft;

@Mixin(MultiplayerInteractionManager.class)
public class MultiplayerInteractionManagerMixin extends InteractionManager {
	public MultiplayerInteractionManagerMixin(Minecraft minecraft) {
		super(minecraft);
	}

	@ModifyReturnValue(method = "method_1715", at = @At("RETURN"))
	public float shockahpi$getBlockReachDistance(float reach) {
		if (reach == 4.0F) return SAPI.reachGet();

		return reach;
	}
}
