package io.github.fabriccompatibilitylayers.modloader.mixin.client.modloader;

import modloader.ModLoader;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkCache;
import net.minecraft.world.chunk.ChunkSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkCache.class)
public class ChunkCacheMixin {
	@Shadow
	private World world;

	@Shadow
	private ChunkSource generator;

	@Inject(method = "decorate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/Chunk;markDirty()V"))
	private void modloader$PopulateChunk(ChunkSource paramcl, int paramInt1, int paramInt2, CallbackInfo ci) {
		ModLoader.PopulateChunk(this.generator, paramInt1, paramInt2, this.world);
	}
}
