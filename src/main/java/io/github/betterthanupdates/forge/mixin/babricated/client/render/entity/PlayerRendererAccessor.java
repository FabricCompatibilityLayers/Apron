package io.github.betterthanupdates.forge.mixin.babricated.client.render.entity;

import net.minecraft.client.render.entity.PlayerRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PlayerRenderer.class)
public interface PlayerRendererAccessor {

    @Accessor
    static String[] getArmorTypes() {
        return new String[0];
    }

    @Accessor
    static void setArmorTypes(String[] armorTypes) {}
}
