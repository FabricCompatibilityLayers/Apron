package io.github.betterthanupdates.shockahpi.mixin.client.nostation;

import java.io.File;

import net.minecraft.class_243;
import net.minecraft.class_251;
import net.minecraft.class_294;
import net.minecraft.class_81;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import shockahpi.DimensionBase;

import net.minecraft.world.dimension.Dimension;

@Mixin(class_294.class)
public abstract class McRegionDimensionFileMixin extends class_81 {
	public McRegionDimensionFileMixin(File file, String string, boolean bl) {
		super(file, string, bl);
	}

	/**
	 * @author SAPI
	 * @reason
	 */
	@Inject(method = "method_1734", at = @At("HEAD"), cancellable = true)
	public void getChunkIO(Dimension paramxa, CallbackInfoReturnable<class_243> cir) {
		File localFile1 = this.method_332();
		DimensionBase localDimensionBase = DimensionBase.getDimByProvider(paramxa.getClass());

		if (localDimensionBase != null && localDimensionBase.number != 0) {
			File localFile2 = new File(localFile1, "DIM" + localDimensionBase.number);
			localFile2.mkdirs();
			cir.setReturnValue(new class_251(localFile2));
		} else {
			cir.setReturnValue(new class_251(localFile1));
		}
	}
}
