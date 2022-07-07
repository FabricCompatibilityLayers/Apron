package io.github.betterthanupdates.guiapi.mixin.client;

import guiapi.GuiModScreen;
import guiapi.GuiModSelect;
import guiapi.ModSettingScreen;
import guiapi.WidgetSetting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.menu.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;

@Mixin(OptionsScreen.class)
public class OptionsScreenMixin extends Screen {
	@Inject(method = "init", at = @At("RETURN"))
	private void guiapi$init(CallbackInfo ci) {
		this.buttons.add(new ButtonWidget(300, this.width / 2 - 100, this.height / 6 + 144, "Global Mod Settings"));
	}

	@Inject(method = "buttonClicked", at = @At("RETURN"))
	private void guiapi$buttonClicked(ButtonWidget par1, CallbackInfo ci) {
		if (par1.active && par1.id == 300) {
			this.client.options.saveOptions();
			ModSettingScreen.guiContext = "";
			WidgetSetting.updateAll();
			GuiModScreen.show(new GuiModSelect(this));
		}
	}
}
