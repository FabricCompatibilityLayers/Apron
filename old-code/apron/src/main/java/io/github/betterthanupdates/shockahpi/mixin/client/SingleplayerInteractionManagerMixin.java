package io.github.betterthanupdates.shockahpi.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import shockahpi.Loc;
import shockahpi.SAPI;
import net.minecraft.SingleplayerInteractionManager;
import net.minecraft.client.InteractionManager;
import net.minecraft.client.Minecraft;

@Mixin(SingleplayerInteractionManager.class)
public abstract class SingleplayerInteractionManagerMixin extends InteractionManager {
	public SingleplayerInteractionManagerMixin(Minecraft client) {
		super(client);
	}

	/**
	 * @author ShockAh
	 * @reason Implement ShockAhPI function
	 */
	@ModifyReturnValue(method = "method_1715", at = @At("RETURN"))
	public float shockahpi$getBlockReachDistance(float reach) {
		if (reach == 4.0F) return SAPI.reachGet();

		return reach;
	}

	/**
	 * @author ShockAh
	 * @reason Implement ShockAhPI function
	 */
	@Inject(method = "method_1716", cancellable = true, at = @At(value = "INVOKE", shift = At.Shift.BEFORE,
			target = "Lnet/minecraft/block/Block;afterBreak(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;IIII)V")
	)
	public void shockahpi$breakBlock_beforeAfterBreak(int x, int y, int z, int par4, CallbackInfoReturnable<Boolean> cir) {
		int blockId = this.minecraft.world.getBlockId(x, y, z);
		int meta = this.minecraft.world.getBlockMeta(x, y, z);

		if (SAPI.interceptHarvest(this.minecraft.world, this.minecraft.player, new Loc(x, y, z), blockId, meta)) {
			cir.setReturnValue(true);
		}
	}
}
