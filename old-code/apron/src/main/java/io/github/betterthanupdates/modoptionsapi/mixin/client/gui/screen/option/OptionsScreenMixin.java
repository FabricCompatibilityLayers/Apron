package io.github.betterthanupdates.modoptionsapi.mixin.client.gui.screen.option;

import modoptionsapi.ModOptionsAPI;
import modoptionsapi.gui.ModMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;

@Mixin(OptionsScreen.class)
public class OptionsScreenMixin extends Screen {
	@Inject(method = "init", at = @At("RETURN"))
	private void modoptionsapi$init(CallbackInfo ci) {
		// TODO: This string needs to be localized, eventually.
		if (ModOptionsAPI.getAllMods().length > 0) this.buttons.add(new ButtonWidget(301, this.width / 2, this.height / 6 + 192, "ModOptionsAPI's Mod Options"));
	}

	@Inject(method = "buttonClicked", at = @At("RETURN"))
	private void modoptionsapi$buttonClicked(ButtonWidget guibutton, CallbackInfo ci) {
		if (guibutton.active && guibutton.id == 301) {
			this.minecraft.options.save();
			this.minecraft.setScreen(new ModMenu(this));
		}
	}
}
