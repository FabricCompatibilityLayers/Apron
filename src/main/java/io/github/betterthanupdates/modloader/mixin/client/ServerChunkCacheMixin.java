package io.github.betterthanupdates.modloader.mixin.client;

import modloader.ModLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.World;
import net.minecraft.world.chunk.ServerChunkCache;
import net.minecraft.world.source.WorldSource;

@Mixin(ServerChunkCache.class)
public abstract class ServerChunkCacheMixin implements WorldSource {
	@Shadow
	private WorldSource worldSource;

	@Shadow
	private World world;

	@Inject(method = "decorate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/Chunk;method_885()V"))
	private void modloader$decorate(WorldSource i, int j, int par3, CallbackInfo ci) {
		ModLoader.PopulateChunk(this.worldSource, j, par3, this.world);
	}
}
