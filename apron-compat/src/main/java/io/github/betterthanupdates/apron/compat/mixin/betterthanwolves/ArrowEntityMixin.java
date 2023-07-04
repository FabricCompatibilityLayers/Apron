package io.github.betterthanupdates.apron.compat.mixin.betterthanwolves;

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
	private int inBlock;

	@Shadow
	private int blockX;

	@Shadow
	private int blockY;

	@Shadow
	private int blockZ;

	public ArrowEntityMixin(World arg) {
		super(arg);
	}

	@Inject(method = "tick", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/projectile/ArrowEntity;shake:I", ordinal = 2, shift = At.Shift.AFTER))
	private void btw$tick(CallbackInfo ci) {
		if (this.inBlock == mod_FCBetterThanWolves.fcBlockDetector.id) {
			mod_FCBetterThanWolves.fcBlockDetector.onEntityCollision(this.world, this.blockX, this.blockY, this.blockZ, this);
		} else if (this.inBlock == mod_FCBetterThanWolves.fcVase.id) {
			((FCBlockVase)mod_FCBetterThanWolves.fcVase).BreakVase(this.world, this.blockX, this.blockY, this.blockZ);
		}
	}
}
