package io.github.betterthanupdates.stapi.mixin.vanilla;

import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Registry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

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

	@Inject(method = "<init>(ILnet/minecraft/block/material/Material;)V", at = @At("RETURN"))
	private void apron_stapi_registerBlock(int arg, Material par2, CallbackInfo ci) {
		System.out.println(this.id + "->" + this.toString());

		if (StationAPIHelper.BLOCKS.containsKey(this.id)) {
			System.out.println(this.id);
			//			Registry.register(BlockRegistry.INSTANCE, this.id, StationAPIHelper.BLOCKS.get(this.id).toString(), (Block) (Object) this);
			Registry.register(BlockRegistry.INSTANCE, StationAPIHelper.BLOCKS.get(this.id), (Block) (Object) this);
		}
	}
}
