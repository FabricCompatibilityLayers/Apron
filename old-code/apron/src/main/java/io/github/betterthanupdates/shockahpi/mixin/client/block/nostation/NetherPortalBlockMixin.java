package io.github.betterthanupdates.shockahpi.mixin.client.block.nostation;

import io.github.betterthanupdates.shockahpi.block.ShockAhPINetherPortalBlock;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ClientPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import playerapi.PlayerAPI;
import shockahpi.PlayerBaseSAPI;

@Mixin(NetherPortalBlock.class)
public abstract class NetherPortalBlockMixin implements ShockAhPINetherPortalBlock {
	@Inject(method = "onEntityCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;method_1388()V", shift = At.Shift.BEFORE))
	public void onEntityCollision(World world, int i, int j, int k, Entity entity, CallbackInfo ci) {
		if (entity instanceof ClientPlayerEntity) {
			ClientPlayerEntity entityplayersp = (ClientPlayerEntity) entity;
			PlayerAPI.getPlayerBase(entityplayersp, PlayerBaseSAPI.class).portal = this.getDimNumber();
		}
	}
}
