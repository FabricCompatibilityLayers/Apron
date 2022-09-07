package io.github.betterthanupdates.stapi;

import java.util.List;
import java.util.Random;

import net.modificationstation.stationapi.api.client.gui.screen.menu.AchievementPage;
import net.modificationstation.stationapi.api.registry.ModID;

public class StAPIAchievementPage extends AchievementPage {
	private final shockahpi.AchievementPage originalPage;

	public StAPIAchievementPage(shockahpi.AchievementPage page) {
		super(StationAPIHelper.MOD_ID.isEmpty() ? ModID.MINECRAFT : StationAPIHelper.getCurrentModId(), page.title);
		this.originalPage = page;
	}

	@Override
	public int getBackgroundTexture(Random random, int column, int row, int randomizedRow, int currentTexture) {
		return this.originalPage.bgGetSprite(random, row, column);
	}

	@Override
	public List<Integer> getAchievementIds() {
		return this.originalPage.getList();
	}
}
