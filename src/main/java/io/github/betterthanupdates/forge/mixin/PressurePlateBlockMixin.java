package io.github.betterthanupdates.forge.mixin;

import io.github.betterthanupdates.forge.world.ForgeWorld;
import net.minecraft.block.Block;
import net.minecraft.block.PressurePlateBlock;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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
		cir.setReturnValue(((ForgeWorld)world).isBlockSolidOnSide(x, y - 1, z, 1));
	}

	/**
	 * @author Eloraam
	 * @reason Minecraft Forge patch to method
	 */
	@Inject(method = "onAdjacentBlockUpdate", at = @At("RETURN"))
	private void forge$onAdjacentBlockUpdate(final World world, final int x, final int y, final int z, final int side,
	                                         final CallbackInfo ci) {
		if (!((ForgeWorld)world).isBlockSolidOnSide(x, y - 1, z, 1)) {
			this.drop(world, x, y, z, world.getBlockMeta(x, y, z));
			world.setBlock(x, y, z, 0);
		}
	}


}
