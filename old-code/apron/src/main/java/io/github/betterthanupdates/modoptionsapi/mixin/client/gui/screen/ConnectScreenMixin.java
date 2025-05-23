package io.github.betterthanupdates.modoptionsapi.mixin.client.gui.screen;

import modoptionsapi.ModOptionsAPI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ConnectScreen;

@Mixin(ConnectScreen.class)
public class ConnectScreenMixin {
	@Inject(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/io/PrintStream;println(Ljava/lang/String;)V", remap = false))
	private void modoptionsapi$init(Minecraft string, String s, int par3, CallbackInfo ci) {
		ModOptionsAPI.joinedMultiplayerWorld(s);
	}
}
