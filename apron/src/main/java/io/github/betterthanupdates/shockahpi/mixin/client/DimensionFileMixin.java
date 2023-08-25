package io.github.betterthanupdates.shockahpi.mixin.client;

import java.io.File;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import shockahpi.DimensionBase;

import net.minecraft.world.WorldManager;
import net.minecraft.world.chunk.ChunkIO;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionData;
import net.minecraft.world.dimension.DimensionFile;
import net.minecraft.world.dimension.NetherDimension;

@Mixin(DimensionFile.class)
public abstract class DimensionFileMixin implements DimensionData {
	@Inject(method = "getChunkIO", at = @At("HEAD"))
	private void getChunkIOHead(Dimension par1, CallbackInfoReturnable<ChunkIO> cir,
								@Share("dimensionBase") LocalRef<DimensionBase> dimensionBase) {
		dimensionBase.set(DimensionBase.getDimByProvider(par1.getClass()));
	}

	@WrapOperation(method = "getChunkIO", constant = @Constant(classValue = NetherDimension.class, ordinal = 0))
	private boolean getChuckIOCondition(Object obj, Operation<Boolean> original,
										@Share("dimensionBase") LocalRef<DimensionBase> dimensionBase) {
		return dimensionBase.get() != null && dimensionBase.get().number != 0;
	}

	@ModifyArg(method = "getChunkIO", index = 1,
			at = @At(value = "INVOKE", target = "Ljava/io/File;<init>(Ljava/io/File;Ljava/lang/String;)V"))
	private String getChunkIOFileName(String child,
									  @Share("dimensionBase") LocalRef<DimensionBase> dimensionBase) {
		return "DIM " + dimensionBase.get().number;
	}
}
