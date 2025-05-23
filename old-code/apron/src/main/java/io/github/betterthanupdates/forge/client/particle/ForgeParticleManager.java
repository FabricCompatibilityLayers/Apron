package io.github.betterthanupdates.forge.client.particle;

import net.minecraft.block.Block;
import net.minecraft.client.particle.BlockParticle;

public interface ForgeParticleManager {
	void addDigParticleEffect(BlockParticle dig_effect, Block block);
}
