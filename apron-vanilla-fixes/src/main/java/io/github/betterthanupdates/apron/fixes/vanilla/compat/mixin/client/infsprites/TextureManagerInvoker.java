package io.github.betterthanupdates.apron.fixes.vanilla.compat.mixin.client.infsprites;

import net.minecraft.client.texture.TextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(TextureManager.class)
public interface TextureManagerInvoker {
	@Invoker("method_1086")
	public int method_1086(int i, int j);
}
