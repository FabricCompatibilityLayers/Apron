package io.github.betterthanupdates.forge.mixin;

import forge.IOverrideReplace;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

@Mixin(Chunk.class)
public abstract class ChunkMixin {
	@Shadow
	public byte[] blocks;

	@Shadow
	@Final
	public int x;

	@Shadow
	@Final
	public int z;

	@Shadow
	public World world;

	@Inject(method = "setBlockWithMetadata", cancellable = true, at = @At(value = "CONSTANT", args = "intValue=255", ordinal = 2))
	private void reforged$setBlockWithMetadata(int i, int j, int k, int l, int i1, CallbackInfoReturnable<Boolean> cir) {
		int k1 = this.blocks[i << 11 | k << 7 | j] & 255;

		int l1 = this.x * 16 + i;
		int i2 = this.z * 16 + k;

		if (Block.BY_ID[k1] instanceof IOverrideReplace) {
			IOverrideReplace iovr = (IOverrideReplace) Block.BY_ID[k1];

			if (!iovr.canReplaceBlock(this.world, l1, j, i2, l)) {
				cir.setReturnValue(iovr.getReplacedSuccess());
			}
		}
	}

	@Inject(method = "method_860", cancellable = true, at = @At(value = "CONSTANT", args = "intValue=255", ordinal = 2))
	private void reforged$method_860(int i, int j, int k, int l, CallbackInfoReturnable<Boolean> cir) {
		int j1 = this.blocks[i << 11 | k << 7 | j] & 255;

		int k1 = this.x * 16 + i;
		int l1 = this.z * 16 + k;

		if (Block.BY_ID[j1] instanceof IOverrideReplace) {
			IOverrideReplace iovr = (IOverrideReplace) Block.BY_ID[j1];

			if (!iovr.canReplaceBlock(this.world, k1, j, l1, l)) {
				cir.setReturnValue(iovr.getReplacedSuccess());
			}
		}
	}
}
