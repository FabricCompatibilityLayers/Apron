package io.github.betterthanupdates.guiapi.mixin.client.gui.screen.option;

import guiapi.ModScreen;
import guiapi.ModSelect;
import guiapi.ModSettingScreen;
import guiapi.widget.SettingWidget;
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
	private void guiapi$init(CallbackInfo ci) {
		// TODO: This string needs to be localized, eventually.
		if (!SettingWidget.all.isEmpty()) this.buttons.add(new ButtonWidget(300, this.width / 2 - 200, this.height / 6 + 192, "GuiAPI's Global Mod Settings"));
	}

	@Inject(method = "buttonClicked", at = @At("RETURN"))
	private void guiapi$buttonClicked(ButtonWidget par1, CallbackInfo ci) {
		if (par1.active && par1.id == 300) {
			this.minecraft.options.save();
			ModSettingScreen.guiContext = "";
			SettingWidget.updateAll();
			ModScreen.show(new ModSelect(this));
		}
	}
}
