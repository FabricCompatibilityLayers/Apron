package io.github.betterthanupdates.forge.mixin;

import io.github.betterthanupdates.forge.ForgeClientReflection;
import net.legacyfabric.fabric.api.logger.v1.Logger;
import net.minecraft.client.TexturePackManager;
import net.minecraft.client.resource.TexturePack;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.GLAllocationUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.nio.Buffer;
import java.nio.IntBuffer;
import java.util.HashMap;

@Mixin(TextureManager.class)
public abstract class TextureManagerMixin {

    @Shadow public TexturePackManager texturePackManager;

    @Shadow private HashMap textures;

    @Shadow private IntBuffer field_1249;

    @Shadow public abstract void bindImageToId(BufferedImage bufferedImage, int i);

    @Shadow protected abstract BufferedImage method_1101(BufferedImage bufferedImage);

    @Shadow protected abstract BufferedImage readImage(InputStream inputStream);

    @Shadow private boolean isClampTexture;

    @Shadow private boolean isBlurTexture;

    @Shadow private BufferedImage missingTexImage;

    /**
     * @author Forge
     * @reason Add Forge warning
     */
    @Overwrite
    public int getTextureId(String s) {
        TexturePack texturePack = this.texturePackManager.texturePack;
        Integer integer = (Integer)this.textures.get(s);
        if (integer != null) {
            return integer;
        } else {
            try {
                if (ForgeClientReflection.Tessellator$renderingWorldRenderer) {
                    Logger.get("Babricated Forge", "Minecraft Forge").warn("Texture %s not preloaded, will cause render glitches!", s);
                }

                ((Buffer)this.field_1249).clear();
                GLAllocationUtils.genTextures(this.field_1249);
                int i = this.field_1249.get(0);
                if (s.startsWith("##")) {
                    this.bindImageToId(this.method_1101(this.readImage(texturePack.getResourceAsStream(s.substring(2)))), i);
                } else if (s.startsWith("%clamp%")) {
                    this.isClampTexture = true;
                    this.bindImageToId(this.readImage(texturePack.getResourceAsStream(s.substring(7))), i);
                    this.isClampTexture = false;
                } else if (s.startsWith("%blur%")) {
                    this.isBlurTexture = true;
                    this.bindImageToId(this.readImage(texturePack.getResourceAsStream(s.substring(6))), i);
                    this.isBlurTexture = false;
                } else {
                    InputStream inputstream = texturePack.getResourceAsStream(s);
                    if (inputstream == null) {
                        this.bindImageToId(this.missingTexImage, i);
                    } else {
                        this.bindImageToId(this.readImage(inputstream), i);
                    }
                }

                this.textures.put(s, i);
                return i;
            } catch (RuntimeException var6) {
                var6.printStackTrace();
                GLAllocationUtils.genTextures(this.field_1249);
                int j = this.field_1249.get(0);
                this.bindImageToId(this.missingTexImage, j);
                this.textures.put(s, j);
                return j;
            }
        }
    }

    /**
     * @author Forge
     * @reason short -> char
     */
    @Overwrite
    private int method_1098(int i, int j) {
        int k = (i & 0xFF000000) >> 24 & 0xFF;
        int l = (j & 0xFF000000) >> 24 & 0xFF;
        char c = 255;
        if (k + l == 0) {
            k = 1;
            l = 1;
            c = 0;
        }

        int i1 = (i >> 16 & 0xFF) * k;
        int j1 = (i >> 8 & 0xFF) * k;
        int k1 = (i & 0xFF) * k;
        int l1 = (j >> 16 & 0xFF) * l;
        int i2 = (j >> 8 & 0xFF) * l;
        int j2 = (j & 0xFF) * l;
        int k2 = (i1 + l1) / (k + l);
        int l2 = (j1 + i2) / (k + l);
        int i3 = (k1 + j2) / (k + l);
        return c << 24 | k2 << 16 | l2 << 8 | i3;
    }
}
