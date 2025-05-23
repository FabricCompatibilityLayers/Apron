package shockahpi;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import io.github.betterthanupdates.Legacy;

@SuppressWarnings("unused")
@Legacy
public class DungeonLoot {
	public final ItemStack loot;
	public final int min;
	public final int max;

	public DungeonLoot(ItemStack stack) {
		this.loot = new ItemStack(stack.itemId, 1, stack.getDamage());
		this.min = this.max = stack.count;
	}

	public DungeonLoot(ItemStack stack, int min, int max) {
		this.loot = new ItemStack(stack.itemId, 1, stack.getDamage());
		this.min = min;
		this.max = max;
	}

	public ItemStack getStack() {
		int damage = 0;
		if (this.loot.itemId <= 255) {
			if (Block.BLOCKS[this.loot.itemId].getColor(1) != 1) {
				damage = this.loot.getDamage();
			} else if (!this.loot.getItem().handheld) {
				damage = this.loot.getDamage();
			}
		}

		return new ItemStack(this.loot.itemId, this.min + (new Random()).nextInt(this.max - this.min + 1), damage);
	}
}
