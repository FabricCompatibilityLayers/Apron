package io.github.betterthanupdates.forge.mixin;

import forge.ForgeHooks;
import io.github.betterthanupdates.forge.block.ForgeBlock;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

/**
 * Default implementation of the new methods provided by Minecraft Forge
 */
@Mixin(Block.class)
public abstract class BlockMixin implements ForgeBlock {

	@Shadow protected float hardness;

	@Shadow @Final public static int[] EMITTANCE;

	@Shadow @Final public int id;

	@Shadow @Final public Material material;

	@Shadow public abstract boolean isFullCube();

	/**
	 * @author Forge
	 */
	@Environment(EnvType.CLIENT)
	@Overwrite
	public float getBrightness(BlockView blockView, int i, int j, int k) {
		return blockView.getNaturalBrightness(i, j, k, this.getLightValue(blockView, i, j, k));
	}

	/**
	 * @author Forge
	 */
	@Overwrite
	public float getHardness(PlayerEntity entityplayer) {
		return this.blockStrength(entityplayer, 0);
	}

	@Override
	public int getLightValue(BlockView iba, int i, int j, int k) {
		return EMITTANCE[this.id];
	}

	@Override
	public boolean isLadder() {
		return false;
	}

	@Override
	public boolean isBlockNormalCube(World world, int i, int j, int k) {
		return this.material.hasNoSuffocation() && this.isFullCube();
	}

	@Override
	public boolean isBlockSolidOnSide(World world, int i, int j, int k, int side) {
		return this.isBlockNormalCube(world, i, j, k);
	}

	@Override
	public boolean isBlockReplaceable(World world, int i, int j, int k) {
		return false;
	}

	@Override
	public boolean isBlockBurning(World world, int i, int j, int k) {
		return false;
	}

	@Override
	public boolean isAirBlock(World world, int i, int j, int k) {
		return false;
	}

	/**
	 * Gets hardness for a block, taking into account its hardness.<br>
	 * By default, this has the same behavior as {@link Block#getHardness()}
	 * @param meta the meta value of the block state
	 * @return the hardness (time to break) of the block
	 */
	@Override
	public float getHardness(int meta) {
		return this.hardness;
	}

	@Override
	public float blockStrength(World world, PlayerEntity player, int i, int j, int k) {
		int md = world.getBlockMeta(i, j, k);
		return this.blockStrength(player, md);
	}

	@Override
	public float blockStrength(PlayerEntity player, int md) {
		return ForgeHooks.blockStrength((Block)(Object) this, player, md);
	}

	@Override
	public boolean canHarvestBlock(PlayerEntity player, int md) {
		return ForgeHooks.canHarvestBlock((Block)(Object) this, player, md);
	}

	// Idk it was there.
	static Class _mthclass$(String s) {
		try {
			return Class.forName(s);
		} catch (ClassNotFoundException var2) {
			throw new NoClassDefFoundError(var2.getMessage());
		}
	}
}
