package io.github.betterthanupdates.forge.mixin.item;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import io.github.betterthanupdates.forge.item.ForgeItem;

@Mixin(Item.class)
public abstract class ItemMixin implements ForgeItem {
	@Shadow
	public abstract float getMiningSpeedMultiplier(ItemStack stack, Block block);

	@Override
	public float getStrVsBlock(ItemStack stack, Block block, int meta) {
		return this.getMiningSpeedMultiplier(stack, block);
	}
}
