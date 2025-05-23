package io.github.betterthanupdates.apron.compat.mixin.client.eqex;

import forge.ITextureProvider;
import net.minecraft.BlockAggregator;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockAggregator.class)
public class BlockAggregatorMixin implements ITextureProvider {
	@Override
	public String getTextureFile() {
		return "/eqex/eqterrain.png";
	}
}
