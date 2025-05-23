package io.github.betterthanupdates.shockahpi.mixin.client.gui.screen.achievement;

import java.lang.reflect.Array;
import net.minecraft.client.gui.screen.achievement.AchievementsScreen;
import fr.catcore.cursedmixinextensions.annotations.Public;
import org.spongepowered.asm.mixin.Mixin;

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
