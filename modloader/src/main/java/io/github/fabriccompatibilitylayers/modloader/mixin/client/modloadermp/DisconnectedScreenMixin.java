package io.github.fabriccompatibilitylayers.modloader.mixin.client.modloadermp;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.DisconnectedScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DisconnectedScreen.class)
public class DisconnectedScreenMixin {
	@WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/DisconnectedScreen;drawCenteredTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V", ordinal = 1))
	private void modloadermp$multilineReasons(DisconnectedScreen instance, TextRenderer textRenderer, String s, int width, int height, int color, Operation<Void> original) {
		String[] as = s.split("\n");

		for (int k = 0; k < as.length; ++k) {
			original.call(instance, textRenderer, as[k], width, height + k * 10, color);
		}
	}
}
