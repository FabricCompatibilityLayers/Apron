package io.github.betterthanupdates.forge.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Block;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;

import io.github.betterthanupdates.forge.ForgeReflection;
import io.github.betterthanupdates.forge.world.ForgeWorld;

@Mixin(TrapdoorBlock.class)
public abstract class TrapdoorBlockMixin extends Block {
	protected TrapdoorBlockMixin(int blockId, Material material) {
		super(blockId, material);
	}

	int cachedMeta = 0;

	@Inject(method = "onAdjacentBlockUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockMeta(III)I"))
	private void reforged$onAdjacentBlockUpdate(World i, int j, int k, int l, int par5, CallbackInfo ci) {
		this.cachedMeta = i.getBlockMeta(j, k, l);
	}

	@Redirect(method = "onAdjacentBlockUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;canSuffocate(III)Z"))
	private boolean reforged$disableValidation(World instance, int j, int k, int i) {
		return !(!ForgeReflection.TrapdoorBlock$disableValidation && !((ForgeWorld) instance).isBlockSolidOnSide(j, k, i, (this.cachedMeta & 3) + 2));
	}

	int cachedL;

	@Inject(method = "canPlaceAt", at = @At("HEAD"))
	private void reforged$canPlaceAt(World i, int j, int k, int l, int par5, CallbackInfoReturnable<Boolean> cir) {
		this.cachedL = par5;
	}

	@Redirect(method = "canPlaceAt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;canSuffocate(III)Z"))
	private boolean reforged$isBlockSolidOnSide(World instance, int j, int k, int i) {
		return ((ForgeWorld) instance).isBlockSolidOnSide(j, k, i, this.cachedL);
	}
}
