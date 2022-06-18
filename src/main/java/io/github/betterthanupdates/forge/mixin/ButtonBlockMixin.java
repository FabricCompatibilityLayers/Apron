package io.github.betterthanupdates.forge.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.ButtonBlock;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ButtonBlock.class)
public abstract class ButtonBlockMixin extends Block {

    @Shadow protected abstract boolean method_1048(World arg, int i, int j, int k);

    protected ButtonBlockMixin(int i, Material arg) {
        super(i, arg);
    }

    /**
     * @author Forge
     */
    @Overwrite
    public boolean canPlaceAt(World world, int i, int j, int k, int l) {
        if (l == 2 && world.isBlockSolidOnSide(i, j, k + 1, 2)) {
            return true;
        } else if (l == 3 && world.isBlockSolidOnSide(i, j, k - 1, 3)) {
            return true;
        } else if (l == 4 && world.isBlockSolidOnSide(i + 1, j, k, 4)) {
            return true;
        } else {
            return l == 5 && world.isBlockSolidOnSide(i - 1, j, k, 5);
        }
    }

    /**
     * @author Forge
     */
    @Overwrite
    public boolean canPlaceAt(World world, int i, int j, int k) {
        if (world.isBlockSolidOnSide(i - 1, j, k, 5)) {
            return true;
        } else if (world.isBlockSolidOnSide(i + 1, j, k, 4)) {
            return true;
        } else {
            return world.isBlockSolidOnSide(i, j, k - 1, 3) ? true : world.isBlockSolidOnSide(i, j, k + 1, 2);
        }
    }

    /**
     * @author Forge
     */
    @Overwrite
    public void onBlockPlaced(World world, int i, int j, int k, int l) {
        int i1 = world.getBlockMeta(i, j, k);
        int j1 = i1 & 8;
        i1 &= 7;
        if (l == 2 && world.isBlockSolidOnSide(i, j, k + 1, 2)) {
            i1 = 4;
        } else if (l == 3 && world.isBlockSolidOnSide(i, j, k - 1, 3)) {
            i1 = 3;
        } else if (l == 4 && world.isBlockSolidOnSide(i + 1, j, k, 4)) {
            i1 = 2;
        } else if (l == 5 && world.isBlockSolidOnSide(i - 1, j, k, 5)) {
            i1 = 1;
        } else {
            i1 = this.method_1047(world, i, j, k);
        }

        world.setBlockMeta(i, j, k, i1 + j1);
    }

    /**
     * @author Forge
     */
    @Overwrite
    private int method_1047(World world, int i, int j, int k) {
        if (world.isBlockSolidOnSide(i - 1, j, k, 5)) {
            return 1;
        } else if (world.isBlockSolidOnSide(i + 1, j, k, 4)) {
            return 2;
        } else if (world.isBlockSolidOnSide(i, j, k - 1, 3)) {
            return 3;
        } else {
            return !world.isBlockSolidOnSide(i, j, k + 1, 2) ? 1 : 4;
        }
    }

    /**
     * @author Forge
     */
    @Overwrite
    public void onAdjacentBlockUpdate(World world, int i, int j, int k, int l) {
        if (this.method_1048(world, i, j, k)) {
            int i1 = world.getBlockMeta(i, j, k) & 7;
            boolean flag = false;
            if (!world.isBlockSolidOnSide(i - 1, j, k, 5) && i1 == 1) {
                flag = true;
            }

            if (!world.isBlockSolidOnSide(i + 1, j, k, 4) && i1 == 2) {
                flag = true;
            }

            if (!world.isBlockSolidOnSide(i, j, k - 1, 3) && i1 == 3) {
                flag = true;
            }

            if (!world.isBlockSolidOnSide(i, j, k + 1, 2) && i1 == 4) {
                flag = true;
            }

            if (flag) {
                this.drop(world, i, j, k, world.getBlockMeta(i, j, k));
                world.setBlock(i, j, k, 0);
            }
        }

    }
}
