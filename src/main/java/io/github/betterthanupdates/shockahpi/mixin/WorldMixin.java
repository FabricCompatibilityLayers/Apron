package io.github.betterthanupdates.shockahpi.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import shockahpi.DimensionBase;
import shockahpi.Loc;
import shockahpi.SAPI;

import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldProperties;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionData;

@Mixin(World.class)
public abstract class WorldMixin implements BlockView {

	@Shadow
	public WorldProperties properties;

	@Mutable
	@Shadow
	@Final
	public Dimension dimension;

	@Inject(method = "<init>(Lnet/minecraft/world/dimension/DimensionData;Ljava/lang/String;JLnet/minecraft/world/dimension/Dimension;)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/dimension/Dimension;initDimension(Lnet/minecraft/world/World;)V", shift = At.Shift.BEFORE))
	private void sapi$ctr(DimensionData wt1, String s1, long l1, Dimension xa1, CallbackInfo ci) {
		if (xa1 == null) {
			int i1 = 0;
			if (this.properties != null) {
				i1 = this.properties.getDimensionId();
			}

			DimensionBase localDimensionBase = DimensionBase.getDimByNumber(i1);
			this.dimension = localDimensionBase.getWorldProvider();
		}
	}

	@Inject(method = "setBlockWithMetadata", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/Chunk;setBlockWithMetadata(IIIII)Z"))
	private void sapi$setBlockWithMetadata(int i1, int j1, int k1, int l1, int i2, CallbackInfoReturnable<Boolean> cir) {
		l1 = SAPI.interceptBlockSet((World)(Object) this, new Loc(i1, j1, k1), l1);
	}

	@Inject(method = "setBlockInChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/Chunk;method_860(IIII)Z"))
	private void sapi$setBlockInChunk(int i1, int j1, int k1, int l1, CallbackInfoReturnable<Boolean> cir) {
		l1 = SAPI.interceptBlockSet((World)(Object) this, new Loc(i1, j1, k1), l1);
	}
}
