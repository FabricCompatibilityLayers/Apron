package io.github.betterthanupdates.forge.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import shockahpi.SAPI;

import net.minecraft.block.Block;
import net.minecraft.client.ClientInteractionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MultiplayerClientInteractionManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import io.github.betterthanupdates.forge.block.ForgeBlock;
import io.github.betterthanupdates.forge.item.ForgeItem;

@Environment(EnvType.CLIENT)
@Mixin(MultiplayerClientInteractionManager.class)
public abstract class MultiplayerClientInteractionManagerMixin extends ClientInteractionManager {
	public MultiplayerClientInteractionManagerMixin(Minecraft client) {
		super(client);
	}

	@Inject(method = "method_1716", at = @At("HEAD"), cancellable = true)
	private void reforged$method_1716(int i, int j, int k, int l, CallbackInfoReturnable<Boolean> cir) {
		ItemStack itemstack = this.client.player.getHeldItem();
		if (itemstack != null && ((ForgeItem) itemstack.getItem()).onBlockStartBreak(itemstack, i, j, k, this.client.player)) {
			cir.setReturnValue(false);
		}
	}

	int cachedI, cachedJ, cachedK;
	@Inject(method = "method_1707", at = @At("HEAD"))
	private void reforged$method_1707(int j, int k, int l, int par4, CallbackInfo ci) {
		this.cachedI = j;
		this.cachedJ = k;
		this.cachedK = l;
	}

	@Redirect(method = "method_1707", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getHardness(Lnet/minecraft/entity/player/PlayerEntity;)F"))
	private float reforged$method_1707(Block instance, PlayerEntity playerEntity) {
		return ((ForgeBlock) instance).blockStrength(this.client.world, playerEntity, this.cachedI, this.cachedJ, this.cachedK);
	}

	int cachedI2, cachedJ2, cachedK2;
	@Inject(method = "method_1721", at = @At("HEAD"))
	private void reforged$method_1721(int j, int k, int l, int par4, CallbackInfo ci) {
		this.cachedI2 = j;
		this.cachedJ2 = k;
		this.cachedK2 = l;
	}

	@Redirect(method = "method_1721", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getHardness(Lnet/minecraft/entity/player/PlayerEntity;)F"))
	private float reforged$method_1721(Block instance, PlayerEntity playerEntity) {
		return ((ForgeBlock) instance).blockStrength(this.client.world, playerEntity, this.cachedI2, this.cachedJ2, this.cachedK2);
	}

	@Inject(method = "getBlockReachDistance", at = @At("RETURN"), cancellable = true)
	public void reforged$getBlockReachDistance(CallbackInfoReturnable<Float> cir) {
		if (cir.getReturnValue() == 4.0F) cir.setReturnValue(SAPI.reachGet());
	}
}
