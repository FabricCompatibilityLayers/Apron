package io.github.betterthanupdates.stapi.mixin.vanilla;

import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Registry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Block;

import io.github.betterthanupdates.stapi.StationAPIHelper;

@Mixin(Block.class)
public class BlockMixin {
	@Shadow
	@Final
	public int id;

	@ModifyVariable(method = "<init>(ILnet/minecraft/block/material/Material;)V", at = @At("HEAD"), ordinal = 0, argsOnly = true)
	private static int apron_stapi_assignBlockId(int id) {
		return StationAPIHelper.assignBlockId(id);
	}

	@Inject(method = "setTranslationKey", at = @At("RETURN"))
	private void apron_stapi_registerBlock(String key, CallbackInfoReturnable<Block> cir) {
		if (StationAPIHelper.BLOCKS.containsKey(this.id)) {
			Registry.register(BlockRegistry.INSTANCE, StationAPIHelper.BLOCKS.get(this.id).id(key), (Block) (Object) this);
		}
	}
}
