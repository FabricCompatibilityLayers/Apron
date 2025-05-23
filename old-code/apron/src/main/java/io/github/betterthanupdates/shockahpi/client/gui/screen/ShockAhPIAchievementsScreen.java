package io.github.betterthanupdates.shockahpi.client.gui.screen;

import net.minecraft.achievement.Achievement;

public interface ShockAhPIAchievementsScreen {
	boolean isVisibleAchievement(Achievement achievement, int deep);

	boolean isVisibleLine(Achievement achievement);

	boolean checkHidden(Achievement achievement);
}
