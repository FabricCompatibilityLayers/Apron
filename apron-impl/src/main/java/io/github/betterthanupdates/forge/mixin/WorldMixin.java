package io.github.betterthanupdates.forge.mixin;

import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.block.Block;
import net.minecraft.util.math.AxixAlignedBoundingBox;
import net.minecraft.world.BlockView;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

import io.github.betterthanupdates.forge.block.ForgeBlock;
import io.github.betterthanupdates.forge.world.ForgeWorld;

@Mixin(World.class)
public abstract class WorldMixin implements BlockView, ForgeWorld {

	@Shadow
	public abstract boolean isBlockLoaded(int i, int j, int k);

	private int light;
	private boolean replaceable;

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Inject(method = "isAir", at = @At("RETURN"), cancellable = true)
	public void isAir(int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {
		int l = this.getBlockId(x, y, z);
		cir.setReturnValue(l == 0 || ((ForgeBlock) Block.BY_ID[l]).isAirBlock((World) (Object) this, x, y, z));
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Inject(method = "method_165", at = @At("HEAD"))
	public void method_165(LightType lightType, int x, int y, int z, int l, CallbackInfo ci) {
		if (this.isBlockLoaded(x, y, z) && lightType == LightType.field_2758) {
			int i1 = this.getBlockId(x, y, z);
			int bl = i1 == 0 ? 0 : ((ForgeBlock) Block.BY_ID[i1]).getLightValue(this, x, y, z);

			if (bl > l) {
				light = bl;
			} else {
				light = 0;
			}
		}
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@ModifyVariable(method = "method_165", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;method_164(Lnet/minecraft/world/LightType;III)I"), argsOnly = true, ordinal = 3)
	public int method_165(int l) {
		if (light != 0) {
			return light;
		}
		return l;
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Inject(method = "method_225", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockId(III)I", shift = At.Shift.BY, by = 4), cancellable = true, locals = LocalCapture.CAPTURE_FAILEXCEPTION)
	public void method_225(AxixAlignedBoundingBox var1, CallbackInfoReturnable<Boolean> cir, int i, int j, int k, int l, int i1, int j1, int k1, int l1, int i2, int j2) {
		if (j2 > 0 && ((ForgeBlock) Block.BY_ID[j2]).isBlockBurning((World) (Object) this, k1, l1, i2)) {
			cir.setReturnValue(true);
		}
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Inject(method = "canSuffocate", at = @At("HEAD"), cancellable = true)
	public void canSuffocate(int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {
		Block block = Block.BY_ID[this.getBlockId(x, y, z)];
		cir.setReturnValue(block != null && ((ForgeBlock) block).isBlockNormalCube((World) (Object) this, x, y, z));
	}

	@Override
	public boolean isBlockSolidOnSide(int x, int y, int z, int side) {
		Block block = Block.BY_ID[this.getBlockId(x, y, z)];
		return block != null && ((ForgeBlock) block).isBlockSolidOnSide((World) (Object) this, x, y, z, side);
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Inject(method = "canPlaceBlock", at = @At("HEAD"))
	public void canPlaceBlock(int id, int x, int y, int z, boolean flag, int i1, CallbackInfoReturnable<Boolean> cir) {
		int j1 = this.getBlockId(x, y, z);
		Block block = Block.BY_ID[j1];
		replaceable = block != null && ((ForgeBlock) block).isBlockReplaceable((World) (Object) this, x, y, z);
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@ModifyVariable(method = "canPlaceBlock", index = 8, at = @At(value = "STORE"))
	public Block canPlaceBlock(Block block) {
		if (replaceable) {
			return null;
		}
		return block;
	}
}
