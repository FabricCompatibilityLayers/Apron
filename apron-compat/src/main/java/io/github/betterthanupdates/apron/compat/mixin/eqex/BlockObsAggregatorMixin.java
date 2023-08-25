package io.github.betterthanupdates.apron.compat.mixin.eqex;

import forge.ITextureProvider;
import net.minecraft.BlockObsAggregator;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockObsAggregator.class)
public class BlockObsAggregatorMixin implements ITextureProvider {
	@Override
	public String getTextureFile() {
		return "/eqex/eqterrain.png";
	}
}
