package io.github.betterthanupdates.babricated.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import io.github.betterthanupdates.babricated.item.ItemConvertible;

@Mixin(ItemEntity.class)
public class ItemEntityMixin implements ItemConvertible {
	@Shadow
	public ItemStack stack;

	@Override
	public Item asItem() {
		return ((ItemConvertible) (Object) this.stack).asItem();
	}
}
