package io.github.betterthanupdates.forge.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.block.Block;
import net.minecraft.block.LadderBlock;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;

import io.github.betterthanupdates.apron.block.ApronBlock;
import io.github.betterthanupdates.forge.world.ForgeWorld;

@Mixin(LadderBlock.class)
public abstract class LadderBlockMixin extends Block implements ApronBlock {
	protected LadderBlockMixin(int blockId, Material material) {
		super(blockId, material);
	}

	@Override
	public boolean isLadder() {
		return true;
	}

	@Redirect(method = "canPlaceAt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;canSuffocate(III)Z", ordinal = 0))
	private boolean reforged$canPlaceAt$1(World instance, int j, int k, int i) {
		return ((ForgeWorld) instance).isBlockSolidOnSide(j, k, i, 5);
	}

	@Redirect(method = "canPlaceAt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;canSuffocate(III)Z", ordinal = 1))
	private boolean reforged$canPlaceAt$2(World instance, int j, int k, int i) {
		return ((ForgeWorld) instance).isBlockSolidOnSide(j, k, i, 4);
	}

	@Redirect(method = "canPlaceAt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;canSuffocate(III)Z", ordinal = 2))
	private boolean reforged$canPlaceAt$3(World instance, int j, int k, int i) {
		return ((ForgeWorld) instance).isBlockSolidOnSide(j, k, i, 3);
	}

	@Redirect(method = "canPlaceAt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;canSuffocate(III)Z", ordinal = 3))
	private boolean reforged$canPlaceAt$4(World instance, int j, int k, int i) {
		return ((ForgeWorld) instance).isBlockSolidOnSide(j, k, i, 2);
	}

	@Redirect(method = {"onBlockPlaced", "onAdjacentBlockUpdate"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;canSuffocate(III)Z", ordinal = 0))
	private boolean reforged$isBlockSolidOnSide$1(World instance, int j, int k, int i) {
		return ((ForgeWorld) instance).isBlockSolidOnSide(j, k, i, 2);
	}

	@Redirect(method = {"onBlockPlaced", "onAdjacentBlockUpdate"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;canSuffocate(III)Z", ordinal = 1))
	private boolean reforged$isBlockSolidOnSide$2(World instance, int j, int k, int i) {
		return ((ForgeWorld) instance).isBlockSolidOnSide(j, k, i, 3);
	}

	@Redirect(method = {"onBlockPlaced", "onAdjacentBlockUpdate"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;canSuffocate(III)Z", ordinal = 2))
	private boolean reforged$isBlockSolidOnSide$3(World instance, int j, int k, int i) {
		return ((ForgeWorld) instance).isBlockSolidOnSide(j, k, i, 4);
	}

	@Redirect(method = {"onBlockPlaced", "onAdjacentBlockUpdate"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;canSuffocate(III)Z", ordinal = 3))
	private boolean reforged$isBlockSolidOnSide$4(World instance, int j, int k, int i) {
		return ((ForgeWorld) instance).isBlockSolidOnSide(j, k, i, 5);
	}
}
