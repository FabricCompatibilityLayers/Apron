package io.github.betterthanupdates.forge.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.RedstoneRepeater;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(RedstoneRepeater.class)
public class RedstoneRepeaterMixin extends Block {
    protected RedstoneRepeaterMixin(int i, Material arg) {
        super(i, arg);
    }

    /**
     * @author Forge
     */
    @Overwrite
    public boolean canPlaceAt(World world, int i, int j, int k) {
        return !world.isBlockSolidOnSide(i, j - 1, k, 1) ? false : super.canPlaceAt(world, i, j, k);
    }

    /**
     * @author Forge
     */
    @Overwrite
    public boolean canGrow(World world, int i, int j, int k) {
        return !world.isBlockSolidOnSide(i, j - 1, k, 1) ? false : super.canGrow(world, i, j, k);
    }
}
