package io.github.betterthanupdates.babricated.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.item.Item;

import io.github.betterthanupdates.babricated.item.ItemConvertible;

@Mixin(Item.class)
public class ItemMixin implements ItemConvertible {
	@Override
	public Item asItem() {
		return ((Item) (Object) this);
	}
}
