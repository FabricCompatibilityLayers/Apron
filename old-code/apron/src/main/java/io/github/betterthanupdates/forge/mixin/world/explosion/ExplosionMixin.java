package io.github.betterthanupdates.forge.mixin.world.explosion;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import forge.ISpecialResistance;
import net.minecraft.class_60;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(class_60.class)
public class ExplosionMixin {
	@Shadow
	private World field_1399;

	@Shadow
	public double field_1392;

	@Shadow
	public double field_1393;

	@Shadow
	public double field_1394;

	@Shadow
	public Entity field_1395;

	@WrapOperation(method = "method_1195", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockId(III)I", ordinal = 0))
	private int forge$getBlockPos(World instance, int x, int y, int z, Operation<Integer> operation,
								  @Share("x") LocalIntRef xRef, @Share("y") LocalIntRef yRef, @Share("z") LocalIntRef zRef) {
		xRef.set(x);
		yRef.set(y);
		zRef.set(z);

		return operation.call(instance, x, y, z);
	}

	@WrapOperation(method = "method_1195", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getBlastResistance(Lnet/minecraft/entity/Entity;)F"))
	private float forge$getBlastResistance(Block instance, Entity entity, Operation<Float> operation,
										   @Share("x") LocalIntRef xRef, @Share("y") LocalIntRef yRef, @Share("z") LocalIntRef zRef) {
		if (instance instanceof ISpecialResistance) {
			return ((ISpecialResistance) instance).getSpecialExplosionResistance(
					this.field_1399, xRef.get(), yRef.get(), zRef.get(), this.field_1392, this.field_1393, this.field_1394, this.field_1395
			);
		}

		return operation.call(instance, entity);
	}
}
