package io.github.betterthanupdates.forge.mixin.babricated;

import io.github.betterthanupdates.forge.BabricatedAchievement;
import net.minecraft.achievement.Achievement;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatFormatter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Achievement.class)
public abstract class AchievementMixin extends Stat implements BabricatedAchievement {
    @Mutable
    @Shadow
    @Final
    private String description;

    public AchievementMixin(int i, String string, StatFormatter arg) {
        super(i, string, arg);
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }
}
