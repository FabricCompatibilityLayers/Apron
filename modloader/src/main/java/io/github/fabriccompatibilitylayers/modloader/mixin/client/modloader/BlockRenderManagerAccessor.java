package io.github.fabriccompatibilitylayers.modloader.mixin.client.modloader;

import net.minecraft.client.render.block.BlockRenderManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Accessor;

@Pseudo
@Mixin(value = BlockRenderManager.class, priority = 1001)
public interface BlockRenderManagerAccessor {
	@Accessor(value = "cfgGrassFix", remap = false)
	static boolean getCfgGrassFix() {
		return true;
	}

	@Accessor(value = "cfgGrassFix", remap = false)
	static void setCfgGrassFix(boolean value) {
	}
}
