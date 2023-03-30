package io.github.betterthanupdates.reforged.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.AbstractAnimalEntity;
import net.minecraft.entity.animal.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

@Mixin(SheepEntity.class)
public class SheepEntityMixin extends AbstractAnimalEntity {
	public SheepEntityMixin(World arg) {
		super(arg);
	}

	@Inject(method = "interact", cancellable = true, at = @At("RETURN"))
	private void reforged$interact(PlayerEntity entityplayer, CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(super.interact(entityplayer));
	}
}
