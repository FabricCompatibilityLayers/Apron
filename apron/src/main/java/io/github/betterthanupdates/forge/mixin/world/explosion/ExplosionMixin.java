package io.github.betterthanupdates.forge.mixin.world.explosion;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import forge.ISpecialResistance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Explosion.class)
public class ExplosionMixin {
	@Shadow
	private World world;

	@Shadow
	public double x;

	@Shadow
	public double y;

	@Shadow
	public double z;

	@Shadow
	public Entity cause;

	@WrapOperation(method = "kaboomPhase1", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockId(III)I", ordinal = 0))
	private int forge$getBlockPos(World instance, int x, int y, int z, Operation<Integer> operation,
								  @Share("x") LocalIntRef xRef, @Share("y") LocalIntRef yRef, @Share("z") LocalIntRef zRef) {
		xRef.set(x);
		yRef.set(y);
		zRef.set(z);

		return operation.call(instance, x, y, z);
	}

	@WrapOperation(method = "kaboomPhase1", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getBlastResistance(Lnet/minecraft/entity/Entity;)F"))
	private float forge$getBlastResistance(Block instance, Entity entity, Operation<Float> operation,
										   @Share("x") LocalIntRef xRef, @Share("y") LocalIntRef yRef, @Share("z") LocalIntRef zRef) {
		if (instance instanceof ISpecialResistance) {
			return ((ISpecialResistance) instance).getSpecialExplosionResistance(
					this.world, xRef.get(), yRef.get(), zRef.get(), this.x, this.y, this.z, this.cause
			);
		}

		return operation.call(instance, entity);
	}
}
