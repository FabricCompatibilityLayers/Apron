package io.github.betterthanupdates.modloader.mixin.client;

import modloader.ModLoader;
import net.minecraft.class_326;
import net.minecraft.class_51;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.World;

@Mixin(class_326.class)
public abstract class ServerChunkCacheMixin implements class_51 {
	@Shadow
	private class_51 field_1227;

	@Shadow
	private World field_1231;

	@Inject(method = "method_1803", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/Chunk;method_885()V"))
	private void modloader$decorate(class_51 i, int j, int par3, CallbackInfo ci) {
		ModLoader.PopulateChunk(this.field_1227, j, par3, this.field_1231);
	}
}
