package io.github.betterthanupdates.forge.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.block.Block;
import net.minecraft.block.RailBlock;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;

import io.github.betterthanupdates.forge.world.ForgeWorld;

@Mixin(RailBlock.class)
public abstract class RailBlockMixin extends Block {
	protected RailBlockMixin(int blockId, Material material) {
		super(blockId, material);
	}

	/**
	 * @author Eloraam
	 * @reason provide a sane way of knowing whether a block is a rail
	 */
	@Overwrite
	public static final boolean method_1109(World world, int x, int y, int z) {
		int l = world.getBlockId(x, y, z);
		return Block.BY_ID[l] instanceof RailBlock;
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Overwrite
	public static final boolean isRail(int i) {
		return Block.BY_ID[i] instanceof RailBlock;
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Redirect(method = {"canPlaceAt", "onAdjacentBlockUpdate"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;canSuffocate(III)Z"))
	private boolean forge$isBlockSolidOnSide(World instance, int j, int k, int i) {
		return ((ForgeWorld) instance).isBlockSolidOnSide(j, k, i, 1);
	}
}
