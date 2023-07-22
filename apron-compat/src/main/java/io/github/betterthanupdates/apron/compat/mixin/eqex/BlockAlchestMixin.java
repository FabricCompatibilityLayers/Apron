package io.github.betterthanupdates.apron.compat.mixin.eqex;

import forge.ITextureProvider;
import net.minecraft.BlockAlchest;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockAlchest.class)
public class BlockAlchestMixin implements ITextureProvider {
	@Override
	public String getTextureFile() {
		return "/eqex/eqterrain.png";
	}
}
