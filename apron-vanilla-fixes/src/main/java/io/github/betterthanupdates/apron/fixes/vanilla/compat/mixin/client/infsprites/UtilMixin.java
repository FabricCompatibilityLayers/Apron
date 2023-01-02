package io.github.betterthanupdates.apron.fixes.vanilla.compat.mixin.client.infsprites;

import net.mine_diver.infsprites.util.Util;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Util.class)
public class UtilMixin {
	@Mutable
	@Shadow(remap = false)
	@Final
	public static boolean workspace;

	@Inject(method = "<clinit>", at = @At("RETURN"), remap = false)
	private static void fixWorkspace(CallbackInfo ci) {
		workspace = false;
	}
}
