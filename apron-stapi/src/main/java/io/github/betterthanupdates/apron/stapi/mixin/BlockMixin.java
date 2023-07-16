package io.github.betterthanupdates.apron.stapi.mixin;

import io.github.betterthanupdates.apron.StAPIBlock;
import io.github.betterthanupdates.apron.stapi.ApronStAPICompat;
import io.github.betterthanupdates.apron.stapi.ModContents;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.template.block.BlockTemplate;
import net.modificationstation.stationapi.api.template.item.ItemTemplate;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.Random;

@Mixin(Block.class)
public class BlockMixin implements StAPIBlock {
	@Shadow
	@Final
	public int id;
	@Unique
	private static int currentId = -1;

	@ModifyVariable(method = "<init>(ILnet/minecraft/block/material/Material;)V", argsOnly = true, at = @At(value = "HEAD"))
	private static int modifyItemId(int i) {
		if (ApronStAPICompat.isModLoaderTime()) {
			ModContents currentMod = ApronStAPICompat.getModContent();
			currentId = i;

			return currentMod.BLOCKS.registerId(currentId,
					BlockTemplate::getNextId);
		}

		return i;
	}

	@Inject(method = "<init>(ILnet/minecraft/block/material/Material;)V", at = @At("RETURN"))
	private void registerItemInstance(int arg, Material par2, CallbackInfo ci) {
		if (ApronStAPICompat.isModLoaderTime()) {
			ModContents currentMod = ApronStAPICompat.getModContent();

			currentMod.BLOCKS.registerLate(currentId, this.id, (Block)(Object) this);

			currentId = -1;
		}
	}

	@Override
	public int getOriginalBlockId() {
		if (ApronStAPICompat.isModLoaderTime()) {
			ModContents currentMod = ApronStAPICompat.getModContent();

			return currentMod.BLOCKS.autoToOriginal.get(this.id);
		}

		return -1;
	}

	@Redirect(method = "beforeDestroyedByExplosion", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getDropId(ILjava/util/Random;)I"))
	private int fixBlockDrop(Block instance, int i, Random random) {
		int originalId = instance.getDropId(i, random);

		if (originalId > 0) {
			Optional<Identifier> dropBlockId = BlockRegistry.INSTANCE.getId(originalId);
			Optional<Identifier> dropItemId = ItemRegistry.INSTANCE.getId(originalId);

			if (!dropItemId.isEmpty()) return originalId;

			if (dropBlockId.isEmpty()) {
				throw new RuntimeException("Unknown block and item id for drop: " + originalId + " block id: " + instance.id);
			}

			Identifier dropId = dropBlockId.get();

			Item item = ItemRegistry.INSTANCE.get(dropId);

			if (item == null) {
				throw new RuntimeException("No item associated with identifier: " + dropId + " block id: " + instance.id);
			}

			return item.id;
		}

		return originalId;
	}
}
