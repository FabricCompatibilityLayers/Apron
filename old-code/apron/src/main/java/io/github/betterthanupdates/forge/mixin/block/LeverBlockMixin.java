package io.github.betterthanupdates.forge.mixin.block;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.block.Block;
import net.minecraft.block.LeverBlock;
import net.minecraft.block.Material;
import net.minecraft.world.World;

import io.github.betterthanupdates.forge.world.ForgeWorld;

@Mixin(LeverBlock.class)
public abstract class LeverBlockMixin extends Block {
	protected LeverBlockMixin(int blockId, Material material) {
		super(blockId, material);
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Redirect(method = {"canPlaceAt(Lnet/minecraft/world/World;III)Z", "neighborUpdate"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;method_1780(III)Z", ordinal = 0))
	private boolean forge$canPlaceAt$1(World instance, int j, int k, int i) {
		return ((ForgeWorld) instance).isBlockSolidOnSide(j, k, i, 5);
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Redirect(method = {"canPlaceAt(Lnet/minecraft/world/World;III)Z", "neighborUpdate"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;method_1780(III)Z", ordinal = 1))
	private boolean forge$canPlaceAt$2(World instance, int j, int k, int i) {
		return ((ForgeWorld) instance).isBlockSolidOnSide(j, k, i, 4);
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Redirect(method = {"canPlaceAt(Lnet/minecraft/world/World;III)Z", "neighborUpdate"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;method_1780(III)Z", ordinal = 2))
	private boolean forge$canPlaceAt$3(World instance, int j, int k, int i) {
		return ((ForgeWorld) instance).isBlockSolidOnSide(j, k, i, 3);
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Redirect(method = {"canPlaceAt(Lnet/minecraft/world/World;III)Z", "neighborUpdate"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;method_1780(III)Z", ordinal = 3))
	private boolean forge$canPlaceAt$4(World instance, int j, int k, int i) {
		return ((ForgeWorld) instance).isBlockSolidOnSide(j, k, i, 2);
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Redirect(method = {"canPlaceAt(Lnet/minecraft/world/World;III)Z", "neighborUpdate"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;method_1780(III)Z", ordinal = 4))
	private boolean forge$canPlaceAt$5(World instance, int j, int k, int i) {
		return ((ForgeWorld) instance).isBlockSolidOnSide(j, k, i, 1);
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Redirect(method = {"neighborUpdate"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;method_1780(III)Z", ordinal = 5))
	private boolean forge$onAdjacentBlockUpdate$6(World instance, int j, int k, int i) {
		return ((ForgeWorld) instance).isBlockSolidOnSide(j, k, i, 1);
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Redirect(method = {"canPlaceAt(Lnet/minecraft/world/World;IIII)Z", "onPlaced"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;method_1780(III)Z", ordinal = 0))
	private boolean forge$isBlockSolidOnSide$0(World instance, int j, int k, int i) {
		return ((ForgeWorld) instance).isBlockSolidOnSide(j, k, i, 1);
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Redirect(method = {"canPlaceAt(Lnet/minecraft/world/World;IIII)Z", "onPlaced"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;method_1780(III)Z", ordinal = 1))
	private boolean forge$isBlockSolidOnSide$1(World instance, int j, int k, int i) {
		return ((ForgeWorld) instance).isBlockSolidOnSide(j, k, i, 2);
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Redirect(method = {"canPlaceAt(Lnet/minecraft/world/World;IIII)Z", "onPlaced"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;method_1780(III)Z", ordinal = 2))
	private boolean forge$isBlockSolidOnSide$2(World instance, int j, int k, int i) {
		return ((ForgeWorld) instance).isBlockSolidOnSide(j, k, i, 3);
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Redirect(method = {"canPlaceAt(Lnet/minecraft/world/World;IIII)Z", "onPlaced"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;method_1780(III)Z", ordinal = 3))
	private boolean forge$isBlockSolidOnSide$3(World instance, int j, int k, int i) {
		return ((ForgeWorld) instance).isBlockSolidOnSide(j, k, i, 4);
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Redirect(method = {"canPlaceAt(Lnet/minecraft/world/World;IIII)Z", "onPlaced"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;method_1780(III)Z", ordinal = 4))
	private boolean forge$isBlockSolidOnSide$4(World instance, int j, int k, int i) {
		return ((ForgeWorld) instance).isBlockSolidOnSide(j, k, i, 5);
	}
}
