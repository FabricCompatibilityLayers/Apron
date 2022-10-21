package io.github.betterthanupdates.apron.fixes.vanilla.compat.mixin.client.betatweaks;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import modloader.BaseMod;
import modloader.ModLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.betterthanupdates.apron.ReflectionUtils;

@Pseudo
@Mixin(targets = {"betatweaks/Utils"})
public class UtilsMixin {
	@Inject(target = {@Desc(value = "getField", args = {Class.class, String[].class}, ret = Field.class)},
			cancellable = true, at = @At("HEAD"), remap = false)
	private static void fixField(Class<?> target, String[] names, CallbackInfoReturnable<Field> ci) {
		ci.setReturnValue(ReflectionUtils.getField(target, names));
	}

	@Inject(target = {@Desc(value = "getMethod", args = {Class.class, Class[].class, String[].class}, ret = Method.class)},
			cancellable = true, at = @At("HEAD"), remap = false)
	private static void fixMethod(Class<?> target, Class<?>[] types, String[] names, CallbackInfoReturnable<Method> ci) {
		ci.setReturnValue(ReflectionUtils.getMethod(target, names, types));
	}

	@Shadow(remap = false)
	public static boolean classExists(String name) {
		return false;
	}

	/**
	 * @author CatCore
	 * @reason Handle this in a more compatible way.
	 */
	@Overwrite(remap = false)
	public static BaseMod loadMod(String modPath) {
		try {
			if (!classExists(modPath)) {
				modPath = "net.minecraft." + modPath;
			}

			ModLoader.addInternalMod(ModLoader.class.getClassLoader(), modPath);
			return ModLoader.getLoadedMods().get(ModLoader.getLoadedMods().size() - 1);
		} catch (Exception var3) {
			var3.printStackTrace();
			return null;
		}
	}

	@ModifyArg(target = {@Desc(value = "checkModInstalled", args = {String.class, String.class}, ret = boolean.class)},
			remap = false, at = @At(value = "INVOKE", desc = @Desc(value = "classExists", args = {String.class}, ret = boolean.class), remap = false))
	private static String fixCheckModInstalled(String name) {
		if (name.equals("ModLoaderMp")) {
			name = "modloadermp." + name;
		} else if (name.equals("ModSettings")) {
			name = "guiapi." + name;
		} else {
			name = "net.minecraft." + name;
		}

		return name;
	}
}
