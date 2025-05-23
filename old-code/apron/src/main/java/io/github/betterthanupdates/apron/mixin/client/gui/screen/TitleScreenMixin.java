package io.github.betterthanupdates.apron.mixin.client.gui.screen;

//import static io.github.betterthanupdates.apron.Apron.fabricModsLoaded;
//import static io.github.betterthanupdates.apron.Apron.rmlModsLoaded;
//
//import java.util.Objects;
//
//import modloader.ModLoader;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
//import net.fabricmc.loader.api.FabricLoader;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Constant;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.ModifyConstant;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;

//import io.github.betterthanupdates.apron.Apron;

@Environment(EnvType.CLIENT)
@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {
//	@ModifyConstant(method = "render", constant = @Constant(stringValue = "Minecraft Beta 1.7.3"))
//	private String apron$renderVersion(final String constant) {
//		return Apron.versionString(constant);
//	}
//
//	@Inject(method = "render(IIF)V", at = @At(value = "INVOKE",
//			target = "Lorg/lwjgl/opengl/GL11;glPopMatrix()V", shift = At.Shift.AFTER, remap = false))
//	private void apron$renderMods(int a, int b, float c, CallbackInfo ci) {
//		int yOffset = FabricLoader.getInstance().isModLoaded("mojangfix") ? 22 : 12;
//
//		this.drawTextWithShadow(this.textRenderer,
//				String.format("Mods: %s, %s", rmlModsLoaded(), fabricModsLoaded()), 2, yOffset, 0x50_50_50);
//	}
//
//	@Inject(method = "initVanillaScreen", at = @At("HEAD"))
//	private void apron$initStart(CallbackInfo ci) {
//		Apron.fabricModCount = FabricLoader.getInstance().getAllMods().stream()
//				.filter((modContainer -> !Objects.equals(modContainer.getMetadata().getId(), "minecraft"))).count();
//		Apron.rmlModCount = ModLoader.getLoadedMods().size();
//	}
}
