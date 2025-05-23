package io.github.betterthanupdates.apron.compat.mixin.client.betterthanwolves;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.FCBlockVase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.mod_FCBetterThanWolves;
import net.minecraft.world.World;

@Mixin(ArrowEntity.class)
public abstract class ArrowEntityMixin extends Entity {
	@Shadow
	private int field_1580;

	@Shadow
	private int field_1577;

	@Shadow
	private int field_1578;

	@Shadow
	private int field_1579;

	public ArrowEntityMixin(World arg) {
		super(arg);
	}

	@Inject(method = "tick", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/projectile/ArrowEntity;field_1575:I", ordinal = 2, shift = At.Shift.AFTER))
	private void btw$tick(CallbackInfo ci) {
		if (this.field_1580 == mod_FCBetterThanWolves.fcBlockDetector.id) {
			mod_FCBetterThanWolves.fcBlockDetector.onEntityCollision(this.world, this.field_1577, this.field_1578, this.field_1579, this);
		} else if (this.field_1580 == mod_FCBetterThanWolves.fcVase.id) {
			((FCBlockVase)mod_FCBetterThanWolves.fcVase).BreakVase(this.world, this.field_1577, this.field_1578, this.field_1579);
		}
	}
}
