package io.github.betterthanupdates.modloader.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.DisconnectingScreen;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(value= EnvType.CLIENT)
@Mixin(DisconnectingScreen.class)
public abstract class DisconnectingScreenMixin extends Screen {
	@Shadow
	private String line2;

	/**
	 * @author Risugami
	 * @reason ModLoaderMP expects multi-line disconnect messages,
	 * and they need to be displayed correctly.
	 */
	@Inject(method = "render", cancellable = true, at = @At(value = "INVOKE", shift = At.Shift.BEFORE,
			target = "Lnet/minecraft/client/gui/screen/DisconnectingScreen;drawTextWithShadowCentred(Lnet/minecraft/client/render/TextRenderer;Ljava/lang/String;III)V"))
	private void modloader$render(int j, int f, float delta, CallbackInfo ci) {
		String[] descriptionSplit = this.line2.split("\n");

		for (int k = 0; k < descriptionSplit.length; k++) {
			this.drawTextWithShadowCentred(this.textRenderer, descriptionSplit[k],
					this.width / 2, this.height / 2 - 10 + k * 10, 16777215);
		}

		super.render(j, f, delta);
		ci.cancel();
	}
}
