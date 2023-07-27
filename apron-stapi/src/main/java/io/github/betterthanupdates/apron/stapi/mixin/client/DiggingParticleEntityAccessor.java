package io.github.betterthanupdates.apron.stapi.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.block.Block;
import net.minecraft.client.entity.particle.DiggingParticleEntity;

@Mixin(DiggingParticleEntity.class)
public interface DiggingParticleEntityAccessor {
	@Accessor
	public Block getBlock();
}
