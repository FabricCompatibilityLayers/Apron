package io.github.betterthanupdates.apron.fixes.vanilla.compat.mixin.client.overrideapi;

import net.minecraft.client.gui.widget.ButtonWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import overrideapi.utils.gui.GuiHandler;
import overrideapi.utils.Reflection;

import java.lang.reflect.Field;

@Pseudo
@Mixin(GuiHandler.class)
public class GuiHandlerMixin {
	@Shadow(remap = false)
	private final Field controlListField = Reflection.findField(ButtonWidget.class, new String[] {"field_154", "buttons"});

	@Shadow(remap = false)
	private final Field selectedButtonField = Reflection.findField(ButtonWidget.class, new String[] {"field_150", "lastClickedButton"});
	@ModifyConstant(method = "init", constant = @Constant(stringValue = "e"), remap = false)
	private static String fixFieldNameP1(String constant) {
		return "field_154";
	}
}
