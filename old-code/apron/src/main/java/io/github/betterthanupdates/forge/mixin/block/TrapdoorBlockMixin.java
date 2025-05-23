package io.github.betterthanupdates.forge.mixin.block;

import com.llamalad7.mixinextras.sugar.Local;
import fr.catcore.cursedmixinextensions.annotations.Public;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.world.World;

import io.github.betterthanupdates.forge.world.ForgeWorld;

@Mixin(TrapdoorBlock.class)
public abstract class TrapdoorBlockMixin extends Block {
	@Public
	@Unique
	private static boolean disableValidation = false;
	protected TrapdoorBlockMixin(int blockId, Material material) {
		super(blockId, material);
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Redirect(method = "neighborUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;method_1780(III)Z"))
	private boolean forge$disableValidation(World instance, int j, int k, int i, @Local(ordinal = 4) int l) {
		return !(!disableValidation && !((ForgeWorld) instance).isBlockSolidOnSide(j, k, i, (l & 3) + 2));
	}

	@Inject(method = "canPlaceAt", at = @At("HEAD"), cancellable = true)
	private void forge$cancelPlacement(World i, int j, int k, int l, int par5, CallbackInfoReturnable<Boolean> cir) {
		if (disableValidation) cir.setReturnValue(true);
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Redirect(method = "canPlaceAt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;method_1780(III)Z"))
	private boolean forge$isBlockSolidOnSide(World instance, int j, int k, int i, @Local(ordinal = 3) int l) {
		return ((ForgeWorld) instance).isBlockSolidOnSide(j, k, i, l);
	}
}
