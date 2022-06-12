package io.github.betterthanupdates.babricated.item;

import net.minecraft.item.Item;
import org.jetbrains.annotations.Nullable;

/**
 * A utility interface from newer versions of Minecraft that makes getting an item from
 * {@link net.minecraft.block.Block a Block},
 * {@link net.minecraft.item.ItemStack an ItemStack}, or
 * {@link net.minecraft.entity.ItemEntity an ItemEntity} easier.
 */
public interface ItemConvertible {
	@Nullable
	default Item asItem() {
		return Item.byId[0];
	}
}
