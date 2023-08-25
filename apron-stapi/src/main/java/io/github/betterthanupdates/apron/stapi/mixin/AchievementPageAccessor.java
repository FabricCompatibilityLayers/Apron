package io.github.betterthanupdates.apron.stapi.mixin;

import java.util.List;

import net.modificationstation.stationapi.api.client.gui.screen.menu.AchievementPage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AchievementPage.class)
public interface AchievementPageAccessor {
	@Accessor(remap = false)
	static List<AchievementPage> getPAGES() {
		return null;
	}
}
