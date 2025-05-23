package io.github.betterthanupdates.apron.compat.mixin.client.aether;

import java.util.List;

import fr.catcore.cursedmixinextensions.annotations.Public;
import net.minecraft.class_182;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import paulscode.sound.SoundSystem;

import net.minecraft.GuiAchievementAether;
import net.minecraft.GuiAetherButton;
import net.minecraft.GuiIngameAether;
import net.minecraft.GuiMultiplayerAether;
import net.minecraft.GuiSelectWorldAether;
import net.minecraft.SingleplayerInteractionManager;
import net.minecraft.class_591;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.toast.AchievementToast;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.entity.player.ClientPlayerEntity;
import net.minecraft.mod_Aether;

import io.github.betterthanupdates.apron.ReflectionUtils;
import io.github.betterthanupdates.apron.mixin.client.gui.widget.ButtonWidgetAccessor;
import io.github.betterthanupdates.apron.mixin.entity.EntityAccessor;
import io.github.betterthanupdates.apron.mixin.client.sound.SoundManagerAccessor;
import io.github.betterthanupdates.apron.mixin.world.WorldAccessor;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {

	@Shadow
	private ButtonWidget multiplayerButton;

	@Unique
	private class_591 latestSave;
	@Unique
	private String hoverText;

	@Public
	@Unique
	private static boolean mmactive = false;
	@Public
	@Unique
	private static boolean renderOption = mod_Aether.worldMenu;
	@Public
	@Unique
	private static boolean themeOption = mod_Aether.aetherMenu;
	@Public
	@Unique
	private static int musicId = -1;
	@Public
	@Unique
	private static boolean loadingWorld = false;
	@Public
	@Unique
	private static AchievementToast ach = null;

	@Inject(method = "tick", at = @At("TAIL"))
	public void tick(CallbackInfo ci) {
		final ClientPlayerEntity player = this.minecraft.player;
		if (renderOption && player != null && !player.dead) {
			player.yaw += 0.2F;
			player.pitch = 0.0F;
			((EntityAccessor) player).setField_1636(0.0F);
		}
	}

	@Inject(method = "init", at = @At("TAIL"))
	public void initVanillaScreen(CallbackInfo ci) {
		final class_182 worldStorage = this.minecraft.method_2127();
		final List<class_591> saves = worldStorage.method_1002();
		if (!saves.isEmpty()) {
			latestSave = saves.get(0);
		}

		mmactive = true;
		this.minecraft.field_2819 = new GuiAchievementAether(this.minecraft);

		if (!ReflectionUtils.isModLoaded("mod_InfSprites")) {
			this.setOverlay();
		}

		if (musicId == -1 && !loadingWorld) {
			this.minecraft.soundManager.method_2009("aether.music.menu", 1.0F, 1.0F);

			musicId = ((SoundManagerAccessor) this.minecraft.soundManager).getField_2671();
			((SoundManagerAccessor) this.minecraft.soundManager).setField_2675(999999999);
		}

		if (loadingWorld) {
			loadingWorld = false;
		}

		showWorldPreview();
		addButtons();
	}

	private void setOverlay() {
		this.minecraft.inGameHud = new GuiIngameAether(this.minecraft);
	}

	@Inject(method = "render", at = @At("RETURN"))
	public void render(CallbackInfo ci) {
		// Hover Text
		this.drawTextWithShadow(this.textRenderer, this.hoverText, this.width - 72, 28, 0xffffff);
	}

	@ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/texture/TextureManager;getTextureId(Ljava/lang/String;)I"))
	public String render$modifyLogoTexture(String s) {
		if (themeOption) {
			return "/aether/title/mclogomod1.png";
		} else {
			return s;
		}
	}

	@ModifyArgs(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/TitleScreen;drawTexture(IIIIII)V", ordinal = 0))
	public void render$renderLogoPart1(Args args) {
		if (!themeOption && renderOption) {
			GL11.glPushMatrix();
			GL11.glScalef(0.8F, 0.8F, 0.8F);
		}

		if (renderOption) {
			args.set(0, 15);
			args.set(1, 15);
		} else if (themeOption) {
			args.set(0, this.width / 2 - 274 / 2 + 30);
			args.set(1, 30);
		}
	}

	@ModifyArgs(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/TitleScreen;drawTexture(IIIIII)V", ordinal = 1))
	public void render$renderLogoPart2(Args args) {
		if (renderOption) {
			args.set(0, 170);
			args.set(1, 15);
		} else if (themeOption) {
			args.set(0, this.width / 2 - 274 / 2 + 185);
			args.set(1, 30);
		}
	}

	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/TitleScreen;drawTexture(IIIIII)V", ordinal = 1, shift = At.Shift.AFTER))
	public void render$hideSplashText(CallbackInfo ci) {
		if (!themeOption && renderOption) {
			GL11.glPopMatrix();
		}
	}

	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/TitleScreen;drawCenteredTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V"))
	public void render$drawSplashText(TitleScreen instance, TextRenderer textRenderer, String s, int i, int j, int k) {
		if (!renderOption) {
			this.drawCenteredTextWithShadow(textRenderer, s, i, j, k);
		}
	}

	@ModifyArgs(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/TitleScreen;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V", ordinal = 0))
	public void render$modifyVersionText(Args args) {
		if (renderOption) {
			args.set(2, this.width - this.textRenderer.getWidth(args.get(1)) - 5);
			args.set(3, this.height - 20);
			args.set(4, 16777215);
		}
	}

	@ModifyArgs(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/TitleScreen;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V", ordinal = 1))
	public void render$modifyCopyrightText(Args args) {
		if (renderOption) {
			args.set(2, ((int) args.get(2)) - 3);
			args.set(4, 5263440);
		}
	}

	@Override
	protected void mouseReleased(int x, int y, int k) {
		for (Object o : this.buttons) {
			final ButtonWidget button = (ButtonWidget) o;

			if (x < button.x || y < button.y || x >= button.x + ((ButtonWidgetAccessor) button).getWidth() || y >= button.y + ((ButtonWidgetAccessor) button).getHeight()) {
				continue;
			}

			switch (button.id) {
				case 5:
					this.hoverText = "Toggle World";
					break;
				case 6:
					if (themeOption) {
						this.hoverText = "Normal Theme";
					} else {
						this.hoverText = "Aether Theme";
					}

					break;
				case 7:
					this.hoverText = "Quick Load";
			}
		}
	}

	@Inject(method = "buttonClicked", at = @At("HEAD"))
	public void buttonClicked(ButtonWidget button, CallbackInfo ci) {
		if (button.id == 1) {
			this.minecraft.setScreen(new GuiSelectWorldAether(this, musicId));
		}

		if (button.id == 2) {
			this.minecraft.setScreen(new GuiMultiplayerAether(this, musicId));
		}

		if (button.id == 5) {
			renderOption = !renderOption;
			if (renderOption) {
				showWorldPreview();
			} else {
				hideWorldPreview();
			}
		}

		if (button.id == 6) {
			themeOption = !themeOption;
		}

		if (button.id == 7) {
			this.minecraft.setScreen(null);
			mmactive = false;

			SoundSystem sound = SoundManagerAccessor.getSoundSystem();
			sound.stop("sound_" + musicId);
			((SoundManagerAccessor) this.minecraft.soundManager).setField_2675(6000);

			musicId = -1;
		}

		addButtons();
	}

	private void addButtons() {
		buttons.removeIf(o -> ((ButtonWidget) o).id < 8);

		final TranslationStorage var1 = TranslationStorage.getInstance();
		this.buttons.add(new ButtonWidget(5, this.width - 24, 4, 20, 20, var1.get("W")));
		this.buttons.add(new ButtonWidget(6, this.width - 48, 4, 20, 20, var1.get("T")));

		if (renderOption) {
			final int var5 = this.height / 4 + 20;
			if (themeOption) {
				this.buttons.add(new GuiAetherButton(0, this.width / 4 - 100, var5 + 72, var1.get("menu.options")));
				this.buttons.add(new GuiAetherButton(1, this.width / 4 - 100, var5, var1.get("menu.singleplayer")));
				this.buttons.add(this.multiplayerButton = new GuiAetherButton(2, this.width / 4 - 100, var5 + 24, var1.get("menu.multiplayer")));
				this.buttons.add(new GuiAetherButton(3, this.width / 4 - 100, var5 + 48, var1.get("menu.mods")));
				this.buttons.add(new GuiAetherButton(4, this.width / 4 - 100, var5 + 96, var1.get("menu.quit")));
			} else {
				this.buttons.add(new ButtonWidget(0, this.width / 4 - 100, var5 + 72, var1.get("menu.options")));
				this.buttons.add(new ButtonWidget(1, this.width / 4 - 100, var5, var1.get("menu.singleplayer")));
				this.buttons.add(this.multiplayerButton = new ButtonWidget(2, this.width / 4 - 100, var5 + 24, var1.get("menu.multiplayer")));
				this.buttons.add(new ButtonWidget(3, this.width / 4 - 100, var5 + 48, var1.get("menu.mods")));
				this.buttons.add(new ButtonWidget(4, this.width / 4 - 100, var5 + 96, var1.get("menu.quit")));
			}
			this.buttons.add(new ButtonWidget(7, this.width - 72, 4, 20, 20, var1.get("Q")));
		} else {
			final int var5 = this.height / 4 + 40;
			if (themeOption) {
				this.buttons.add(new GuiAetherButton(0, this.width / 2 - 110, var5 + 72, 98, 20, var1.get("menu.options")));
				this.buttons.add(new GuiAetherButton(1, this.width / 2 - 110, var5, var1.get("menu.singleplayer")));
				this.buttons.add(this.multiplayerButton = new GuiAetherButton(2, this.width / 2 - 110, var5 + 24, var1.get("menu.multiplayer")));
				this.buttons.add(new GuiAetherButton(3, this.width / 2 - 110, var5 + 48, var1.get("menu.mods")));
				this.buttons.add(new GuiAetherButton(4, this.width / 2 + 2 - 10, var5 + 72, 98, 20, var1.get("menu.quit")));
			} else {
				this.buttons.add(new ButtonWidget(0, this.width / 2 - 110, var5 + 72, 98, 20, var1.get("menu.options")));
				this.buttons.add(new ButtonWidget(1, this.width / 2 - 110, var5, var1.get("menu.singleplayer")));
				this.buttons.add(this.multiplayerButton = new ButtonWidget(2, this.width / 2 - 110, var5 + 24, var1.get("menu.multiplayer")));
				this.buttons.add(new ButtonWidget(3, this.width / 2 - 110, var5 + 48, var1.get("menu.mods")));
				this.buttons.add(new ButtonWidget(4, this.width / 2 + 2 - 10, var5 + 72, 98, 20, var1.get("menu.quit")));
			}
		}
	}

	private void showWorldPreview() {
		if (this.latestSave != null) {
			this.minecraft.interactionManager = new SingleplayerInteractionManager(this.minecraft);
			this.minecraft.options.hideHud = true;
			this.minecraft.options.thirdPerson = true;
			this.minecraft.method_2120(latestSave.method_1956(), latestSave.method_1958(), 0L);
			((WorldAccessor) this.minecraft.world).setField_212(999999999);
		}
	}

	private void hideWorldPreview() {
		this.minecraft.world = null;
		this.minecraft.player = null;
	}

	@Override
	public void renderBackground() {
		if (renderOption && this.latestSave != null) return;

		if (themeOption) {
			this.renderAetherBackground();
		} else {
			this.renderBackgroundTexture(0);
		}
	}

	public void renderAetherBackground() {
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_FOG);
		Tessellator tessellator = Tessellator.INSTANCE;
		GL11.glBindTexture(3553, this.minecraft.textureManager.getTextureId("/aether/gui/aetherBG.png"));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		float f = 32.0F;
		tessellator.startQuads();
		tessellator.color(0x999999);
		tessellator.vertex(0.0, this.height, 0.0, 0.0, (float) this.height / f);
		tessellator.vertex(this.width, this.height, 0.0, (float) this.width / f, (float) this.height / f);
		tessellator.vertex(this.width, 0.0, 0.0, (float) this.width / f, 0.0);
		tessellator.vertex(0.0, 0.0, 0.0, 0.0, 0.0);
		tessellator.draw();
	}

	@Override
	public boolean shouldPause() {
		return false;
	}

	@Override
	public void removed() {
		this.minecraft.options.hideHud = false;
		this.minecraft.options.thirdPerson = false;
	}
}
