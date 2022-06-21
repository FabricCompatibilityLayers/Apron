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
	 * @author Eloraam
	 * @reason implement Forge hooks
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
	 * @author Eloraam
	 * @reason implement forge hooks
	 */
	@Overwrite
	public boolean useItemOnBlock(PlayerEntity player, World world, ItemStack stack, int x, int y, int z, int side) {
		if (stack != null && stack.getItem() instanceof IUseItemFirst) {
			IUseItemFirst iuif = (IUseItemFirst) stack.getItem();

			if (iuif.onItemUseFirst(stack, player, world, x, y, z, side)) {
				return true;
			}
		}

		int i1 = world.getBlockId(x, y, z);

		if (i1 > 0 && Block.BY_ID[i1].canUse(world, x, y, z, player)) {
			return true;
		} else if (stack == null) {
			return false;
		} else if (!stack.useOnBlock(player, world, x, y, z, side)) {
			return false;
		} else {
			if (stack.count == 0) {
				ForgeHooks.onDestroyCurrentItem(player, stack);
			}

			return true;
		}
	}
}
