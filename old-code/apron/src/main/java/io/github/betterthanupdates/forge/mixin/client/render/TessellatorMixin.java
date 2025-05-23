package io.github.betterthanupdates.forge.mixin.client.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.render.Tessellator;

import io.github.betterthanupdates.forge.ForgeClientReflection;
import io.github.betterthanupdates.forge.client.render.ForgeTessellator;

@Environment(EnvType.CLIENT)
@Mixin(Tessellator.class)
public abstract class TessellatorMixin implements ForgeTessellator {
	@Shadow
	public static Tessellator INSTANCE;

	@Shadow
	public boolean drawing;

	// Forge Fields
	@Unique
	public boolean defaultTexture = false;

	/**
	 * @author Eloraam
	 * @reason method instruction order is different from vanilla.
	 */
	@Inject(method = "<clinit>", at = @At("RETURN"))
	private static void classCtr$overwrite(CallbackInfo ci) {
		ForgeClientReflection.Tessellator$firstInstance = INSTANCE;
		((ForgeTessellator) ForgeClientReflection.Tessellator$firstInstance).defaultTexture(true);
	}

	@Override
	public boolean defaultTexture() {
		return this.defaultTexture;
	}

	@Override
	public void defaultTexture(boolean defaultTexture) {
		this.defaultTexture = defaultTexture;
	}

	@Override
	public boolean isTessellating() {
		return drawing;
	}
}
