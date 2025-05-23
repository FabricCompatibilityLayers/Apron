package io.github.betterthanupdates.forge.mixin.world.chunk;

import com.llamalad7.mixinextras.sugar.Local;
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

	@Inject(method = "setBlock", at = @At(value = "FIELD", target = "Lnet/minecraft/world/chunk/Chunk;blocks:[B", ordinal = 1), cancellable = true)
	private void forge$setBlockWithMetadata(int i, int j, int k, int l, int i1, CallbackInfoReturnable<Boolean> cir,
											@Local(ordinal = 6) int blockId, @Local(ordinal = 7) int l1, @Local(ordinal = 8) int i2) {
		if (Block.BLOCKS[blockId] instanceof IOverrideReplace) {
			IOverrideReplace overrideReplace = (IOverrideReplace) Block.BLOCKS[blockId];

			if (!overrideReplace.canReplaceBlock(this.world, l1, j, i2, l)) {
				cir.setReturnValue(overrideReplace.getReplacedSuccess());
			}
		}
	}

	@Inject(method = "setBlockId", at = @At(value = "FIELD", target = "Lnet/minecraft/world/chunk/Chunk;blocks:[B", ordinal = 1), cancellable = true)
	private void forge$method_860(int i, int j, int k, int l, CallbackInfoReturnable<Boolean> cir,
											@Local(ordinal = 5) int blockId, @Local(ordinal = 6) int l1, @Local(ordinal = 7) int i2) {
		if (Block.BLOCKS[blockId] instanceof IOverrideReplace) {
			IOverrideReplace overrideReplace = (IOverrideReplace) Block.BLOCKS[blockId];

			if (!overrideReplace.canReplaceBlock(this.world, l1, j, i2, l)) {
				cir.setReturnValue(overrideReplace.getReplacedSuccess());
			}
		}
	}
}
