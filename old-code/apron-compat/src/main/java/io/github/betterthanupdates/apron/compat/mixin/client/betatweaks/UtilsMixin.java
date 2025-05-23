package io.github.betterthanupdates.apron.compat.mixin.client.betatweaks;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import betatweaks.Utils;
import modloader.BaseMod;
import modloader.ModLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.betterthanupdates.apron.ReflectionUtils;

@Mixin(Utils.class)
public class UtilsMixin {
	@Inject(method = "getField",
			cancellable = true, at = @At("HEAD"), remap = false)
	private static void fixField(Class<?> target, String[] names, CallbackInfoReturnable<Field> ci) {
		ci.setReturnValue(ReflectionUtils.getField(target, names));
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
}
