package io.github.betterthanupdates.forge.mixin.world;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Block;
import net.minecraft.util.math.Box;
import net.minecraft.world.BlockView;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.betterthanupdates.forge.block.ForgeBlock;
import io.github.betterthanupdates.forge.world.ForgeWorld;

@Mixin(World.class)
public abstract class WorldMixin implements BlockView, ForgeWorld {

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@ModifyReturnValue(method = "method_234", at = @At("RETURN"))
	public boolean forge$isAir(boolean original, int x, int y, int z) {
		int l = this.getBlockId(x, y, z);
		return original || ((ForgeBlock) Block.BLOCKS[l]).isAirBlock((World) (Object) this, x, y, z);
	}

	@WrapOperation(method = "method_165", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockId(III)I"))
	private int forge$method_165$computeLightValue(World instance, int x, int y, int z, Operation<Integer> operation, @Share("computed") LocalRef<Integer> ref, @Local(ordinal = 3) int l) {
		int blockId = operation.call(instance, x, y, z);

		ref.set(Math.max(
				blockId == 0 ? 0 :
						((ForgeBlock) Block.BLOCKS[blockId]).getLightValue(this, x, y, z),
				l));

		return blockId;
	}

	@Inject(method = "method_165", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;method_164(Lnet/minecraft/world/LightType;III)I"))
	private void forge$method_165$fixVar(LightType i, int j, int k, int l, int par5, CallbackInfo ci, @Share("computed") LocalRef<Integer> ref, @Local(ordinal = 3) LocalIntRef lRef) {
		Integer computed = ref.get();

		if (computed != null) {
			lRef.set(Math.max(computed, lRef.get()));
		}
	}

	/**
	 * @author Eloraam
	 * @reason implement Forge hooks
	 */
	@Inject(method = "method_225", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockId(III)I", shift = At.Shift.BY, by = 4), cancellable = true)
	public void forge$method_225(Box var1, CallbackInfoReturnable<Boolean> cir, @Local(index = 8) int k1, @Local(index = 9) int l1, @Local(index = 10) int i2, @Local(index = 11) int j2) {
		if (j2 > 0 && ((ForgeBlock) Block.BLOCKS[j2]).isBlockBurning((World) (Object) this, k1, l1, i2)) {
			cir.setReturnValue(true);
		}
	}

	@ModifyReturnValue(method = "method_1780", at = @At(value = "RETURN", ordinal = 1))
	public boolean forge$canSuffocate(boolean original, @Local(ordinal = 0) int x, @Local(ordinal = 1) int y, @Local(ordinal = 2) int z, @Local(ordinal = 0) Block block) {
		return ((ForgeBlock) block).isBlockNormalCube((World) (Object) this, x, y, z) || original;
	}

	@Override
	public boolean isBlockSolidOnSide(int x, int y, int z, int side) {
		Block block = Block.BLOCKS[this.getBlockId(x, y, z)];
		return block != null && ((ForgeBlock) block).isBlockSolidOnSide((World) (Object) this, x, y, z, side);
	}

	@ModifyVariable(method = "method_156", ordinal = 0, at = @At(value = "LOAD", ordinal = 6))
	private Block forge$replaceBlock(Block block, @Local(ordinal = 1) int j, @Local(ordinal = 2) int k, @Local(ordinal = 2) int l) {
		if (block != null && ((ForgeBlock) block).isBlockReplaceable((World) (Object) this, j, k, l)) {
			return null;
		}

		return block;
	}
}
