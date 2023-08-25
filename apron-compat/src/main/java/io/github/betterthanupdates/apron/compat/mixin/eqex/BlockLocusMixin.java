package io.github.betterthanupdates.apron.compat.mixin.eqex;

import forge.ITextureProvider;
import net.minecraft.BlockLocus;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockLocus.class)
public class BlockLocusMixin implements ITextureProvider {
	@Override
	public String getTextureFile() {
		return "/eqex/eqterrain.png";
	}
}
