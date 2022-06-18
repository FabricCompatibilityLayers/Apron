package io.github.betterthanupdates.forge.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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
	@Inject(method = "canPlaceAt", at = @At("RETURN"), cancellable = true)
	private void forge$canPlaceAt(final World world, final int x, final int y, final int z,
								  final CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(((ForgeWorld) world).isBlockSolidOnSide(x, y - 1, z, 1));
	}

	/**
	 * @author Eloraam
	 * @reason Minecraft Forge patch to method
	 */
	@Redirect(method = "onAdjacentBlockUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;canSuffocate(III)Z"))
	private boolean forge$onAdjacentBlockUpdate(World world, int x, int y, int z) {
		return ((ForgeWorld) world).isBlockSolidOnSide(x, y, z, 1);
	}


}
