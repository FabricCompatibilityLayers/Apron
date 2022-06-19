package io.github.betterthanupdates.shockahpi.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import shockahpi.SapiClientPlayerEntity;

import net.minecraft.block.PortalBlock;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import io.github.betterthanupdates.shockahpi.block.ShockAhPIPortalBlock;

@Mixin(PortalBlock.class)
public abstract class PortalBlockMixin implements ShockAhPIPortalBlock {
	@Override
	public int getDimNumber() {
		return -1;
	}

	@Inject(method = "onEntityCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;method_1388()V"))
	private void sapi$onEntityCollision(World i, int j, int k, int arg2, Entity par5, CallbackInfo ci) {
		if (par5 instanceof SapiClientPlayerEntity) {
			((SapiClientPlayerEntity)par5).portal = this.getDimNumber();
		}
	}
}
