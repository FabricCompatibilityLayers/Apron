package io.github.betterthanupdates.apron.compat.mixin.eqex;

import forge.ITextureProvider;
import net.minecraft.BlockDarkMatterFurnace;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockDarkMatterFurnace.class)
public class BlockDarkMatterFurnaceMixin implements ITextureProvider {
	@Override
	public String getTextureFile() {
		return "/eqex/eqterrain.png";
	}
}
