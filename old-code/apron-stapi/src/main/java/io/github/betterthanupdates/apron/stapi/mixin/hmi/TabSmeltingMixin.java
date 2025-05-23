package io.github.betterthanupdates.apron.stapi.mixin.hmi;

import io.github.betterthanupdates.apron.stapi.hmi.HMITab;
import net.glasslauncher.hmifabric.tabs.TabSmelting;
import net.minecraft.recipe.SmeltingRecipeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(TabSmelting.class)
public abstract class TabSmeltingMixin implements HMITab {
	@Shadow(remap = false)
	protected Map recipesComplete;

	@Override
	public void apron$updateRecipeList() {
		this.recipesComplete = SmeltingRecipeManager.getInstance().getRecipes();
	}
}
