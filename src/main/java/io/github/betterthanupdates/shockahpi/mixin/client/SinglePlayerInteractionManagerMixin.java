package io.github.betterthanupdates.shockahpi.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import shockahpi.Loc;
import shockahpi.SAPI;

import net.minecraft.client.ClientInteractionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.SingleplayerInteractionManager;

@Mixin(SingleplayerInteractionManager.class)
public abstract class SinglePlayerInteractionManagerMixin extends ClientInteractionManager {
	public SinglePlayerInteractionManagerMixin(Minecraft client) {
		super(client);
	}

	/**
	 * @author ShockAh
	 * @reason Implement ShockAhPI function
	 */
	@Inject(method = "getBlockReachDistance", cancellable = true, at = @At("RETURN"))
	public void shockahpi$getBlockReachDistance(CallbackInfoReturnable<Float> cir) {
		if (cir.getReturnValue() == 4.0F) cir.setReturnValue(SAPI.reachGet());
	}

	/**
	 * @author ShockAh
	 * @reason Implement ShockAhPI function
	 */
	@Inject(method = "method_1716", cancellable = true, at = @At(value = "INVOKE", shift = At.Shift.BEFORE,
			target = "Lnet/minecraft/block/Block;afterBreak(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;IIII)V")
	)
	public void shockahpi$breakBlock_beforeAfterBreak(int x, int y, int z, int par4, CallbackInfoReturnable<Boolean> cir) {
		int blockId = this.client.world.getBlockId(x, y, z);
		int meta = this.client.world.getBlockMeta(x, y, z);

		if (SAPI.interceptHarvest(this.client.world, this.client.player, new Loc(x, y, z), blockId, meta)) {
			cir.setReturnValue(true);
		}
	}
}
