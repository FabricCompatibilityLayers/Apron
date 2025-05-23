package io.github.betterthanupdates.modloader.mixin.server;

import modloader.ModLoader;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_51;
import net.minecraft.class_73;
import net.minecraft.class_79;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.SERVER)
@Mixin(class_79.class)
public abstract class ServerWorldSourceMixin implements class_51 {
	@Shadow
	private class_51 field_936;

	@Shadow
	private class_73 field_940;

	@Inject(method = "method_1803", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/Chunk;method_885()V"))
	private void modloader$decorate(class_51 ichunkprovider, int i, int j, CallbackInfo ci) {
		ModLoader.PopulateChunk(this.field_936, i << 4, j << 4, this.field_940);
	}
}
