package io.github.betterthanupdates.apron.stapi.mixin;

import io.github.betterthanupdates.apron.stapi.ApronStAPICompat;
import io.github.betterthanupdates.apron.stapi.ModContents;
import net.minecraft.item.Item;
import net.modificationstation.stationapi.api.template.item.ItemTemplate;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(Item.class)
public class ItemMixin {
	@Shadow
	@Final
	public int id;

	@Unique
	private static int currentId = -1;

	@ModifyVariable(method = "<init>", argsOnly = true, at = @At(value = "HEAD"))
	private static int modifyItemId(int i) {
		if (ApronStAPICompat.isModLoaderTime()) {
			ModContents currentMod = ApronStAPICompat.getModContent();
			currentId = 256 + i;

			return currentMod.ITEMS.registerId(currentId,
					() -> (256 + ItemTemplate.getNextId())) - 256;
		}

		return i;
	}

	@Inject(method = "<init>", at = @At("RETURN"))
	private void registerItemInstance(int par1, CallbackInfo ci) {
		if (ApronStAPICompat.isModLoaderTime()) {
			ModContents currentMod = ApronStAPICompat.getModContent();

			currentMod.ITEMS.registerLate(currentId, this.id, (Item)(Object) this);

			currentId = -1;
		}
	}
}
