package io.github.betterthanupdates.apron.compat.mixin.betterblocks;

import forge.ISpecialResistance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.IBetterBlock;
import net.minecraft.entity.Entity;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

@Mixin(IBetterBlock.class)
public interface IBetterBlockMixin extends ISpecialResistance {
	@Shadow
	float getExplosionResistance(BlockView arg, int i, int j, int k, Entity arg2);

	@Override
	default float getSpecialExplosionResistance(World world, int i, int j, int k, double d, double e, double f, Entity entity) {
		return this.getExplosionResistance(world, i, j, k, entity);
	}
}
