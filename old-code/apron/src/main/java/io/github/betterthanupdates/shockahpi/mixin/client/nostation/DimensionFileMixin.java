package io.github.betterthanupdates.shockahpi.mixin.client.nostation;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.class_243;
import net.minecraft.class_81;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import shockahpi.DimensionBase;

import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionData;
import net.minecraft.world.dimension.NetherDimension;

@Mixin(class_81.class)
public abstract class DimensionFileMixin implements DimensionData {
	@Inject(method = "method_1734", at = @At("HEAD"))
	private void getChunkIOHead(Dimension par1, CallbackInfoReturnable<class_243> cir,
								@Share("dimensionBase") LocalRef<DimensionBase> dimensionBase) {
		dimensionBase.set(DimensionBase.getDimByProvider(par1.getClass()));
	}

	@WrapOperation(method = "method_1734", constant = @Constant(classValue = NetherDimension.class, ordinal = 0))
	private boolean getChuckIOCondition(Object obj, Operation<Boolean> original,
										@Share("dimensionBase") LocalRef<DimensionBase> dimensionBase) {
		return dimensionBase.get() != null && dimensionBase.get().number != 0;
	}

	@ModifyArg(method = "method_1734", index = 1,
			at = @At(value = "INVOKE", target = "Ljava/io/File;<init>(Ljava/io/File;Ljava/lang/String;)V", remap = false))
	private String getChunkIOFileName(String child,
									  @Share("dimensionBase") LocalRef<DimensionBase> dimensionBase) {
		return dimensionBase.get() != null ? "DIM " + dimensionBase.get().number : child;
	}
}
