package io.github.betterthanupdates.forge.mixin;

import io.github.betterthanupdates.forge.stat.achievement.ForgeAchievement;
import net.minecraft.achievement.Achievement;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatFormatter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Achievement.class)
public abstract class AchievementMixin extends Stat implements ForgeAchievement {
	@Mutable
	@Shadow
	@Final
	private String description;

	public AchievementMixin(int statId, String string, StatFormatter statFormatter) {
		super(statId, string, statFormatter);
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}
}
