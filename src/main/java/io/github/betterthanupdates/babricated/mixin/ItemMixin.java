package io.github.betterthanupdates.babricated.mixin;

import io.github.betterthanupdates.babricated.item.ItemConvertible;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Item.class)
public class ItemMixin implements ItemConvertible {
	@Override
	public Item asItem() {
		return ((Item)(Object) this);
	}
}
