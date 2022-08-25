package io.github.betterthanupdates.apron.mixin.client;

import static io.github.betterthanupdates.apron.Apron.*;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiElement;
import net.minecraft.client.gui.InGameHud;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public abstract class InGameHudMixin extends GuiElement {
	@Shadow
	private Minecraft client;

	@ModifyConstant(method = "render(FZII)V", constant = @Constant(stringValue = "Minecraft Beta 1.7.3 ("))
	private String apron$replaceVersionString(String constant) {
		return versionString(constant);
	}

	@Inject(method = "render(FZII)V", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/util/math/MathHelper;floor(D)I"))
	private void apron$addDebugLine(float bl, boolean i, int j, int par4, CallbackInfo ci) {
		int y = 96;

		if (FabricLoader.getInstance().isModLoaded("stationapi")) {
			y = 106;
		}

		this.drawTextWithShadow(this.client.textRenderer,
				String.format("M: %s, %s", rmlModsLoaded(), fabricModsLoaded()), 2, y, 0xE0_E0_E0);
	}
}
