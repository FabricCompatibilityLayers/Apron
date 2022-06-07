package io.github.betterthanupdates.forge.mixin.babricated.entity;

import net.minecraft.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockEntity.class)
public interface BlockEntityAccessor {

    @Invoker
    static void callRegister(Class class_, String string) {

    }
}
