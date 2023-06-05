package io.github.betterthanupdates.shockahpi.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
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

	@Inject(method = "onEntityCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;method_1388()V", shift = At.Shift.BEFORE))
	public void onEntityCollision(World world, int i, int j, int k, Entity entity, CallbackInfo ci) {
		if (entity instanceof AbstractClientPlayerEntity) {
			AbstractClientPlayerEntity entityplayersp = (AbstractClientPlayerEntity) entity;
			PlayerAPI.getPlayerBase(entityplayersp, PlayerBaseSAPI.class).portal = this.getDimNumber();
		}
	}
}
