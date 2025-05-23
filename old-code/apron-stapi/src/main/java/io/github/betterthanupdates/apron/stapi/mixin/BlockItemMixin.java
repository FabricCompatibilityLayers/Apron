package io.github.betterthanupdates.apron.stapi.mixin;

import io.github.betterthanupdates.apron.stapi.ApronStAPICompat;
import io.github.betterthanupdates.apron.stapi.ModContents;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.modificationstation.stationapi.api.item.StationFlatteningBlockItem;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public class BlockItemMixin extends Item {

	@Shadow
	private int itemId;

	public BlockItemMixin(int i) {
		super(i);
	}

	@ModifyVariable(method = "<init>", argsOnly = true, at= @At(value = "FIELD", target = "Lnet/minecraft/item/BlockItem;itemId:I"))
	private int fixBlockId(int i) {
		if (ApronStAPICompat.isModLoaderTime()) {
			ModContents modContents = ApronStAPICompat.getModContent();

			int blocId = i + 256;

			if (modContents.BLOCKS.originalToAuto.containsKey(blocId)) {
				i = modContents.BLOCKS.originalToAuto.get(blocId) - 256;
			} else {
				modContents.ITEMS.changeOriginalId(blocId, modContents.BLOCKS.autoToOriginal.get(blocId));
			}
		}

		return i;
	}

	@Inject(method = "getTranslationKey()Ljava/lang/String;", at = @At("HEAD"))
	private void debug(CallbackInfoReturnable<String> cir) {
		Identifier id = ItemRegistry.INSTANCE.getId(this);

		if (id != null) {
			Block block = BlockRegistry.INSTANCE.get(id);

			if (block != null) {
				if (this.itemId != block.id) {
					((StationFlatteningBlockItem) this).setBlock(block);
				}
			} else if (id.path.endsWith("_")) {
				Identifier fixId = id.namespace.id(id.path.replace("_", ""));

				block = BlockRegistry.INSTANCE.get(fixId);

				if (block != null) {
					if (this.itemId != block.id) {
						((StationFlatteningBlockItem) this).setBlock(block);
					}
				}
			}
		}
	}
}
