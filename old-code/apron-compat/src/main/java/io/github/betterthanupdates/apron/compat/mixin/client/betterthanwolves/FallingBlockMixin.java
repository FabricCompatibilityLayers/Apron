package io.github.betterthanupdates.apron.compat.mixin.client.betterthanwolves;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.block.SandBlock;
import net.minecraft.world.World;

@Mixin(SandBlock.class)
public class FallingBlockMixin {
	@Inject(method = "method_435", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/World;getBlockId(III)I"), cancellable = true)
	private static void btw$method_435(World arg, int i, int j, int k, CallbackInfoReturnable<Boolean> cir) {
		if (arg.method_234(i, j, k)) cir.setReturnValue(true);
	}
}
