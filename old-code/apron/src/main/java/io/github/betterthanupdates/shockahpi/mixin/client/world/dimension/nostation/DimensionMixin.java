package io.github.betterthanupdates.shockahpi.mixin.client.world.dimension.nostation;

import net.minecraft.world.dimension.Dimension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import shockahpi.DimensionBase;

@Mixin(Dimension.class)
public class DimensionMixin {
	@Inject(method = "method_1767", cancellable = true, at = @At("HEAD"))
	private static void getShockAhPIDimensions(int i, CallbackInfoReturnable<Dimension> cir) {
		DimensionBase dimensionbase = DimensionBase.getDimByNumber(i);

		if (dimensionbase != null) {
			cir.setReturnValue(dimensionbase.getWorldProvider());
		}
	}
}
