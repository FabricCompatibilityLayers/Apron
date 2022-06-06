package io.github.betterthanupdates.forge.mixin;

import org.spongepowered.asm.mixin.Mixin;

import io.github.betterthanupdates.forge.ForgePlayerEntity;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements ForgePlayerEntity {
	public float getCurrentPlayerStrVsBlock(Block block, int meta) {
		return 0f;
	}
}
