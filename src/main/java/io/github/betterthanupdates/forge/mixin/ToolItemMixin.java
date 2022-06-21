package io.github.betterthanupdates.forge.mixin;

import forge.ForgeHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;

import io.github.betterthanupdates.forge.item.ForgeItem;

@Mixin(ToolItem.class)
public class ToolItemMixin extends Item implements ForgeItem {
	@Shadow
	public float field_2713;

	public ToolItemMixin(int i) {
		super(i);
	}

	@Override
	public float getStrVsBlock(ItemStack stack, Block block, int meta) {
		return ForgeHooks.isToolEffective(stack, block, meta) ? this.field_2713 : this.getStrengthOnBlock(stack, block);
	}
}
