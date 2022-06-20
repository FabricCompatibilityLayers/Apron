package io.github.betterthanupdates.apron.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

import io.github.betterthanupdates.apron.item.ItemConvertible;

@Mixin(Block.class)
public class BlockMixin implements ItemConvertible {
	@Shadow
	@Final
	public int id;

	@Override
	public Item asItem() {
		return Item.byId[this.id - 256];
	}
}
