package io.github.betterthanupdates.forge.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.block.Block;
import net.minecraft.block.PressurePlateBlock;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;

import io.github.betterthanupdates.forge.world.ForgeWorld;

@Mixin(PressurePlateBlock.class)
public class PressurePlateBlockMixin extends Block {
	protected PressurePlateBlockMixin(int blockId, Material material) {
		super(blockId, material);
	}

	/**
	 * @author Eloraam
	 * @reason Minecraft Forge patch to method
	 */
	@Redirect(method = {"canPlaceAt", "onAdjacentBlockUpdate"},
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;canSuffocate(III)Z"))
	private boolean forge$onAdjacentBlockUpdate(World world, int x, int y, int z) {
		return ((ForgeWorld) world).isBlockSolidOnSide(x, y, z, 1);
	}
}
