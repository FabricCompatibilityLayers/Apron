package io.github.betterthanupdates.apron.compat.mixin.betterthanwolves;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Block;
import net.minecraft.block.CakeBlock;
import net.minecraft.block.material.Material;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(CakeBlock.class)
public class CakeBlockMixin extends Block {
	protected CakeBlockMixin(int i, Material arg) {
		super(i, arg);
	}

	@Override
	public void onBlockPlaced(World arg, int i, int j, int k) {
		super.onBlockPlaced(arg, i, j, k);
		if(arg.method_263(i, j, k)) {
			setRedstoneOn(arg, i, j, k, true);
			arg.playSound((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "mob.ghast.scream", 1.0F, arg.rand.nextFloat() * 0.4F + 0.8F);
		}
	}

	@ModifyVariable(method = "updateBoundingBox", at = @At("HEAD"), ordinal = 0)
	private int changeToBTWMedthod_1(int var5, @Local BlockView iblockaccess, @Local int i, @Local int j, @Local int k) {
		return getEatState(iblockaccess, i, j, k);
	}

	@ModifyVariable(method = "getCollisionShape", at = @At("HEAD"), ordinal = 0)
	private int changeToBTWMedthod_2(int var5, @Local World arg, @Local int i, @Local int j, @Local int k) {
		return getEatState(arg, i, j, k);
	}

	@ModifyVariable(method = "getOutlineShape", at = @At("HEAD"), ordinal = 0)
	private int changeToBTWMedthod_3(int var5, @Local World arg, @Local int i, @Local int j, @Local int k) {
		return getEatState(arg, i, j, k);
	}

	@ModifyVariable(method = "method_1528", at = @At("HEAD"), ordinal = 0)
	private int changeToBTWMedthod_4(int var6, @Local World arg, @Local int i, @Local int j, @Local int k) {
		return getEatState(arg, i, j, k) + 1;
	}

	@Redirect(method = "method_1528", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockMeta(IIII)V"))
	private void changeToBTWMedthod_5(World instance, int j, int k, int l, int i, @Local int var6) {
		setEatState(instance, i, j, k, var6);
	}

	@Inject(method = "onAdjacentBlockUpdate", at = @At("TAIL"))
	private void addBTWCheck(World arg, int i, int j, int k, int l, CallbackInfo callbackInfo) {
		if (((CakeBlock)(Object)this).canGrow(arg, i, j, k)) {
			boolean bOn = isRedstoneOn(arg, i, j, k);
			boolean bReceivingRedstone = arg.method_263(i, j, k);
			if (bOn != bReceivingRedstone) {
				setRedstoneOn(arg, i, j, k, bReceivingRedstone);
				if (bReceivingRedstone) {
					arg.playSound((double)i + 0.5, (double)j + 0.5, (double)k + 0.5, "mob.ghast.scream", 1.0F, arg.rand.nextFloat() * 0.4F + 0.8F);
				}
			}
		}
	}

	public boolean isRedstoneOn(BlockView iBlockAccess, int i, int j, int k) {
		return (iBlockAccess.getBlockMeta(i, j, k) & 8) > 0;
	}

	public void setRedstoneOn(World world, int i, int j, int k, boolean bOn) {
		int iMetaData = world.getBlockMeta(i, j, k) & -9;
		if (bOn) {
			iMetaData |= 8;
		}

		world.setBlockMeta(i, j, k, iMetaData);
	}

	public int getEatState(BlockView iBlockAccess, int i, int j, int k) {
		return iBlockAccess.getBlockMeta(i, j, k) & 7;
	}

	public void setEatState(World world, int i, int j, int k, int state) {
		int iMetaData = world.getBlockMeta(i, j, k) & 8;
		iMetaData |= state;
		world.setBlockMeta(i, j, k, iMetaData);
	}

	@Override
	public void randomDisplayTick(World world, int i, int j, int k, Random random) {
		if (isRedstoneOn(world, i, j, k)) {
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
