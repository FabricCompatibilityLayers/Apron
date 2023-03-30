package io.github.betterthanupdates.forge.client.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface ForgeTessellator {
	boolean defaultTexture();

	void defaultTexture(boolean defaultTexture);

	boolean isTessellating();
}
