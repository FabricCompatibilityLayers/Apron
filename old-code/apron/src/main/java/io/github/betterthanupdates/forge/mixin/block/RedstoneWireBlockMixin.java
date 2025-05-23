package io.github.betterthanupdates.forge.mixin.block;

import forge.IConnectRedstone;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import io.github.betterthanupdates.forge.world.ForgeWorld;

@Mixin(RedstoneWireBlock.class)
public class RedstoneWireBlockMixin extends Block {
	protected RedstoneWireBlockMixin(int blockId, Material material) {
		super(blockId, material);
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Redirect(method = {"canPlaceAt"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;method_1780(III)Z"))
	private boolean forge$isBlockSolidOnSide(World instance, int j, int k, int i) {
		return ((ForgeWorld) instance).isBlockSolidOnSide(j, k, i, 1);
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Inject(method = "method_1287", cancellable = true, at = @At("HEAD"))
	private static void forge$canConnectRedstone(BlockView blockView, int i, int j, int k, int l, CallbackInfoReturnable<Boolean> cir) {
		int id = blockView.getBlockId(i, j, k);
		if (id != Block.REDSTONE_WIRE.id
				&& id != 0
				&& Block.BLOCKS[id] instanceof IConnectRedstone) {
			IConnectRedstone icr = (IConnectRedstone) Block.BLOCKS[id];
			cir.setReturnValue(icr.canConnectRedstone(blockView, i, j, k, l));
		}
	}
}
