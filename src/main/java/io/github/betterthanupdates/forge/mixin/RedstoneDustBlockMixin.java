package io.github.betterthanupdates.forge.mixin;

import forge.IConnectRedstone;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.minecraft.block.Block;
import net.minecraft.block.RedstoneDustBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.number.BedMagicNumbers;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import io.github.betterthanupdates.forge.world.ForgeWorld;

@Mixin(RedstoneDustBlock.class)
public class RedstoneDustBlockMixin extends Block {
	protected RedstoneDustBlockMixin(int i, Material arg) {
		super(i, arg);
	}

	/**
	 * @author Forge
	 * @reason
	 */
	@Overwrite
	public boolean canPlaceAt(World world, int i, int j, int k) {
		return ((ForgeWorld) world).isBlockSolidOnSide(i, j - 1, k, 1);
	}

	/**
	 * @author Forge
	 * @reason
	 */
	@Overwrite
	public static boolean method_1287(BlockView blockView, int i, int j, int k, int l) {
		int i1 = blockView.getBlockId(i, j, k);

		if (i1 == Block.REDSTONE_DUST.id) {
			return true;
		} else if (i1 == 0) {
			return false;
		} else if (Block.BY_ID[i1] instanceof IConnectRedstone) {
			IConnectRedstone icr = (IConnectRedstone) Block.BY_ID[i1];
			return icr.canConnectRedstone(blockView, i, j, k, l);
		} else if (Block.BY_ID[i1].getEmitsRedstonePower()) {
			return true;
		} else if (i1 != Block.REDSTONE_REPEATER.id && i1 != Block.REDSTONE_REPEATER_LIT.id) {
			return false;
		} else {
			int j1 = blockView.getBlockMeta(i, j, k);
			return l == BedMagicNumbers.field_793[j1 & 3];
		}
	}
}
