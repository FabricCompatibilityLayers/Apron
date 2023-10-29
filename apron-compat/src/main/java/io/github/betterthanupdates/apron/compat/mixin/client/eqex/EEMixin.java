package io.github.betterthanupdates.apron.compat.mixin.client.eqex;

import forge.MinecraftForgeClient;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.mod_EE;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(mod_EE.class)
public class EEMixin {
	@Inject(method = "<clinit>", at = @At("RETURN"))
	private static void registerTextureAtlas(CallbackInfo ci) {
		MinecraftForgeClient.preloadTexture("/eqex/eqitems.png");
		MinecraftForgeClient.preloadTexture("/eqex/eqterrain.png");
	}

	@Redirect(method = {"RenderInvBlock", "RenderWorldBlock"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/texture/TextureManager;bindTexture(I)V"))
	private void apron$redirectBindTextureCalls(TextureManager instance, int i) {}

	@Redirect(method = {"RenderInvBlock", "RenderWorldBlock"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/texture/TextureManager;getTextureId(Ljava/lang/String;)I"))
	private int apron$redirectGetTextureCalls(TextureManager instance, String s) {
		return -1;
	}
}
