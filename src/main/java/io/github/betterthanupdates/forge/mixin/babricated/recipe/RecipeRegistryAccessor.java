package io.github.betterthanupdates.forge.mixin.babricated.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(RecipeRegistry.class)
public interface RecipeRegistryAccessor {

    @Invoker
    void callAddShapedRecipe(ItemStack arg, Object... objects);

    @Invoker
    void callAddShapelessRecipe(ItemStack arg, Object... objects);
}
