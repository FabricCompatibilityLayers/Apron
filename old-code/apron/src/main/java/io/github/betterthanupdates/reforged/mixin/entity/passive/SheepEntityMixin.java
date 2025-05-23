package io.github.betterthanupdates.reforged.mixin.entity.passive;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

@Mixin(SheepEntity.class)
public class SheepEntityMixin extends AnimalEntity {
	public SheepEntityMixin(World arg) {
		super(arg);
	}

	@Inject(method = "method_1323", cancellable = true, at = @At("RETURN"))
	private void reforged$interact(PlayerEntity entityplayer, CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(super.method_1323(entityplayer));
	}
}
