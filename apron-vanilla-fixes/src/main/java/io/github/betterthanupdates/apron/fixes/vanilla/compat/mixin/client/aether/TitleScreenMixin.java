package io.github.betterthanupdates.apron.fixes.vanilla.compat.mixin.client.aether;

import static io.github.betterthanupdates.apron.fixes.vanilla.AetherHelper.*;

import java.util.List;

import modloader.ModLoader;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
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
import net.minecraft.client.SingleplayerInteractionManager;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.menu.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.TextRenderer;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.world.storage.WorldMetadata;
import net.minecraft.world.storage.WorldStorage;

import io.github.betterthanupdates.apron.mixin.ButtonWidgetAccessor;
import io.github.betterthanupdates.apron.mixin.EntityAccessor;
import io.github.betterthanupdates.apron.mixin.SoundHelperAccessor;
import io.github.betterthanupdates.apron.mixin.WorldAccessor;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {

	@Shadow
	private ButtonWidget multiplayerButton;
	private WorldMetadata latestSave;
	private String hoverText;

	@Inject(method = "tick", at = @At("TAIL"))
	public void tick(CallbackInfo ci) {
		final AbstractClientPlayerEntity player = this.client.player;
		if (renderOption && player != null && !player.removed) {
			player.yaw += 0.2F;
			player.pitch = 0.0F;
			((EntityAccessor) player).setFallDistance(0.0F);
		}
	}

	@Inject(method = "initVanillaScreen", at = @At("TAIL"))
	public void initVanillaScreen(CallbackInfo ci) {
		final WorldStorage worldStorage = this.client.getWorldStorage();
		final List<WorldMetadata> saves = worldStorage.getMetadata();
		if (!saves.isEmpty()) {
			latestSave = saves.get(0);
		}

		mmactive = true;
		this.client.achievement = new GuiAchievementAether(this.client);
		this.client.overlay = new GuiIngameAether(this.client);

		if (musicId == -1 && !loadingWorld) {
			this.client.soundHelper.playSound("aether.music.menu", 1.0F, 1.0F);

			musicId = ((SoundHelperAccessor) this.client.soundHelper).getSoundUID();
			((SoundHelperAccessor) this.client.soundHelper).setMusicCountdown(999999999);
		}

		if (loadingWorld) {
			loadingWorld = false;
		}

		showWorldPreview();
		addButtons();
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

	@ModifyArgs(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/menu/TitleScreen;blit(IIIIII)V", ordinal = 0))
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

	@ModifyArgs(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/menu/TitleScreen;blit(IIIIII)V", ordinal = 1))
	public void render$renderLogoPart2(Args args) {
		if (renderOption) {
			args.set(0, 170);
			args.set(1, 15);
		} else if (themeOption) {
			args.set(0, this.width / 2 - 274 / 2 + 185);
			args.set(1, 30);
		}
	}

	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/menu/TitleScreen;blit(IIIIII)V", ordinal = 1, shift = At.Shift.AFTER))
	public void render$hideSplashText(CallbackInfo ci) {
		if (!themeOption && renderOption) {
			GL11.glPopMatrix();
		}
	}

	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/menu/TitleScreen;drawTextWithShadowCentred(Lnet/minecraft/client/render/TextRenderer;Ljava/lang/String;III)V"))
	public void render$drawSplashText(TitleScreen instance, TextRenderer textRenderer, String s, int i, int j, int k) {
		if (!renderOption) {
			this.drawTextWithShadowCentred(textRenderer, s, i, j, k);
		}
	}

	@ModifyArgs(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/menu/TitleScreen;drawTextWithShadow(Lnet/minecraft/client/render/TextRenderer;Ljava/lang/String;III)V", ordinal = 0))
	public void render$modifyVersionText(Args args) {
		if (renderOption) {
			args.set(2, this.width - this.textRenderer.getTextWidth(args.get(1)) - 5);
			args.set(3, this.height - 20);
			args.set(4, 16777215);
		}
	}

	@ModifyArgs(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/menu/TitleScreen;drawTextWithShadow(Lnet/minecraft/client/render/TextRenderer;Ljava/lang/String;III)V", ordinal = 1))
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
			this.client.openScreen(new GuiSelectWorldAether(this, musicId));
		}

		if (button.id == 2) {
			this.client.openScreen(new GuiMultiplayerAether(this, musicId));
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
			this.client.openScreen(null);
			mmactive = false;

			SoundSystem sound = SoundHelperAccessor.getSoundSystem();
			sound.stop("sound_" + musicId);
			((SoundHelperAccessor) this.client.soundHelper).setMusicCountdown(6000);

			musicId = -1;
		}

		addButtons();
	}

	private void addButtons() {
		buttons.removeIf(o -> ((ButtonWidget) o).id < 8);

		final TranslationStorage var1 = TranslationStorage.getInstance();
		this.buttons.add(new ButtonWidget(5, this.width - 24, 4, 20, 20, var1.translate("W")));
		this.buttons.add(new ButtonWidget(6, this.width - 48, 4, 20, 20, var1.translate("T")));

		if (renderOption) {
			final int var5 = this.height / 4 + 20;
			if (themeOption) {
				this.buttons.add(new GuiAetherButton(0, this.width / 4 - 100, var5 + 72, var1.translate("menu.options")));
				this.buttons.add(new GuiAetherButton(1, this.width / 4 - 100, var5, var1.translate("menu.singleplayer")));
				this.buttons.add(this.multiplayerButton = new GuiAetherButton(2, this.width / 4 - 100, var5 + 24, var1.translate("menu.multiplayer")));
				this.buttons.add(new GuiAetherButton(3, this.width / 4 - 100, var5 + 48, var1.translate("menu.mods")));
				this.buttons.add(new GuiAetherButton(4, this.width / 4 - 100, var5 + 96, var1.translate("menu.quit")));
			} else {
				this.buttons.add(new ButtonWidget(0, this.width / 4 - 100, var5 + 72, var1.translate("menu.options")));
				this.buttons.add(new ButtonWidget(1, this.width / 4 - 100, var5, var1.translate("menu.singleplayer")));
				this.buttons.add(this.multiplayerButton = new ButtonWidget(2, this.width / 4 - 100, var5 + 24, var1.translate("menu.multiplayer")));
				this.buttons.add(new ButtonWidget(3, this.width / 4 - 100, var5 + 48, var1.translate("menu.mods")));
				this.buttons.add(new ButtonWidget(4, this.width / 4 - 100, var5 + 96, var1.translate("menu.quit")));
			}
			this.buttons.add(new ButtonWidget(7, this.width - 72, 4, 20, 20, var1.translate("Q")));
		} else {
			final int var5 = this.height / 4 + 40;
			if (themeOption) {
				this.buttons.add(new GuiAetherButton(0, this.width / 2 - 110, var5 + 72, 98, 20, var1.translate("menu.options")));
				this.buttons.add(new GuiAetherButton(1, this.width / 2 - 110, var5, var1.translate("menu.singleplayer")));
				this.buttons.add(this.multiplayerButton = new GuiAetherButton(2, this.width / 2 - 110, var5 + 24, var1.translate("menu.multiplayer")));
				this.buttons.add(new GuiAetherButton(3, this.width / 2 - 110, var5 + 48, var1.translate("menu.mods")));
				this.buttons.add(new GuiAetherButton(4, this.width / 2 + 2 - 10, var5 + 72, 98, 20, var1.translate("menu.quit")));
			} else {
				this.buttons.add(new ButtonWidget(0, this.width / 2 - 110, var5 + 72, 98, 20, var1.translate("menu.options")));
				this.buttons.add(new ButtonWidget(1, this.width / 2 - 110, var5, var1.translate("menu.singleplayer")));
				this.buttons.add(this.multiplayerButton = new ButtonWidget(2, this.width / 2 - 110, var5 + 24, var1.translate("menu.multiplayer")));
				this.buttons.add(new ButtonWidget(3, this.width / 2 - 110, var5 + 48, var1.translate("menu.mods")));
				this.buttons.add(new ButtonWidget(4, this.width / 2 + 2 - 10, var5 + 72, 98, 20, var1.translate("menu.quit")));
			}
		}
	}

	private void showWorldPreview() {
		this.client.interactionManager = new SingleplayerInteractionManager(this.client);
		ModLoader.getMinecraftInstance().options.hideHud = true;
		this.client.options.thirdPerson = true;
		this.client.createOrLoadWorld(latestSave.getFileName(), latestSave.getWorldName(), 0L);
		((WorldAccessor) this.client.world).setAutoSaveInterval(999999999);
	}

	private void hideWorldPreview() {
		this.client.world = null;
		this.client.player = null;
	}

	@Override
	public void renderBackground() {
		if (renderOption) return;

		if (themeOption) {
			this.renderAetherBackground();
		} else {
			this.renderDirtBackground(0);
		}
	}

	public void renderAetherBackground() {
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_FOG);
		Tessellator tessellator = Tessellator.INSTANCE;
		GL11.glBindTexture(3553, this.client.textureManager.getTextureId("/aether/gui/aetherBG.png"));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		float f = 32.0F;
		tessellator.start();
		tessellator.color(0x999999);
		tessellator.vertex(0.0, this.height, 0.0, 0.0, (float) this.height / f);
		tessellator.vertex(this.width, this.height, 0.0, (float) this.width / f, (float) this.height / f);
		tessellator.vertex(this.width, 0.0, 0.0, (float) this.width / f, 0.0);
		tessellator.vertex(0.0, 0.0, 0.0, 0.0, 0.0);
		tessellator.tessellate();
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	@Override
	public void onClose() {
		this.client.options.hideHud = false;
		this.client.options.thirdPerson = false;
	}
}
