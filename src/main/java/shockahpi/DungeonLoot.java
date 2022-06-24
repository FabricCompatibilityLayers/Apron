package shockahpi;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import io.github.betterthanupdates.Legacy;

@SuppressWarnings("unused")
@Legacy
public class DungeonLoot {
	@Legacy
	public final ItemStack loot;
	@Legacy
	public final int min;
	@Legacy
	public final int max;

	@Legacy
	public DungeonLoot(ItemStack stack) {
		this.loot = new ItemStack(stack.itemId, 1, stack.getMeta());
		this.min = this.max = stack.count;
	}

	@Legacy
	public DungeonLoot(ItemStack stack, int min, int max) {
		this.loot = new ItemStack(stack.itemId, 1, stack.getMeta());
		this.min = min;
		this.max = max;
	}

	@Legacy
	public ItemStack getStack() {
		int damage = 0;

		if (this.loot.itemId <= 255) {
			if (Block.BY_ID[this.loot.itemId].getBaseColor(1) != 1) {
				damage = this.loot.getMeta();
			} else if (!this.loot.getItem().isRendered3d) {
				damage = this.loot.getMeta();
			}
		}

		return new ItemStack(this.loot.itemId, this.min + new Random().nextInt(this.max - this.min + 1), damage);
	}
}
