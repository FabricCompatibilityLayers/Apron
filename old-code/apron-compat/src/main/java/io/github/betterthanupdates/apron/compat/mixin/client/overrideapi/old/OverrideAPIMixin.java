package io.github.betterthanupdates.apron.compat.mixin.client.overrideapi.old;

import java.net.URL;
import java.net.URLClassLoader;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import overrideapi.OverrideAPI;

@Mixin(OverrideAPI.class)
public class OverrideAPIMixin {
	@Redirect(method = "<init>", remap = false, at = @At(value = "INVOKE", target = "Ljava/lang/Class;getClassLoader()Ljava/lang/ClassLoader;", remap = false))
	private ClassLoader fixClassLoader(Class instance) {
		return new URLClassLoader(new URL[]{}, this.getClass().getClassLoader());
	}
}
