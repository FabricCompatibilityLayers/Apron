package io.github.betterthanupdates.apron.stapi.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.Minecraft;

import io.github.betterthanupdates.apron.stapi.ApronStAPICompat;
import io.github.betterthanupdates.stapi.StAPIMinecraftClient;

@Mixin(Minecraft.class)
public class MinecraftClientMixin implements StAPIMinecraftClient {
	@Override
	public int apron$stapi$registerTextureOverride(String target, String textureFile) {
		return ApronStAPICompat.registerTextureOverride(target, textureFile);
	}

	@Override
	public void apron$stapi$preloadTexture(String texture) {
		ApronStAPICompat.preloadTexture(texture);
	}
}
