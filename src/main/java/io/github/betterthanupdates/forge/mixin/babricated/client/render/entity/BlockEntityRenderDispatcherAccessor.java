package io.github.betterthanupdates.forge.mixin.babricated.client.render.entity;

import net.minecraft.client.render.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.entity.block.BlockEntityRenderer;
import net.minecraft.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(BlockEntityRenderDispatcher.class)
public interface BlockEntityRenderDispatcherAccessor {

    @Accessor
    Map<Class<? extends BlockEntity>, BlockEntityRenderer> getCustomRenderers();
}
