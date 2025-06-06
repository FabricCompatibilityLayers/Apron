package io.github.fabriccompatibilitylayers.modloader.mixin.common;

import net.minecraft.achievement.Achievement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Achievement.class)
public interface AchievementAccessor {
	@Accessor("translationKey")
	void setTranslationKey(String key);
}
