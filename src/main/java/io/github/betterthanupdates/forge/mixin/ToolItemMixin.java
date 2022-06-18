package io.github.betterthanupdates.forge.mixin;

import forge.ForgeHooks;
import io.github.betterthanupdates.forge.item.ForgeItem;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ToolItem.class)
public class ToolItemMixin extends Item implements ForgeItem {
    @Shadow public float field_2713;

    public ToolItemMixin(int i) {
        super(i);
    }

    @Override
    public float getStrVsBlock(ItemStack itemstack, Block block, int md) {
        return ForgeHooks.isToolEffective(itemstack, block, md) ? this.field_2713 : this.getStrengthOnBlock(itemstack, block);
    }
}
