package io.github.betterthanupdates.forge.mixin;

import forge.ForgeHooks;
import modloader.ModLoader;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.block.Block;
import net.minecraft.container.slot.CraftingResultSlot;
import net.minecraft.container.slot.Slot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.achievement.Achievements;

@Mixin(CraftingResultSlot.class)
public class CraftingResultSlotMixin extends Slot {
	@Shadow
	private PlayerEntity player;

	@Shadow
	@Final
	private Inventory craftingInventory;

	public CraftingResultSlotMixin(Inventory arg, int i, int j, int k) {
		super(arg, i, j, k);
	}

	/**
	 * @author Forge
	 * @reason
	 */
	@Overwrite
	public void onCrafted(ItemStack itemstack) {
		itemstack.onCrafted(this.player.world, this.player);
		if (itemstack.itemId == Block.WORKBENCH.id) {
			this.player.increaseStat(Achievements.CRAFT_WORKBENCH, 1);
		} else if (itemstack.itemId == Item.WOOD_PICKAXE.id) {
			this.player.increaseStat(Achievements.CRAFT_PICKAXE, 1);
		} else if (itemstack.itemId == Block.FURNACE.id) {
			this.player.increaseStat(Achievements.CRAFT_FURNACE, 1);
		} else if (itemstack.itemId == Item.WOOD_HOE.id) {
			this.player.increaseStat(Achievements.CRAFT_HOE, 1);
		} else if (itemstack.itemId == Item.BREAD.id) {
			this.player.increaseStat(Achievements.CRAFT_BREAD, 1);
		} else if (itemstack.itemId == Item.CAKE.id) {
			this.player.increaseStat(Achievements.CRAFT_CAKE, 1);
		} else if (itemstack.itemId == Item.STONE_PICKAXE.id) {
			this.player.increaseStat(Achievements.CRAFT_BETTER_PICKAXE, 1);
		} else if (itemstack.itemId == Item.WOOD_SWORD.id) {
			this.player.increaseStat(Achievements.CRAFT_SWORD, 1);
		}

		ModLoader.TakenFromCrafting(this.player, itemstack);
		ForgeHooks.onTakenFromCrafting(this.player, itemstack, this.craftingInventory);

		for (int i = 0; i < this.craftingInventory.getInventorySize(); ++i) {
			ItemStack itemstack1 = this.craftingInventory.getInventoryItem(i);
			if (itemstack1 != null) {
				this.craftingInventory.takeInventoryItem(i, 1);
				if (itemstack1.getItem().hasContainerItemType()) {
					this.craftingInventory.setInventoryItem(i, new ItemStack(itemstack1.getItem().getContainerItemType()));
				}
			}
		}

	}
}
