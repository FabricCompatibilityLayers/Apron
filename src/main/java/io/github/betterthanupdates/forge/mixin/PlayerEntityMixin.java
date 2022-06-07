package io.github.betterthanupdates.forge.mixin;

import io.github.betterthanupdates.forge.ForgePlayerEntity;
import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements ForgePlayerEntity {
	private PlayerEntityMixin(World arg) {
		super(arg);
	}

	@Override
	public float getCurrentPlayerStrVsBlock(Block block, int meta) {
		return 0f;
	}
}
