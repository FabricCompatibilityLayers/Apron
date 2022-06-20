package io.github.betterthanupdates.modloader.mixin.server;

import modloader.ModLoader;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.world.ServerWorldSource;
import net.minecraft.world.source.WorldSource;

@Environment(EnvType.SERVER)
@Mixin(ServerWorldSource.class)
public abstract class ServerWorldSourceMixin implements WorldSource {
	@Shadow
	private WorldSource parentLevelSource;

	@Shadow
	private ServerWorld world;

	@Inject(method = "decorate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/Chunk;method_885()V"))
	private void modloader$decorate(WorldSource ichunkprovider, int i, int j, CallbackInfo ci) {
		ModLoader.PopulateChunk(this.parentLevelSource, i << 4, j << 4, this.world);
	}
}
