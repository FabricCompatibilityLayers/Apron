package io.github.betterthanupdates.forge.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.texture.TextureManager;

import io.github.betterthanupdates.forge.ForgeClientReflection;

@Environment(EnvType.CLIENT)
@Mixin(TextureManager.class)
public abstract class TextureManagerMixin {
	@Inject(method = "getTextureId(Ljava/lang/String;)I", at = @At(value = "INVOKE", target = "Ljava/nio/IntBuffer;clear()Ljava/nio/Buffer;"))
	private void reforged$getTextureId(String s, CallbackInfoReturnable<Integer> cir) {
		if (ForgeClientReflection.Tessellator$renderingWorldRenderer) {
			System.out.printf("Warning: Texture %s not preloaded, will cause render glitches!\n", s);
		}
	}
}
