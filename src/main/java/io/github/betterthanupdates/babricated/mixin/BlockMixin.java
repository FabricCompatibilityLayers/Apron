package io.github.betterthanupdates.babricated.mixin;

import io.github.betterthanupdates.babricated.item.ItemConvertible;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Block.class)
public class BlockMixin implements ItemConvertible {
	@Shadow @Final public int id;

	@Override
	public Item asItem() {
		return Item.byId[this.id - 256];
	}
}
