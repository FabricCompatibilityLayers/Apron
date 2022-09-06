package io.github.betterthanupdates.stapi.mixin.vanilla;

import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Registry;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Block;

import io.github.betterthanupdates.stapi.StAPIBlockHelper;
import io.github.betterthanupdates.stapi.StationAPIHelper;

@Mixin(Block.class)
public class BlockMixin implements StAPIBlockHelper {
	@Shadow
	@Final
	public int id;

	@Unique
	private int originalId = 0;

	@ModifyVariable(method = "<init>(ILnet/minecraft/block/material/Material;)V",
			at = @At(value = "FIELD", opcode = Opcodes.GETSTATIC, target = "Lnet/minecraft/block/Block;BY_ID:[Lnet/minecraft/block/Block;", ordinal = 0),
			ordinal = 0, argsOnly = true)
	private int apron_stapi_assignBlockId(int id) {
		this.originalId = id;
		return StationAPIHelper.assignBlockId(id);
	}

	@Inject(method = "setTranslationKey", at = @At("RETURN"))
	private void apron_stapi_registerBlock(String key, CallbackInfoReturnable<Block> cir) {
		if (StationAPIHelper.BLOCKS.containsKey(this.id) && !BlockRegistry.INSTANCE.getKey((Block) (Object) this).isPresent()) {
			String id = key;

			if (BlockRegistry.INSTANCE.containsId(StationAPIHelper.BLOCKS.get(this.id).id(id))) {
				int i = 0;
				String alternativeId = id + i;

				while (BlockRegistry.INSTANCE.containsId(StationAPIHelper.BLOCKS.get(this.id).id(alternativeId))) {
					i++;
					alternativeId = id + i;
				}

				id = alternativeId;
			}

			Registry.register(BlockRegistry.INSTANCE, StationAPIHelper.BLOCKS.get(this.id).id(id), (Block) (Object) this);
		}
	}

	@Override
	public void register() {
		if (StationAPIHelper.BLOCKS.containsKey(this.id)) {
			String id = this.originalId + "_";

			if (BlockRegistry.INSTANCE.containsId(StationAPIHelper.BLOCKS.get(this.id).id(id))) {
				int i = 0;
				String alternativeId = id + i;

				while (BlockRegistry.INSTANCE.containsId(StationAPIHelper.BLOCKS.get(this.id).id(alternativeId))) {
					i++;
					alternativeId = id + i;
				}

				id = alternativeId;
			}

			Registry.register(BlockRegistry.INSTANCE, StationAPIHelper.BLOCKS.get(this.id).id(id), (Block) (Object) this);
		}
	}
}
