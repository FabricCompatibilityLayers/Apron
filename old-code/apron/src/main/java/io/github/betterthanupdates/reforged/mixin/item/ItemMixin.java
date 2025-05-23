package io.github.betterthanupdates.reforged.mixin.item;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import io.github.betterthanupdates.reforged.item.ReforgedItem;

@Mixin(Item.class)
public abstract class ItemMixin implements ReforgedItem {
	@Override
	public boolean onBlockStartBreak(ItemStack itemstack, int x, int y, int z, PlayerEntity player) {
		return false;
	}
}
