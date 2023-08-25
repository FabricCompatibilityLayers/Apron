package io.github.betterthanupdates.apron.stapi.mixin.hmi;

import io.github.betterthanupdates.apron.stapi.hmi.HMITab;
import net.glasslauncher.hmifabric.tabs.TabSmelting;
import net.glasslauncher.hmifabric.tabs.TabWithTexture;
import net.minecraft.recipe.SmeltingRecipeRegistry;
import net.modificationstation.stationapi.api.registry.ModID;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(TabSmelting.class)
public abstract class TabSmeltingMixin extends TabWithTexture implements HMITab {
	@Shadow(remap = false)
	protected Map recipesComplete;

	public TabSmeltingMixin(ModID tabCreator, int slotsPerRecipe, String texturePath, int width, int height, int minPaddingX, int minPaddingY, int textureX, int textureY) {
		super(tabCreator, slotsPerRecipe, texturePath, width, height, minPaddingX, minPaddingY, textureX, textureY);
	}

	@Override
	public void apron$updateRecipeList() {
		this.recipesComplete = SmeltingRecipeRegistry.getInstance().getRecipes();
	}
}
