package io.github.betterthanupdates.apron.stapi.mixin;

import io.github.betterthanupdates.apron.stapi.LoadingDoneListener;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.util.Identifier;
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
		if (ItemRegistry.INSTANCE.get(this.itemId) == null && this.itemId < Block.BLOCKS.length && Block.BLOCKS[this.itemId] != null) {
			this.apron$fixId(Block.BLOCKS[this.itemId]);
		}
	}

	@Inject(method = "<init>(Lnet/minecraft/nbt/NbtCompound;)V", at = @At("RETURN"))
	private void fixId(NbtCompound par1, CallbackInfo ci) {
		if (ItemRegistry.INSTANCE.get(this.itemId) == null && this.itemId < Block.BLOCKS.length && Block.BLOCKS[this.itemId] != null) {
			this.apron$fixId(Block.BLOCKS[this.itemId]);
		}
	}

	@Inject(method = "writeNbt", at = @At("HEAD"))
	private void fixNbtId(NbtCompound par1, CallbackInfoReturnable<NbtCompound> cir) {
		if (ItemRegistry.INSTANCE.get(this.itemId) == null) {
			if (this.itemId < Block.BLOCKS.length && Block.BLOCKS[this.itemId] != null) this.apron$fixId(Block.BLOCKS[this.itemId]);
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
