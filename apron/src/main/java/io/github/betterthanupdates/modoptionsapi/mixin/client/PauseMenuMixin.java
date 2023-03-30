package io.github.betterthanupdates.modoptionsapi.mixin.client;

import modoptionsapi.gui.ModMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.PauseScreen;
import net.minecraft.client.gui.widget.ButtonWidget;

@Mixin(PauseScreen.class)
public class PauseMenuMixin extends Screen {
	@Inject(method = "initVanillaScreen", at = @At("RETURN"))
	private void modoptionsapi$addButton(CallbackInfo ci) {
		this.buttons.add(new ButtonWidget(30, this.width / 2 - 100, this.height / 4 + 148 + -16, "Mod World Options"));
	}

	@Inject(method = "buttonClicked", at = @At("RETURN"))
	private void modoptionsapi$buttonClicked(ButtonWidget guibutton, CallbackInfo ci) {
		if (guibutton.id == 30) {
			if (this.client.hasWorld()) {
				String[] parts = this.client.options.lastServer.split("_");
				String name = parts[0];
				this.client.openScreen(new ModMenu((PauseScreen) (Object) this, name, true));
			} else {
				String name = this.client.world.getProperties().getName();
				this.client.openScreen(new ModMenu((PauseScreen) (Object) this, name, false));
			}
		}
	}
}
