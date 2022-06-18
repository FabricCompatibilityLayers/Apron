package io.github.betterthanupdates.forge.client.particle;

import net.minecraft.block.Block;
import net.minecraft.client.entity.particle.DiggingParticleEntity;

public interface ForgeParticleManager {
	void addDigParticleEffect(DiggingParticleEntity dig_effect, Block block);
}
