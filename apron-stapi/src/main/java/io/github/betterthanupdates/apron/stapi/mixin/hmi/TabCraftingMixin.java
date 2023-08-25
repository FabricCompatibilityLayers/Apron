package io.github.betterthanupdates.apron.stapi.mixin.hmi;

import io.github.betterthanupdates.apron.stapi.hmi.HMITab;
import net.glasslauncher.hmifabric.tabs.TabCrafting;
import net.glasslauncher.hmifabric.tabs.TabWithTexture;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeRegistry;
import net.modificationstation.stationapi.api.registry.ModID;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.List;

@Mixin(TabCrafting.class)
public abstract class TabCraftingMixin extends TabWithTexture implements HMITab {
	@Shadow
	protected List<Object> recipes;

	@Shadow
	protected List<Object> recipesComplete;

	public TabCraftingMixin(ModID tabCreator, int slotsPerRecipe, String texturePath, int width, int height, int minPaddingX, int minPaddingY, int textureX, int textureY) {
		super(tabCreator, slotsPerRecipe, texturePath, width, height, minPaddingX, minPaddingY, textureX, textureY);
	}

	@Override
	public void apron$updateRecipeList() {
		this.recipes = new ArrayList<Object>(RecipeRegistry.getInstance().getRecipes());
		this.recipesComplete = new ArrayList<Object>(RecipeRegistry.getInstance().getRecipes());

		for (int i = 0; i < recipesComplete.size(); i++) {
			//Removes recipes that are too big and ruin everything @flans mod
			if (((Recipe) recipesComplete.get(i)).getIngredientCount() > 9) {
				recipesComplete.remove(i);
				i -= 1;
			}
		}
	}
}
