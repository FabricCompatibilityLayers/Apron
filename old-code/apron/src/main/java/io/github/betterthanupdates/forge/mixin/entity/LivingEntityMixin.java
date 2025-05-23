package io.github.betterthanupdates.forge.mixin.entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

import io.github.betterthanupdates.forge.block.ForgeBlock;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	public LivingEntityMixin(World world) {
		super(world);
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Redirect(method = "method_932", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockId(III)I"))
	private int forge$method_932(World instance, int j, int k, int i) {
		int blockId = instance.getBlockId(j, k, i);
		Block block = Block.BLOCKS[blockId];
		return block != null && ((ForgeBlock) block).isLadder() ? Block.LADDER.id : 0;
	}
}
