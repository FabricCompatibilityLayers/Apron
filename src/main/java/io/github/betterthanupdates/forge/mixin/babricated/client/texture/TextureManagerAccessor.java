package io.github.betterthanupdates.forge.mixin.babricated.client.texture;

import net.minecraft.client.TexturePackManager;
import net.minecraft.client.texture.TextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TextureManager.class)
public interface TextureManagerAccessor {

    @Accessor
    TexturePackManager getTexturePackManager();
}
