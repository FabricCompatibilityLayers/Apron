package io.github.betterthanupdates.modoptionsapi.mixin.client.gui.screen;

import modoptionsapi.ModOptionsAPI;
import modoptionsapi.gui.ModMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;

@Mixin(GameMenuScreen.class)
public class GameMenuScreenMixin extends Screen {
	@Inject(method = "init", at = @At("RETURN"))
	private void modoptionsapi$addButton(CallbackInfo ci) {
		if (ModOptionsAPI.getAllMods().length > 0)
			this.buttons.add(new ButtonWidget(30, this.width / 2 - 100, this.height / 4 + 148 + -16, "Mod World Options"));
	}

	@Inject(method = "buttonClicked", at = @At("RETURN"))
	private void modoptionsapi$buttonClicked(ButtonWidget guibutton, CallbackInfo ci) {
		if (guibutton.id == 30) {
			if (this.minecraft.isWorldRemote()) {
				String[] parts = this.minecraft.options.lastServer.split("_");
				String name = parts[0];
				this.minecraft.setScreen(new ModMenu((GameMenuScreen) (Object) this, name, true));
			} else {
				String name = this.minecraft.world.method_262().getName();
				this.minecraft.setScreen(new ModMenu((GameMenuScreen) (Object) this, name, false));
			}
		}
	}
}
