package io.github.betterthanupdates.forge.mixin;

import io.github.betterthanupdates.forge.world.ForgeWorld;
import net.minecraft.block.Block;
import net.minecraft.block.RedstoneRepeaterBlock;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(RedstoneRepeaterBlock.class)
public class RedstoneRepeaterBlockMixin extends Block {
	protected RedstoneRepeaterBlockMixin(int i, Material arg) {
		super(i, arg);
	}

	/**
	 * @author Forge
	 * @reason
	 */
	@Overwrite
	public boolean canPlaceAt(World world, int i, int j, int k) {
		return !((ForgeWorld)world).isBlockSolidOnSide(i, j - 1, k, 1) ? false : super.canPlaceAt(world, i, j, k);
	}

	/**
	 * @author Forge
	 * @reason
	 */
	@Overwrite
	public boolean canGrow(World world, int i, int j, int k) {
		return !((ForgeWorld)world).isBlockSolidOnSide(i, j - 1, k, 1) ? false : super.canGrow(world, i, j, k);
	}
}
