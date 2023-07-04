package io.github.betterthanupdates.forge.mixin;

import fr.catcore.modremapperapi.api.mixin.Public;
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

import io.github.betterthanupdates.forge.world.ForgeWorld;

@Mixin(TrapdoorBlock.class)
public abstract class TrapdoorBlockMixin extends Block {
	@Public
	private static boolean disableValidation = false;
	protected TrapdoorBlockMixin(int blockId, Material material) {
		super(blockId, material);
	}

	int cachedMeta = 0;

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Inject(method = "onAdjacentBlockUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockMeta(III)I"))
	private void forge$onAdjacentBlockUpdate(World i, int j, int k, int l, int par5, CallbackInfo ci) {
		this.cachedMeta = i.getBlockMeta(j, k, l);
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Redirect(method = "onAdjacentBlockUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;canSuffocate(III)Z"))
	private boolean forge$disableValidation(World instance, int j, int k, int i) {
		return !(!disableValidation && !((ForgeWorld) instance).isBlockSolidOnSide(j, k, i, (this.cachedMeta & 3) + 2));
	}

	int cachedL;

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Inject(method = "canPlaceAt", at = @At("HEAD"))
	private void forge$canPlaceAt(World i, int j, int k, int l, int par5, CallbackInfoReturnable<Boolean> cir) {
		this.cachedL = par5;
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Redirect(method = "canPlaceAt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;canSuffocate(III)Z"))
	private boolean forge$isBlockSolidOnSide(World instance, int j, int k, int i) {
		return ((ForgeWorld) instance).isBlockSolidOnSide(j, k, i, this.cachedL);
	}
}
