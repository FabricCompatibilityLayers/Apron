package io.github.betterthanupdates.modloader.mixin.client.gui.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.client.gui.screen.Screen;

@Environment(EnvType.CLIENT)
@Mixin(DisconnectedScreen.class)
public abstract class DisconnectedScreenMixin extends Screen {
	@Shadow
	private String reason;

	/**
	 * @author Risugami
	 * @reason ModLoaderMP expects multi-line disconnect messages,
	 * and they need to be displayed correctly.
	 */
	@Inject(method = "render", cancellable = true, at = @At(value = "INVOKE", ordinal = 0,
			target = "Lnet/minecraft/client/gui/screen/DisconnectedScreen;drawCenteredTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V"))
	private void modloader$render(int j, int f, float delta, CallbackInfo ci) {
		String[] descriptionSplit = this.reason.split("\n");

		for (int k = 0; k < descriptionSplit.length; k++) {
			this.drawCenteredTextWithShadow(this.textRenderer, descriptionSplit[k],
					this.width / 2, this.height / 2 - 10 + k * 10, 16777215);
		}

		super.render(j, f, delta);
		ci.cancel();
	}
}
