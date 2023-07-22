package io.github.betterthanupdates.apron.compat.mixin.eqex;

import forge.ITextureProvider;
import net.minecraft.BlockDarkMatter;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockDarkMatter.class)
public class BlockDarkMatterMixin implements ITextureProvider {
	@Override
	public String getTextureFile() {
		return "/eqex/eqterrain.png";
	}
}
