package io.github.betterthanupdates.forge.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.Block;
import net.minecraft.client.ClientInteractionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MultiplayerClientInteractionManager;
import net.minecraft.entity.player.PlayerEntity;

import io.github.betterthanupdates.forge.block.ForgeBlock;

@Environment(EnvType.CLIENT)
@Mixin(MultiplayerClientInteractionManager.class)
public abstract class MultiplayerClientInteractionManagerMixin extends ClientInteractionManager {
	public MultiplayerClientInteractionManagerMixin(Minecraft client) {
		super(client);
	}

	int cachedI, cachedJ, cachedK;

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Inject(method = "destroyFireAndBreakBlock", at = @At("HEAD"))
	private void forge$method_1707(int j, int k, int l, int par4, CallbackInfo ci) {
		this.cachedI = j;
		this.cachedJ = k;
		this.cachedK = l;
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Redirect(method = "destroyFireAndBreakBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getHardness(Lnet/minecraft/entity/player/PlayerEntity;)F"))
	private float forge$method_1707(Block instance, PlayerEntity playerEntity) {
		return ((ForgeBlock) instance).blockStrength(this.client.world, playerEntity, this.cachedI, this.cachedJ, this.cachedK);
	}

	int cachedI2, cachedJ2, cachedK2;

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Inject(method = "dig", at = @At("HEAD"))
	private void forge$method_1721(int j, int k, int l, int par4, CallbackInfo ci) {
		this.cachedI2 = j;
		this.cachedJ2 = k;
		this.cachedK2 = l;
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Redirect(method = "dig", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getHardness(Lnet/minecraft/entity/player/PlayerEntity;)F"))
	private float forge$method_1721(Block instance, PlayerEntity playerEntity) {
		return ((ForgeBlock) instance).blockStrength(this.client.world, playerEntity, this.cachedI2, this.cachedJ2, this.cachedK2);
	}
}
