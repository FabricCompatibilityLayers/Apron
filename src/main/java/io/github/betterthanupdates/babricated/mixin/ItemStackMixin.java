package io.github.betterthanupdates.babricated.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import io.github.betterthanupdates.babricated.item.ItemConvertible;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ItemConvertible {
	@Shadow
	public abstract Item getItem();

	@Override
	public Item asItem() {
		return this.getItem();
	}
}
