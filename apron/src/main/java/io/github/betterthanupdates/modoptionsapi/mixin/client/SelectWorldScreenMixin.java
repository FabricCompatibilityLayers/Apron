package io.github.betterthanupdates.modoptionsapi.mixin.client;

import modoptionsapi.ModOptionsAPI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.screen.SelectWorldScreen;

@Mixin(SelectWorldScreen.class)
public abstract class SelectWorldScreenMixin {
	@Shadow
	protected abstract String getWorldName(int i);

	@Inject(method = "loadWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;createOrLoadWorld(Ljava/lang/String;Ljava/lang/String;J)V"))
	private void modoptionsapi$loadWorld(int i, CallbackInfo ci) {
		ModOptionsAPI.selectedWorld(this.getWorldName(i));
	}
}
