package io.github.betterthanupdates.apron.compat.mixin.client.betterthanwolves;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Block;
import net.minecraft.block.CakeBlock;
import net.minecraft.block.Material;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

import io.github.betterthanupdates.apron.compat.betterthanwolves.BTWCakeBlock;

@Mixin(CakeBlock.class)
public class CakeBlockMixin extends Block implements BTWCakeBlock {
	protected CakeBlockMixin(int i, Material arg) {
		super(i, arg);
	}

	@Override
	public void onPlaced(World world, int i, int j, int k) {
		super.onPlaced(world, i, j, k);
		boolean bReceivingRedstone = world.method_263(i, j, k);
		if (bReceivingRedstone) {
			this.SetRedstoneOn(world, i, j, k, true);
			world.playSound((double)i + 0.5, (double)j + 0.5, (double)k + 0.5, "mob.ghast.scream", 1.0F, world.field_214.nextFloat() * 0.4F + 0.8F);
		}
	}

	@ModifyVariable(method = "updateBoundingBox", at = @At("HEAD"), ordinal = 0)
	private int btw$updateBoundingBox(int var5, @Local BlockView iblockaccess, @Local(ordinal = 0) int i, @Local(ordinal = 1) int j, @Local(ordinal = 2) int k) {
		return this.GetEatState(iblockaccess, i, j, k);
	}

	@ModifyVariable(method = "getCollisionShape", at = @At("HEAD"), ordinal = 0)
	private int btw$getCollisionShape(int var5, @Local World arg, @Local(ordinal = 0) int i, @Local(ordinal = 1) int j, @Local(ordinal = 2) int k) {
		return this.GetEatState(arg, i, j, k);
	}

	@ModifyVariable(method = "getBoundingBox", at = @At("HEAD"), ordinal = 0)
	private int btw$getOutlineShape(int var5, @Local World arg, @Local(ordinal = 0) int i, @Local(ordinal = 1) int j, @Local(ordinal = 2) int k) {
		return this.GetEatState(arg, i, j, k);
	}

	@ModifyVariable(method = "getTexture(II)I", at = @At("HEAD"), ordinal = 1, argsOnly = true)
	private int btw$getTextureForSide(int value) {
		return value & 7;
	}

	@ModifyVariable(method = "method_1528", at = @At("HEAD"), ordinal = 0)
	private int btw$method_1528$1(int var6, @Local World arg, @Local(ordinal = 0) int i, @Local(ordinal = 1) int j, @Local(ordinal = 2) int k) {
		return this.GetEatState(arg, i, j, k) + 1;
	}

	@Redirect(method = "method_1528", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;method_215(IIII)V"))
	private void btw$method_1528$2(World instance, int j, int k, int l, int i, @Local(ordinal = 3) int var6) {
		this.SetEatState(instance, i, j, k, var6);
	}

	@Inject(method = "neighborUpdate", at = @At("TAIL"))
	private void addBTWCheck(World world, int i, int j, int k, int l, CallbackInfo callbackInfo) {
		if (this.canGrow(world, i, j, k)) {
			boolean bOn = this.IsRedstoneOn(world, i, j, k);
			boolean bReceivingRedstone = world.method_263(i, j, k);
			if (bOn != bReceivingRedstone) {
				this.SetRedstoneOn(world, i, j, k, bReceivingRedstone);
				if (bReceivingRedstone) {
					world.playSound((double)i + 0.5, (double)j + 0.5, (double)k + 0.5, "mob.ghast.scream", 1.0F, world.field_214.nextFloat() * 0.4F + 0.8F);
				}
			}
		}
	}

	@Override
	public boolean IsRedstoneOn(BlockView iBlockAccess, int i, int j, int k) {
		return (iBlockAccess.getBlockMeta(i, j, k) & 8) > 0;
	}

	@Override
	public void SetRedstoneOn(World world, int i, int j, int k, boolean bOn) {
		int iMetaData = world.getBlockMeta(i, j, k) & -9;
		if (bOn) {
			iMetaData |= 8;
		}

		world.method_215(i, j, k, iMetaData);
	}

	@Override
	public int GetEatState(BlockView iBlockAccess, int i, int j, int k) {
		return iBlockAccess.getBlockMeta(i, j, k) & 7;
	}

	@Override
	public void SetEatState(World world, int i, int j, int k, int state) {
		int iMetaData = world.getBlockMeta(i, j, k) & 8;
		iMetaData |= state;
		world.method_215(i, j, k, iMetaData);
	}

	@Override
	public void randomDisplayTick(World world, int i, int j, int k, Random random) {
		if (this.IsRedstoneOn(world, i, j, k)) {
			double d = (double)i + 0.5 + ((double)random.nextFloat() - 0.5) * 0.666;
			double d1 = (double)((float)j) + 0.65;
			double d2 = (double)k + 0.5 + ((double)random.nextFloat() - 0.5) * 0.666;
			float f = 0.06666667F;
			float f1 = f * 0.6F + 0.4F;
			float f2 = f * f * 0.7F - 0.5F;
			float f3 = f * f * 0.6F - 0.7F;
			if (f2 < 0.0F) {
				f2 = 0.0F;
			}

			if (f3 < 0.0F) {
				f3 = 0.0F;
			}

			world.addParticle("reddust", d, d1, d2, (double)f1, (double)f2, (double)f3);
		}
	}
}
