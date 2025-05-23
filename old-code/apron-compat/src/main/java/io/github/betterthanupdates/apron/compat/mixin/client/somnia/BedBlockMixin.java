package io.github.betterthanupdates.apron.compat.mixin.client.somnia;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.mod_Somnia;
import net.minecraft.world.World;

@Mixin(BedBlock.class)
public class BedBlockMixin extends Block {
	protected BedBlockMixin(int i, Material arg) {
		super(i, arg);
	}

	@Inject(method = "<init>", at = @At("RETURN"))
	private void somnia$ctr(int par1, CallbackInfo ci) {
		this.setSoundGroup(Block.WOOL_SOUND_GROUP);
	}

	@Override
	public Block setHardness(float f) {
		return super.setHardness(0.8F);
	}

	@Inject(method = "onUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;method_495(III)Lnet/minecraft/class_141;"), cancellable = true)
	private void somnia$canUse(World world, int i, int j, int k, PlayerEntity entityplayer, CallbackInfoReturnable<Boolean> cir) {
		if (mod_Somnia.isValidActivation(world, entityplayer)) {
			mod_Somnia.openGuiBedIfPossible(entityplayer, world, i, j, k);
			cir.setReturnValue(true);
		}
	}
}
