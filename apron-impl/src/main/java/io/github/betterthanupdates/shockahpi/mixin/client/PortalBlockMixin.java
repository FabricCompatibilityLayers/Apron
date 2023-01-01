package io.github.betterthanupdates.shockahpi.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import playerapi.PlayerAPI;
import shockahpi.PlayerBaseSAPI;

import net.minecraft.block.PortalBlock;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import io.github.betterthanupdates.shockahpi.block.ShockAhPIPortalBlock;

@Mixin(PortalBlock.class)
public abstract class PortalBlockMixin implements ShockAhPIPortalBlock {
	@Override
	public int getDimNumber() {
		return -1;
	}

	/**
	 * @author SAPI
	 * @reason yes
	 */
	@Overwrite
	public void onEntityCollision(World world, int i, int j, int k, Entity entity) {
		if (entity.vehicle == null && entity.passenger == null) {
			if (entity instanceof AbstractClientPlayerEntity) {
				AbstractClientPlayerEntity entityplayersp = (AbstractClientPlayerEntity) entity;
				((PlayerBaseSAPI) PlayerAPI.getPlayerBase(entityplayersp, PlayerBaseSAPI.class)).portal = this.getDimNumber();
			}

			entity.method_1388();
		}
	}
}
