package io.github.betterthanupdates.forge.mixin.server.nostation;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.betterthanupdates.forge.block.ForgeBlock;
import net.minecraft.block.Block;
import net.minecraft.class_70;
import net.minecraft.class_73;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(class_70.class)
public class ServerInteractionManagerMixin {
	@Shadow
	private class_73 field_2310;

	@Shadow
	private int field_2318;

	@Shadow
	private int field_2319;

	@Shadow
	private int field_2320;

	@Redirect(method = "method_1828", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getHardness(Lnet/minecraft/entity/player/PlayerEntity;)F"))
	private float forge$method_1828$blockStrength(Block instance, PlayerEntity playerEntity) {
		return ((ForgeBlock) instance).blockStrength(this.field_2310, playerEntity, this.field_2318, this.field_2319, this.field_2320);
	}

	@Redirect(method = {"method_1830", "method_1829"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getHardness(Lnet/minecraft/entity/player/PlayerEntity;)F", ordinal = 0))
	private float forge$blockStrength(Block instance, PlayerEntity playerEntity, @Local(ordinal = 0) int i, @Local(ordinal = 1) int j, @Local(ordinal = 2) int k) {
		return ((ForgeBlock) instance).blockStrength(this.field_2310, playerEntity, i, j, k);
	}

	@Redirect(method = "method_1834", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;method_514(Lnet/minecraft/block/Block;)Z"))
	private boolean forge$canHarvestBlock(PlayerEntity instance, Block block, @Local(ordinal = 4) int meta) {
		return ((ForgeBlock) block).canHarvestBlock(instance, meta);
	}
}
