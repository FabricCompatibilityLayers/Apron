package io.github.betterthanupdates.apron.stapi.mixin.hmi;

import io.github.betterthanupdates.apron.stapi.hmi.HMITab;
import net.glasslauncher.hmifabric.tabs.Tab;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Tab.class)
public class TabMixin implements HMITab {
	@Override
	public void apron$updateRecipeList() {

	}
}
