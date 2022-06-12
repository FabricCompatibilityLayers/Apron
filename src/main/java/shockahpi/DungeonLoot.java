package shockahpi;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import java.util.Random;

@SuppressWarnings("unused")
public class DungeonLoot {
	public final ItemStack loot;
	public final int min;
	public final int max;

	public DungeonLoot(ItemStack stack) {
		this.loot = new ItemStack(stack.itemId, 1, stack.getMeta());
		this.min = this.max = stack.count;
	}

	public DungeonLoot(ItemStack stack, int min, int max) {
		this.loot = new ItemStack(stack.itemId, 1, stack.getMeta());
		this.min = min;
		this.max = max;
	}

	public ItemStack getStack() {
		int damage = 0;
		if (this.loot.itemId <= 255) {
			if (Block.BY_ID[this.loot.itemId].getBaseColor(1) != 1) {
				damage = this.loot.getMeta();
			} else if (!this.loot.getItem().isRendered3d()) {
				damage = this.loot.getMeta();
			}
		}

		return new ItemStack(this.loot.itemId, this.min + (new Random()).nextInt(this.max - this.min + 1), damage);
	}
}
