package io.github.betterthanupdates.modloader.mixin.server;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_426;
import net.minecraft.class_73;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Environment(EnvType.SERVER)
@Mixin(class_426.class)
public abstract class CommandManagerMixin {
	@Redirect(method = "method_1411", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_73;method_204(J)V"))
	private void makeClientCompatibleIGuess(class_73 instance, long l) {
		instance.setTime(l);
	}
}
