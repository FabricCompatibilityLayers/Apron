package io.github.betterthanupdates.forge.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import io.github.betterthanupdates.forge.item.ForgeItem;

@Mixin(Item.class)
public abstract class ItemMixin implements ForgeItem {

	@Shadow
	public abstract float getStrengthOnBlock(ItemStack arg, Block arg2);

	@Override
	public float getStrVsBlock(ItemStack itemstack, Block block, int md) {
		return this.getStrengthOnBlock(itemstack, block);
	}
}
