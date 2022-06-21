package io.github.betterthanupdates.forge.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import io.github.betterthanupdates.forge.block.ForgeBlock;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	@Shadow
	public float field_1048;

	@Shadow
	public float field_1050;

	public LivingEntityMixin(World world) {
		super(world);
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Overwrite
	public boolean method_932() {
		int i = MathHelper.floor(this.x);
		int j = MathHelper.floor(this.boundingBox.minY);
		int k = MathHelper.floor(this.z);
		Block block = Block.BY_ID[this.world.getBlockId(i, j, k)];
		return block == null ? false : ((ForgeBlock) block).isLadder();
	}
}
