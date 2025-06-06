package io.github.fabriccompatibilitylayers.modloader.mixin.client.modloadermp;

import modloadermp.ModLoaderMp;
import net.minecraft.client.gui.screen.MultiplayerScreen;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiplayerScreen.class)
public class MultiplayerScreenMixin {
	@Inject(method = "<init>", at = @At("RETURN"))
	private void modloadermp$ForceInit(Screen par1, CallbackInfo ci) {
		ModLoaderMp.Init();
	}
}
