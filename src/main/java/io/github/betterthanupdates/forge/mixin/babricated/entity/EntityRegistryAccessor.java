package io.github.betterthanupdates.forge.mixin.babricated.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Map;

@Mixin(EntityRegistry.class)
public interface EntityRegistryAccessor {

    @Accessor
    static Map<String, Class<? extends Entity>> getSTRING_ID_TO_CLASS() {
        return null;
    }

    @Invoker
    static void callRegister(Class class_, String string, int i) {

    }
}
