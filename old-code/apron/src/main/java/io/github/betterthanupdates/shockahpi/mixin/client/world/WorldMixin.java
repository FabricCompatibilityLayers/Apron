package io.github.betterthanupdates.shockahpi.mixin.client.world;

import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import shockahpi.Loc;
import shockahpi.SAPI;

import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldProperties;

@Mixin(World.class)
public abstract class WorldMixin implements BlockView {
	@Shadow
	public WorldProperties properties;

	@ModifyArg(method = "<init>(Lnet/minecraft/world/dimension/DimensionData;Ljava/lang/String;JLnet/minecraft/world/dimension/Dimension;)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/dimension/Dimension;method_1767(I)Lnet/minecraft/world/dimension/Dimension;", ordinal = 1))
	private int sapi$fixDimensionId(int i) {
		if (this.properties != null) {
			i = this.properties.getDimensionId();
		}

		return i;
	}

	@ModifyArg(method = "method_154",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/Chunk;setBlock(IIIII)Z"), index = 3)
	private int sapi$setBlockWithMetadata$1(int i, @Local(ordinal = 0, argsOnly = true) int x, @Local(ordinal = 1, argsOnly = true) int y,
											@Local(ordinal = 2, argsOnly = true) int z) {
		return SAPI.interceptBlockSet((World) (Object) this, new Loc(x, y, z), i);
	}

	@ModifyArg(method = "method_200",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/Chunk;setBlockId(IIII)Z"), index = 3)
	private int sapi$setBlockInChunk$1(int i, @Local(ordinal = 0, argsOnly = true) int x, @Local(ordinal = 1, argsOnly = true) int y,
									   @Local(ordinal = 2, argsOnly = true) int z) {
		return SAPI.interceptBlockSet((World) (Object) this, new Loc(x, y, z), i);
	}
}
