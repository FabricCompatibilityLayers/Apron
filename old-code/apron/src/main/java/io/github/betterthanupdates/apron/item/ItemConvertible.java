package io.github.betterthanupdates.apron.item;

import net.minecraft.block.Block;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import net.minecraft.item.Item;

/**
 * A utility interface from newer versions of Minecraft that makes getting an item from
 * {@link net.minecraft.block.Block a Block},
 * {@link net.minecraft.item.ItemStack an ItemStack}, or
 * {@link net.minecraft.entity.ItemEntity an ItemEntity} easier.
 */
public interface ItemConvertible {
	@Nullable
	default Item asItem() {
		if (((Object)this) instanceof ItemStack) return ((ItemStack) (Object) this).getItem();
		if (this instanceof Item) return ((Item) this);
		if (this instanceof ItemEntity) return ((ItemConvertible)(Object)((ItemEntity) this).stack).asItem();
		if (this instanceof Block) return Item.ITEMS[((Block) this).id];

		return Item.ITEMS[0];
	}
}
