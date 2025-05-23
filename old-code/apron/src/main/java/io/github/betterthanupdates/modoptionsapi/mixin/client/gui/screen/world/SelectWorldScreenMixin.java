package io.github.betterthanupdates.modoptionsapi.mixin.client.gui.screen.world;

import modoptionsapi.ModOptionsAPI;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SelectWorldScreen.class)
public abstract class SelectWorldScreenMixin {
	@Shadow
	protected abstract String method_1889(int i);

	@Inject(method = "method_1891", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;method_2120(Ljava/lang/String;Ljava/lang/String;J)V"))
	private void modoptionsapi$loadWorld(int i, CallbackInfo ci) {
		ModOptionsAPI.selectedWorld(this.method_1889(i));
	}
}
