package io.github.betterthanupdates.forge.mixin.client;

import forge.ForgeHooks;
import forge.IUseItemFirst;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.minecraft.block.Block;
import net.minecraft.client.ClientInteractionManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
@Mixin(ClientInteractionManager.class)
public class ClientInteractionManagerMixin {
	/**
	 * @author Forge
	 * @reason
	 */
	@Overwrite
	public boolean method_1712(PlayerEntity player, World world, ItemStack stack) {
		int i = stack.count;
		ItemStack stack1 = stack.use(world, player);

		if (stack1 != stack || stack1 != null && stack1.count != i) {
			player.inventory.main[player.inventory.selectedHotBarSlot] = stack1;

			if (stack1.count == 0) {
				player.inventory.main[player.inventory.selectedHotBarSlot] = null;
				ForgeHooks.onDestroyCurrentItem(player, stack1);
			}

			return true;
		} else {
			return false;
		}
	}

	/**
	 * @author Forge
	 * @reason
	 */
	@Overwrite
	public boolean useItemOnBlock(PlayerEntity player, World world, ItemStack itemstack, int i, int j, int k, int l) {
		if (itemstack != null && itemstack.getItem() instanceof IUseItemFirst) {
			IUseItemFirst iuif = (IUseItemFirst) itemstack.getItem();

			if (iuif.onItemUseFirst(itemstack, player, world, i, j, k, l)) {
				return true;
			}
		}

		int i1 = world.getBlockId(i, j, k);

		if (i1 > 0 && Block.BY_ID[i1].canUse(world, i, j, k, player)) {
			return true;
		} else if (itemstack == null) {
			return false;
		} else if (!itemstack.useOnBlock(player, world, i, j, k, l)) {
			return false;
		} else {
			if (itemstack.count == 0) {
				ForgeHooks.onDestroyCurrentItem(player, itemstack);
			}

			return true;
		}
	}
}
