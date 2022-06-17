package io.github.betterthanupdates.forge.mixin;

import forge.ForgeHooks;
import forge.IUseItemFirst;
import net.minecraft.block.Block;
import net.minecraft.client.ClientInteractionManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ClientInteractionManager.class)
public class ClientInteractionManagerMixin {

    /**
     * @author Forge
     * @reason
     */
    @Overwrite
    public boolean method_1712(PlayerEntity entityplayer, World world, ItemStack itemstack) {
        int i = itemstack.count;
        ItemStack itemstack1 = itemstack.use(world, entityplayer);
        if (itemstack1 != itemstack || itemstack1 != null && itemstack1.count != i) {
            entityplayer.inventory.main[entityplayer.inventory.selectedHotBarSlot] = itemstack1;
            if (itemstack1.count == 0) {
                entityplayer.inventory.main[entityplayer.inventory.selectedHotBarSlot] = null;
                ForgeHooks.onDestroyCurrentItem(entityplayer, itemstack1);
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
    public boolean useItemOnBlock(PlayerEntity entityplayer, World world, ItemStack itemstack, int i, int j, int k, int l) {
        if (itemstack != null && itemstack.getItem() instanceof IUseItemFirst) {
            IUseItemFirst iuif = (IUseItemFirst)itemstack.getItem();
            if (iuif.onItemUseFirst(itemstack, entityplayer, world, i, j, k, l)) {
                return true;
            }
        }

        int i1 = world.getBlockId(i, j, k);
        if (i1 > 0 && Block.BY_ID[i1].canUse(world, i, j, k, entityplayer)) {
            return true;
        } else if (itemstack == null) {
            return false;
        } else if (!itemstack.useOnBlock(entityplayer, world, i, j, k, l)) {
            return false;
        } else {
            if (itemstack.count == 0) {
                ForgeHooks.onDestroyCurrentItem(entityplayer, itemstack);
            }

            return true;
        }
    }
}
