package io.github.betterthanupdates.apron.mixin.client.gui.widget;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.gui.widget.ButtonWidget;

@Mixin(ButtonWidget.class)
public interface ButtonWidgetAccessor {
	@Accessor
	int getWidth();
	@Accessor
	int getHeight();
}
