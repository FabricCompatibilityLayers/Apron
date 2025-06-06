package io.github.fabriccompatibilitylayers.modloader.mixin.server;

import modloader.ModLoader;
import net.minecraft.server.world.chunk.ServerChunkCache;
import net.minecraft.world.ServerWorld;
import net.minecraft.world.chunk.ChunkSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerChunkCache.class)
public class ServerChunkCacheMixin {
	@Shadow
	private ChunkSource generator;

	@Shadow
	private ServerWorld world;

	@Inject(method = "decorate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/Chunk;markDirty()V"))
	private void modloader$PopulateChunk(ChunkSource ichunkprovider, int i, int j, CallbackInfo ci) {
		ModLoader.PopulateChunk(this.generator, i << 4, j << 4, this.world);
	}
}
