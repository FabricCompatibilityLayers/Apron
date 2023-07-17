package io.github.betterthanupdates.stapi;

public interface StAPIMinecraftClient {
	default int apron$stapi$registerTextureOverride(String target, String textureFile) {
		return -1;
	}
	default void apron$stapi$preloadTexture(String texture) {}
}
