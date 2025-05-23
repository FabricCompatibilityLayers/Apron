package io.github.betterthanupdates.apron.stapi.mixin;

import io.github.betterthanupdates.apron.LifecycleUtils;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.impl.item.ToolEffectivenessImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(ToolEffectivenessImpl.class)
public class ToolEffectivenessImplMixin {
	@Inject(method = "shouldApplyCustomLogic", cancellable = true, at = @At("HEAD"))
	private static void apron$disableCustomLogicForML(ItemStack item, BlockState state, CallbackInfoReturnable<Boolean> cir) {
		if (!(LifecycleUtils.MOD_LIST.contains(Objects.requireNonNull(ItemRegistry.INSTANCE.getId(item.getItem())).namespace.toString())
				|| LifecycleUtils.MOD_LIST.contains(Objects.requireNonNull(BlockRegistry.INSTANCE.getId(state.getBlock())).namespace.toString()))) {
			cir.setReturnValue(false);
		}
	}
}
