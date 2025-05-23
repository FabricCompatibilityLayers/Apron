package io.github.betterthanupdates.forge.mixin.block;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.TorchBlock;
import net.minecraft.world.World;

import io.github.betterthanupdates.forge.world.ForgeWorld;

@Mixin(TorchBlock.class)
public abstract class TorchBlockMixin extends Block {
	protected TorchBlockMixin(int blockId, Material material) {
		super(blockId, material);
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Redirect(method = "method_1674", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;method_1780(III)Z"))
	private boolean forge$isBlockSolidOnSide$0(World instance, int j, int k, int i) {
		return ((ForgeWorld) instance).isBlockSolidOnSide(j, k, i, 1);
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Redirect(method = {"canPlaceAt", "onPlaced(Lnet/minecraft/world/World;III)V", "neighborUpdate"},
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;method_1780(III)Z", ordinal = 0))
	private boolean forge$canPlaceAt$1(World instance, int j, int k, int i) {
		return ((ForgeWorld) instance).isBlockSolidOnSide(j, k, i, 5);
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Redirect(method = {"canPlaceAt", "onPlaced(Lnet/minecraft/world/World;III)V", "neighborUpdate"},
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;method_1780(III)Z", ordinal = 1))
	private boolean forge$canPlaceAt$2(World instance, int j, int k, int i) {
		return ((ForgeWorld) instance).isBlockSolidOnSide(j, k, i, 4);
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Redirect(method = {"canPlaceAt", "onPlaced(Lnet/minecraft/world/World;III)V", "neighborUpdate"},
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;method_1780(III)Z", ordinal = 2))
	private boolean forge$canPlaceAt$3(World instance, int j, int k, int i) {
		return ((ForgeWorld) instance).isBlockSolidOnSide(j, k, i, 3);
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Redirect(method = {"canPlaceAt", "onPlaced(Lnet/minecraft/world/World;III)V", "neighborUpdate"},
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;method_1780(III)Z", ordinal = 3))
	private boolean forge$canPlaceAt$4(World instance, int j, int k, int i) {
		return ((ForgeWorld) instance).isBlockSolidOnSide(j, k, i, 2);
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Redirect(method = "onPlaced(Lnet/minecraft/world/World;IIII)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;method_1780(III)Z", ordinal = 0))
	private boolean forge$isBlockSolidOnSide$1(World instance, int j, int k, int i) {
		return ((ForgeWorld) instance).isBlockSolidOnSide(j, k, i, 2);
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Redirect(method = "onPlaced(Lnet/minecraft/world/World;IIII)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;method_1780(III)Z", ordinal = 1))
	private boolean forge$isBlockSolidOnSide$2(World instance, int j, int k, int i) {
		return ((ForgeWorld) instance).isBlockSolidOnSide(j, k, i, 3);
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Redirect(method = "onPlaced(Lnet/minecraft/world/World;IIII)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;method_1780(III)Z", ordinal = 2))
	private boolean forge$isBlockSolidOnSide$3(World instance, int j, int k, int i) {
		return ((ForgeWorld) instance).isBlockSolidOnSide(j, k, i, 4);
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Redirect(method = "onPlaced(Lnet/minecraft/world/World;IIII)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;method_1780(III)Z", ordinal = 3))
	private boolean forge$isBlockSolidOnSide$4(World instance, int j, int k, int i) {
		return ((ForgeWorld) instance).isBlockSolidOnSide(j, k, i, 5);
	}
}
