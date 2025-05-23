package io.github.betterthanupdates.forge.mixin.block;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.Material;
import net.minecraft.world.World;

import io.github.betterthanupdates.forge.world.ForgeWorld;

@Mixin(ChestBlock.class)
public abstract class ChestBlockMixin extends BlockWithEntity {
	protected ChestBlockMixin(int blockId, Material material) {
		super(blockId, material);
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Redirect(method = "onUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;method_1780(III)Z"))
	private boolean forge$canUse(World instance, int j, int k, int i) {
		return ((ForgeWorld) instance).isBlockSolidOnSide(j, k, i, 0);
	}
}
