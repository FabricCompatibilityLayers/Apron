package io.github.betterthanupdates.shockahpi.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import shockahpi.DimensionBase;
import shockahpi.Loc;
import shockahpi.SAPI;

import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldProperties;
import net.minecraft.world.dimension.Dimension;

@Mixin(World.class)
public abstract class WorldMixin implements BlockView {
	@Shadow
	public WorldProperties properties;

	@Redirect(method = "<init>(Lnet/minecraft/world/dimension/DimensionData;Ljava/lang/String;JLnet/minecraft/world/dimension/Dimension;)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/dimension/Dimension;getByID(I)Lnet/minecraft/world/dimension/Dimension;", ordinal = 0))
	private Dimension sapi$ctr1(int i) {
		DimensionBase localDimensionBase = DimensionBase.getDimByNumber(i);
		return localDimensionBase.getWorldProvider();
	}

	@Redirect(method = "<init>(Lnet/minecraft/world/dimension/DimensionData;Ljava/lang/String;JLnet/minecraft/world/dimension/Dimension;)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/dimension/Dimension;getByID(I)Lnet/minecraft/world/dimension/Dimension;", ordinal = 1))
	private Dimension sapi$ctr2(int i) {
		if (this.properties != null) {
			i = this.properties.getDimensionId();
		}

		DimensionBase localDimensionBase = DimensionBase.getDimByNumber(i);
		return localDimensionBase.getWorldProvider();
	}

	int cachedI, cachedJ, cachedK;

	@Inject(method = "setBlockWithMetadata", at = @At("HEAD"))
	private void sapi$setBlockWithMetadata(int i, int j, int k, int l, int i1, CallbackInfoReturnable<Boolean> cir) {
		this.cachedI = i;
		this.cachedJ = j;
		this.cachedK = k;
	}

	@Inject(method = "setBlockWithMetadata",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/Chunk;setBlockWithMetadata(IIIII)Z"))
	private void sapi$setBlockWithMetadata$1(int j, int k, int l, int m, int par5, CallbackInfoReturnable<Boolean> cir) {
		par5 = SAPI.interceptBlockSet((World) (Object) this, new Loc(this.cachedI, this.cachedJ, this.cachedK), par5);
	}

	int cachedI2, cachedJ2, cachedK2;

	@Inject(method = "setBlockInChunk", at = @At("HEAD"))
	private void sapi$setBlockInChunk(int i, int j, int k, int l, CallbackInfoReturnable<Boolean> cir) {
		this.cachedI2 = i;
		this.cachedJ2 = j;
		this.cachedK2 = k;
	}

	@Inject(method = "setBlockInChunk",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/Chunk;method_860(IIII)Z"))
	private void sapi$setBlockInChunk$1(int j, int k, int l, int par4, CallbackInfoReturnable<Boolean> cir) {
		par4 = SAPI.interceptBlockSet((World) (Object) this, new Loc(this.cachedI2, this.cachedJ2, this.cachedK2), par4);
	}
}
