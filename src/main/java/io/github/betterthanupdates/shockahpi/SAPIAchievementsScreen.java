package io.github.betterthanupdates.shockahpi;

import net.minecraft.stat.achievement.Achievement;

public interface SAPIAchievementsScreen {
	boolean isVisibleAchievement(Achievement achievement, int deep);

	boolean isVisibleLine(Achievement achievement);

	boolean checkHidden(Achievement achievement);
}
