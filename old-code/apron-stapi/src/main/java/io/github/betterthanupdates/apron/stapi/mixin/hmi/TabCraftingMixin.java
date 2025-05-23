package io.github.betterthanupdates.apron.stapi.mixin.hmi;

import io.github.betterthanupdates.apron.stapi.hmi.HMITab;
import net.glasslauncher.hmifabric.tabs.TabCrafting;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.CraftingRecipeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.List;

@Mixin(TabCrafting.class)
public abstract class TabCraftingMixin implements HMITab {
	@Shadow(remap = false)
	protected List<Object> recipes;

	@Shadow(remap = false)
	protected List<Object> recipesComplete;

	@Override
	public void apron$updateRecipeList() {
		this.recipes = new ArrayList<Object>(CraftingRecipeManager.getInstance().getRecipes());
		this.recipesComplete = new ArrayList<Object>(CraftingRecipeManager.getInstance().getRecipes());

		for (int i = 0; i < recipesComplete.size(); i++) {
			//Removes recipes that are too big and ruin everything @flans mod
			if (((CraftingRecipe) recipesComplete.get(i)).getSize() > 9) {
				recipesComplete.remove(i);
				i -= 1;
			}
		}
	}
}
