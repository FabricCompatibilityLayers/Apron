package io.github.betterthanupdates.forge.mixin.client.nostation;

import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import net.minecraft.SingleplayerInteractionManager;
import net.minecraft.block.Block;
import net.minecraft.client.InteractionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;

import io.github.betterthanupdates.forge.block.ForgeBlock;

@Environment(EnvType.CLIENT)
@Mixin(SingleplayerInteractionManager.class)
public class SingleplayerInteractionManagerMixin extends InteractionManager {
	public SingleplayerInteractionManagerMixin(Minecraft client) {
		super(client);
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Redirect(method = "method_1716", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/entity/player/ClientPlayerEntity;method_514(Lnet/minecraft/block/Block;)Z"))
	private boolean forge$method_1716(ClientPlayerEntity instance, Block block, @Local(ordinal = 5) int meta) {
		return ((ForgeBlock) block).canHarvestBlock(instance, meta);
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Redirect(method = "method_1707", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getHardness(Lnet/minecraft/entity/player/PlayerEntity;)F"))
	private float forge$method_1707(Block instance, PlayerEntity playerEntity, @Local(ordinal = 0) int i, @Local(ordinal = 1) int j, @Local(ordinal = 2) int k) {
		return ((ForgeBlock) instance).blockStrength(this.minecraft.world, playerEntity, i, j, k);
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Redirect(method = "method_1721", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getHardness(Lnet/minecraft/entity/player/PlayerEntity;)F"))
	private float forge$method_1721(Block instance, PlayerEntity playerEntity, @Local(ordinal = 0) int i, @Local(ordinal = 1) int j, @Local(ordinal = 2) int k) {
		return ((ForgeBlock) instance).blockStrength(this.minecraft.world, playerEntity, i, j, k);
	}
}
