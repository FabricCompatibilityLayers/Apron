package io.github.betterthanupdates.forge.mixin.block;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.block.Block;
import net.minecraft.block.LadderBlock;
import net.minecraft.block.Material;
import net.minecraft.world.World;

import io.github.betterthanupdates.forge.block.ForgeBlock;
import io.github.betterthanupdates.forge.world.ForgeWorld;

@Mixin(LadderBlock.class)
public abstract class LadderBlockMixin extends Block implements ForgeBlock {
	protected LadderBlockMixin(int blockId, Material material) {
		super(blockId, material);
	}

	@Override
	public boolean isLadder() {
		return true;
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Redirect(method = "canPlaceAt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;method_1780(III)Z", ordinal = 0))
	private boolean forge$canPlaceAt$1(World instance, int j, int k, int i) {
		return ((ForgeWorld) instance).isBlockSolidOnSide(j, k, i, 5);
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Redirect(method = "canPlaceAt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;method_1780(III)Z", ordinal = 1))
	private boolean forge$canPlaceAt$2(World instance, int j, int k, int i) {
		return ((ForgeWorld) instance).isBlockSolidOnSide(j, k, i, 4);
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Redirect(method = "canPlaceAt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;method_1780(III)Z", ordinal = 2))
	private boolean forge$canPlaceAt$3(World instance, int j, int k, int i) {
		return ((ForgeWorld) instance).isBlockSolidOnSide(j, k, i, 3);
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Redirect(method = "canPlaceAt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;method_1780(III)Z", ordinal = 3))
	private boolean forge$canPlaceAt$4(World instance, int j, int k, int i) {
		return ((ForgeWorld) instance).isBlockSolidOnSide(j, k, i, 2);
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Redirect(method = {"onPlaced", "neighborUpdate"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;method_1780(III)Z", ordinal = 0))
	private boolean forge$isBlockSolidOnSide$1(World instance, int j, int k, int i) {
		return ((ForgeWorld) instance).isBlockSolidOnSide(j, k, i, 2);
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Redirect(method = {"onPlaced", "neighborUpdate"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;method_1780(III)Z", ordinal = 1))
	private boolean forge$isBlockSolidOnSide$2(World instance, int j, int k, int i) {
		return ((ForgeWorld) instance).isBlockSolidOnSide(j, k, i, 3);
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Redirect(method = {"onPlaced", "neighborUpdate"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;method_1780(III)Z", ordinal = 2))
	private boolean forge$isBlockSolidOnSide$3(World instance, int j, int k, int i) {
		return ((ForgeWorld) instance).isBlockSolidOnSide(j, k, i, 4);
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Redirect(method = {"onPlaced", "neighborUpdate"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;method_1780(III)Z", ordinal = 3))
	private boolean forge$isBlockSolidOnSide$4(World instance, int j, int k, int i) {
		return ((ForgeWorld) instance).isBlockSolidOnSide(j, k, i, 5);
	}
}
