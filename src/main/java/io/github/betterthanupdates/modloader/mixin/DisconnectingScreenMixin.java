package io.github.betterthanupdates.modloader.mixin;

import net.minecraft.client.gui.screen.DisconnectingScreen;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DisconnectingScreen.class)
public abstract class DisconnectingScreenMixin extends Screen {
	@Shadow
	private String line2;

	@Inject(method = "render", cancellable = true, at = @At(value = "INVOKE", shift = At.Shift.BEFORE,
			target = "Lnet/minecraft/client/gui/screen/DisconnectingScreen;drawTextWithShadowCentred(Lnet/minecraft/client/render/TextRenderer;Ljava/lang/String;III)V"))
	private void modloader$render(int j, int f, float delta, CallbackInfo ci) {
		String[] as = this.line2.split("\n");

		for(int k = 0; k < as.length; ++k) {
			this.drawTextWithShadowCentred(this.textRenderer, as[k],
					this.width / 2, this.height / 2 - 10 + k * 10, 16777215);
		}

		super.render(j, f, delta);
		ci.cancel();
	}
}
