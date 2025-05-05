package io.github.betterthanupdates.reforged.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_555;
import net.minecraft.client.render.WorldRenderer;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import reforged.ReforgedHooksClient;

@Environment(EnvType.CLIENT)
@Mixin(class_555.class) // EntityRenderer
public class RenderWorldLastMixin {

	@Inject(method = "method_1841",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/class_555;method_1842(IF)V", ordinal = 4))
	private void reforged$pushCloudMatrix(float f, long l, CallbackInfo ci) {
		GL11.glPushMatrix();
	}

	@Inject(method = "method_1841",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/class_555;method_1842(IF)V", ordinal = 5,
					shift = At.Shift.AFTER))
	private void reforged$popCloudMatrixAndDraw(float f, long l, CallbackInfo ci, @Local WorldRenderer var5) {
		GL11.glPopMatrix();
		ReforgedHooksClient.onRenderWorldLast(var5, f);
	}

}
