package io.github.betterthanupdates.shockahpi.mixin.client;

import java.lang.reflect.Array;

import fr.catcore.modremapperapi.api.mixin.Public;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.gui.screen.menu.AchievementsScreen;

@Mixin(AchievementsScreen.class)
public class AchievementsScreenMixin {
	@Public
	private static Class getArrayClass(Class c) {
		try {
			Object e = Array.newInstance(c, 0);
			return e.getClass();
		} catch (Exception var2) {
			throw new IllegalArgumentException(var2);
		}
	}
}
