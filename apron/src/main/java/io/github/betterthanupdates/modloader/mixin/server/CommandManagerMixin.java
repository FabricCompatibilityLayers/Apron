package io.github.betterthanupdates.modloader.mixin.server;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.server.command.CommandManager;
import net.minecraft.server.world.ServerWorld;

@Environment(EnvType.SERVER)
@Mixin(CommandManager.class)
public abstract class CommandManagerMixin {
	@Redirect(method = "processCommand", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;method_204(J)V"))
	private void makeClientCompatibleIGuess(ServerWorld instance, long l) {
		instance.setWorldTime(l);
	}
}
