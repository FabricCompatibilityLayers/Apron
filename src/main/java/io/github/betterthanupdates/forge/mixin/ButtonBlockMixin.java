package io.github.betterthanupdates.forge.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.block.Block;
import net.minecraft.block.ButtonBlock;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;

import io.github.betterthanupdates.forge.world.ForgeWorld;

@Mixin(ButtonBlock.class)
public abstract class ButtonBlockMixin extends Block {
	protected ButtonBlockMixin(int blockId, Material material) {
		super(blockId, material);
	}

	@Redirect(method = {"canPlaceAt(Lnet/minecraft/world/World;IIII)Z", "onBlockPlaced"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;canSuffocate(III)Z", ordinal = 0))
	private boolean reforged$canPlaceAt$1(World instance, int j, int k, int i) {
		return ((ForgeWorld) instance).isBlockSolidOnSide(i, j, k, 2);
	}

	@Redirect(method = {"canPlaceAt(Lnet/minecraft/world/World;IIII)Z", "onBlockPlaced"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;canSuffocate(III)Z", ordinal = 1))
	private boolean reforged$canPlaceAt$2(World instance, int j, int k, int i) {
		return ((ForgeWorld) instance).isBlockSolidOnSide(i, j, k, 3);
	}

	@Redirect(method = {"canPlaceAt(Lnet/minecraft/world/World;IIII)Z", "onBlockPlaced"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;canSuffocate(III)Z", ordinal = 2))
	private boolean reforged$canPlaceAt$3(World instance, int j, int k, int i) {
		return ((ForgeWorld) instance).isBlockSolidOnSide(i, j, k, 4);
	}

	@Redirect(method = {"canPlaceAt(Lnet/minecraft/world/World;IIII)Z", "onBlockPlaced"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;canSuffocate(III)Z", ordinal = 3))
	private boolean reforged$canPlaceAt$4(World instance, int j, int k, int i) {
		return ((ForgeWorld) instance).isBlockSolidOnSide(i, j, k, 5);
	}

	@Redirect(method = {"canPlaceAt(Lnet/minecraft/world/World;III)Z", "method_1047", "onAdjacentBlockUpdate"},
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;canSuffocate(III)Z", ordinal = 0))
	private boolean reforged$canPlaceAt$5(World instance, int j, int k, int i) {
		return ((ForgeWorld) instance).isBlockSolidOnSide(i, j, k, 5);
	}

	@Redirect(method = {"canPlaceAt(Lnet/minecraft/world/World;III)Z", "method_1047", "onAdjacentBlockUpdate"},
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;canSuffocate(III)Z", ordinal = 1))
	private boolean reforged$canPlaceAt$6(World instance, int j, int k, int i) {
		return ((ForgeWorld) instance).isBlockSolidOnSide(i, j, k, 4);
	}

	@Redirect(method = {"canPlaceAt(Lnet/minecraft/world/World;III)Z", "method_1047", "onAdjacentBlockUpdate"},
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;canSuffocate(III)Z", ordinal = 2))
	private boolean reforged$canPlaceAt$7(World instance, int j, int k, int i) {
		return ((ForgeWorld) instance).isBlockSolidOnSide(i, j, k, 3);
	}

	@Redirect(method = {"canPlaceAt(Lnet/minecraft/world/World;III)Z", "method_1047", "onAdjacentBlockUpdate"},
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;canSuffocate(III)Z", ordinal = 3))
	private boolean reforged$canPlaceAt$8(World instance, int j, int k, int i) {
		return ((ForgeWorld) instance).isBlockSolidOnSide(i, j, k, 2);
	}
}
