package io.github.betterthanupdates.apron.stapi.mixin;

import io.github.betterthanupdates.apron.stapi.LoadingDoneListener;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.io.CompoundTag;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class ItemStackMixin {
	@Shadow
	public int itemId;

	@Inject(method = "<init>(III)V", at = @At("RETURN"))
	private void fixId(int j, int k, int par3, CallbackInfo ci) {
		if (ItemRegistry.INSTANCE.get(this.itemId) == null && this.itemId < Block.BY_ID.length && Block.BY_ID[this.itemId] != null) {
			this.apron$fixId(Block.BY_ID[this.itemId]);
		}
	}

	@Inject(method = "<init>(Lnet/minecraft/util/io/CompoundTag;)V", at = @At("RETURN"))
	private void fixId(CompoundTag par1, CallbackInfo ci) {
		if (ItemRegistry.INSTANCE.get(this.itemId) == null && this.itemId < Block.BY_ID.length && Block.BY_ID[this.itemId] != null) {
			this.apron$fixId(Block.BY_ID[this.itemId]);
		}
	}

	@Inject(method = "writeNBT", at = @At("HEAD"))
	private void fixNbtId(CompoundTag par1, CallbackInfoReturnable<CompoundTag> cir) {
		if (ItemRegistry.INSTANCE.get(this.itemId) == null) {
			if (this.itemId < Block.BY_ID.length && Block.BY_ID[this.itemId] != null) this.apron$fixId(Block.BY_ID[this.itemId]);
		}
	}

	@Unique
	private void apron$fixId(Block i) {
		Identifier oldIdentifier = BlockRegistry.INSTANCE.getId(i);

		if (oldIdentifier == null) {
			LoadingDoneListener.LATE_UPDATES.add(() -> {
				Identifier identifier = BlockRegistry.INSTANCE.getId(i);

				if (identifier != null) {
					Item item = ItemRegistry.INSTANCE.get(identifier);

					if (item != null) {
						this.itemId = item.id;
					}
				}
			});
		} else {
			Item item = ItemRegistry.INSTANCE.get(oldIdentifier);

			if (item != null) {
				this.itemId = item.id;
			}
		}
	}
}
