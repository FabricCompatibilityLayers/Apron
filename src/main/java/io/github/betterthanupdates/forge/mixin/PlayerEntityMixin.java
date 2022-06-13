package io.github.betterthanupdates.forge.mixin;

import io.github.betterthanupdates.forge.entity.player.ForgePlayerEntity;
import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements ForgePlayerEntity {
	@Shadow
	public abstract float getStrengh(Block arg);

	private PlayerEntityMixin(World arg) {
		super(arg);
	}

	/**
	 * Gets the strength of the player against the block, taking
	 * into account the meta value of the block state.
	 *
	 * @param block the block id to check strength against
	 * @param meta  the meta value of the block state
	 * @return the strength of the player against the given block
	 */
	@Override
	public float getCurrentPlayerStrVsBlock(Block block, int meta) {
		return this.getStrengh(block);
	}
}
