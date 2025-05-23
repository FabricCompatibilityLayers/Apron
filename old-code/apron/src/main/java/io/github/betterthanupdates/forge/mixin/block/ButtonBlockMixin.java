package io.github.betterthanupdates.forge.mixin.block;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.block.Block;
import net.minecraft.block.ButtonBlock;
import net.minecraft.block.Material;
import net.minecraft.world.World;

import io.github.betterthanupdates.forge.world.ForgeWorld;

@Mixin(ButtonBlock.class)
public abstract class ButtonBlockMixin extends Block {
	protected ButtonBlockMixin(int blockId, Material material) {
		super(blockId, material);
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Redirect(method = {"canPlaceAt(Lnet/minecraft/world/World;IIII)Z", "onPlaced"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;method_1780(III)Z", ordinal = 0))
	private boolean forge$canPlaceAt$1(World instance, int j, int k, int i) {
		return ((ForgeWorld) instance).isBlockSolidOnSide(i, j, k, 2);
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Redirect(method = {"canPlaceAt(Lnet/minecraft/world/World;IIII)Z", "onPlaced"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;method_1780(III)Z", ordinal = 1))
	private boolean forge$canPlaceAt$2(World instance, int j, int k, int i) {
		return ((ForgeWorld) instance).isBlockSolidOnSide(i, j, k, 3);
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Redirect(method = {"canPlaceAt(Lnet/minecraft/world/World;IIII)Z", "onPlaced"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;method_1780(III)Z", ordinal = 2))
	private boolean forge$canPlaceAt$3(World instance, int j, int k, int i) {
		return ((ForgeWorld) instance).isBlockSolidOnSide(i, j, k, 4);
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Redirect(method = {"canPlaceAt(Lnet/minecraft/world/World;IIII)Z", "onPlaced"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;method_1780(III)Z", ordinal = 3))
	private boolean forge$canPlaceAt$4(World instance, int j, int k, int i) {
		return ((ForgeWorld) instance).isBlockSolidOnSide(i, j, k, 5);
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Redirect(method = {"canPlaceAt(Lnet/minecraft/world/World;III)Z", "method_1047", "neighborUpdate"},
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;method_1780(III)Z", ordinal = 0))
	private boolean forge$canPlaceAt$5(World instance, int j, int k, int i) {
		return ((ForgeWorld) instance).isBlockSolidOnSide(i, j, k, 5);
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Redirect(method = {"canPlaceAt(Lnet/minecraft/world/World;III)Z", "method_1047", "neighborUpdate"},
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;method_1780(III)Z", ordinal = 1))
	private boolean forge$canPlaceAt$6(World instance, int j, int k, int i) {
		return ((ForgeWorld) instance).isBlockSolidOnSide(i, j, k, 4);
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Redirect(method = {"canPlaceAt(Lnet/minecraft/world/World;III)Z", "method_1047", "neighborUpdate"},
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;method_1780(III)Z", ordinal = 2))
	private boolean forge$canPlaceAt$7(World instance, int j, int k, int i) {
		return ((ForgeWorld) instance).isBlockSolidOnSide(i, j, k, 3);
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Redirect(method = {"canPlaceAt(Lnet/minecraft/world/World;III)Z", "method_1047", "neighborUpdate"},
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;method_1780(III)Z", ordinal = 3))
	private boolean forge$canPlaceAt$8(World instance, int j, int k, int i) {
		return ((ForgeWorld) instance).isBlockSolidOnSide(i, j, k, 2);
	}
}
