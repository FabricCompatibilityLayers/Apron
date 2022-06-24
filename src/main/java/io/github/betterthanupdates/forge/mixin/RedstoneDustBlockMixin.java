package io.github.betterthanupdates.forge.mixin;

import forge.IConnectRedstone;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Block;
import net.minecraft.block.RedstoneDustBlock;
import net.minecraft.block.material.Material;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import io.github.betterthanupdates.forge.world.ForgeWorld;

@Mixin(RedstoneDustBlock.class)
public class RedstoneDustBlockMixin extends Block {
	protected RedstoneDustBlockMixin(int blockId, Material material) {
		super(blockId, material);
	}

	@Redirect(method = {"canPlaceAt"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;canSuffocate(III)Z"))
	private boolean reforged$isBlockSolidOnSide(World instance, int j, int k, int i) {
		return ((ForgeWorld) instance).isBlockSolidOnSide(j, k, i, 1);
	}

	@Inject(method = "method_1287", cancellable = true, at = @At("HEAD"))
	private static void reforged$canConnectRedstone(BlockView iblockaccess, int i, int j, int k, int l, CallbackInfoReturnable<Boolean> cir) {
		int id = iblockaccess.getBlockId(i, j, k);
		if (id != Block.REDSTONE_DUST.id
				&& id != 0
				&& Block.BY_ID[id] instanceof IConnectRedstone) {
			IConnectRedstone icr = (IConnectRedstone) Block.BY_ID[id];
			cir.setReturnValue(icr.canConnectRedstone(iblockaccess, i, j, k, l));
		}
	}
}
