package io.github.fabriccompatibilitylayers.modloader.mixin.client.modloader;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CraftingRecipeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(CraftingRecipeManager.class)
public interface CraftingRecipeManagerAccessor {
	@Invoker("addShapedRecipe")
	void invokeAddShapedRecipe(ItemStack output, Object... input);

	@Invoker("addShapelessRecipe")
	void invokeAddShapelessRecipe(ItemStack output, Object... input);
}
