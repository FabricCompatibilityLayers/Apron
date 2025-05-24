package io.github.fabriccompatibilitylayers.modloader.mixin.client.modloader;

import net.minecraft.client.resource.pack.TexturePacks;
import net.minecraft.client.texture.TextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TextureManager.class)
public interface TextureManagerAccessor {
	@Accessor("texturePacks")
	TexturePacks getTexturePacks();
}
