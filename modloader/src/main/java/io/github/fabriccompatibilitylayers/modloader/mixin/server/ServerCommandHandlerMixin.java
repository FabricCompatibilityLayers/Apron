package io.github.fabriccompatibilitylayers.modloader.mixin.server;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import modloadermp.ModLoaderMp;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.logging.Logger;

@Mixin(ServerCommandHandler.class)
public class ServerCommandHandlerMixin {
	@WrapWithCondition(method = "executeCommand", at = @At(value = "INVOKE", target = "Ljava/util/logging/Logger;info(Ljava/lang/String;)V", remap = false, ordinal = 3))
	private boolean modloader$handleCommand(Logger instance, String msg,
											@Local(ordinal = 0) String s,
											@Local(ordinal = 1) String s1,
											@Local CommandOutput icommandlistener) {
		return !ModLoaderMp.handleCommand(s, s1, icommandlistener, (ServerCommandHandler) (Object) this);
	}
}
