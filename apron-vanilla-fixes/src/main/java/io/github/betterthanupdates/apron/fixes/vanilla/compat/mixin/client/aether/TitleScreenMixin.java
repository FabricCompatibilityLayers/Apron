package io.github.betterthanupdates.apron.fixes.vanilla.compat.mixin.client.aether;

import static io.github.betterthanupdates.apron.fixes.vanilla.AetherHelper.*;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import modloader.ModLoader;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import paulscode.sound.SoundSystem;

import net.minecraft.GuiAchievementAether;
import net.minecraft.GuiAetherButton;
import net.minecraft.GuiIngameAether;
import net.minecraft.GuiMultiplayerAether;
import net.minecraft.GuiSelectWorldAether;
import net.minecraft.client.Minecraft;
import net.minecraft.client.SingleplayerInteractionManager;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.menu.OptionsScreen;
import net.minecraft.client.gui.screen.menu.TexturePacksScreen;
import net.minecraft.client.gui.screen.menu.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.storage.WorldMetadata;
import net.minecraft.world.storage.WorldStorage;

import io.github.betterthanupdates.apron.mixin.ButtonWidgetAccessor;
import io.github.betterthanupdates.apron.mixin.EntityAccessor;
import io.github.betterthanupdates.apron.mixin.SoundHelperAccessor;
import io.github.betterthanupdates.apron.mixin.WorldAccessor;

@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {
	@Shadow
	private String splashMessage;
	@Shadow
	private ButtonWidget multiplayerButton;
	@Shadow
	private float ticksOpened;
	private List saveList;
	private int selectedWorld;
	public int renderSplit = 4;
	public int closeTicks;
	private String hoverText = "";

	public boolean OnTickInGame(Minecraft game) {
		++this.closeTicks;
		return true;
	}

	/**
	 * @author Aether team
	 * @reason add aether stuff
	 */
	@Overwrite
	public void tick() {
		++this.ticksOpened;

		if (renderOption && ModLoader.getMinecraftInstance().player != null && !ModLoader.getMinecraftInstance().player.removed) {
			AbstractClientPlayerEntity var10000 = ModLoader.getMinecraftInstance().player;
			var10000.yaw += 0.2F;
			ModLoader.getMinecraftInstance().player.pitch = 0.0F;
			((EntityAccessor) ModLoader.getMinecraftInstance().player).setFallDistance(0.0F);
		}
	}

	/**
	 * @author Aether team
	 * @reason add aether stuff
	 */
	@Overwrite
	public void initVanillaScreen() {
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

		ModLoader.getMinecraftInstance().options.hideHud = true;
		ModLoader.getMinecraftInstance().options.thirdPerson = true;
		TranslationStorage var1 = TranslationStorage.getInstance();

		if (renderOption) {
			this.client.interactionManager = new SingleplayerInteractionManager(this.client);

			if (this.client.world == null) {
				this.loadSaves();
				String var2 = this.getSaveFileName(0);
				String var3 = this.getSaveName(0);

				if (var3 != null && var2 != null) {
					this.client.createOrLoadWorld(var2, var3, 0L);
					((WorldAccessor) this.client.world).setAutoSaveInterval(999999999);
				} else {
					renderOption = false;
				}
			}
		} else if (themeOption) {
			this.drawAetherDefaultBackground();
		} else {
			this.renderBackground();
		}

		Calendar var4 = Calendar.getInstance();
		var4.setTime(new Date());

		if (var4.get(2) + 1 == 11 && var4.get(5) == 9) {
			this.splashMessage = "Happy birthday, ez!";
		} else if (var4.get(2) + 1 == 6 && var4.get(5) == 1) {
			this.splashMessage = "Happy birthday, Notch!";
		} else if (var4.get(2) + 1 == 12 && var4.get(5) == 24) {
			this.splashMessage = "Merry X-mas!";
		} else if (var4.get(2) + 1 == 1 && var4.get(5) == 1) {
			this.splashMessage = "Happy new year!";
		} else if (var4.get(2) + 1 == 8 && var4.get(5) == 3) {
			this.splashMessage = "We miss you, Ripsand :(";
		}

		if (renderOption) {
			int var5 = this.height / 4 + 20;
			this.buttons.clear();
			this.buttons.add(new GuiAetherButton(1, this.width / 4 - 100, var5, var1.translate("menu.singleplayer")));
			this.buttons.add(this.multiplayerButton = new GuiAetherButton(2, this.width / 4 - 100, var5 + 24, var1.translate("menu.multiplayer")));
			this.buttons.add(new GuiAetherButton(3, this.width / 4 - 100, var5 + 48, var1.translate("menu.mods")));
			this.buttons.add(new ButtonWidget(5, this.width - 24, 4, 20, 20, var1.translate("W")));
			this.buttons.add(new ButtonWidget(6, this.width - 48, 4, 20, 20, var1.translate("T")));
			this.buttons.add(new ButtonWidget(7, this.width - 72, 4, 20, 20, var1.translate("Q")));
			this.buttons.add(new GuiAetherButton(0, this.width / 4 - 100, var5 + 72, var1.translate("menu.options")));
			this.buttons.add(new GuiAetherButton(4, this.width / 4 - 100, var5 + 96, var1.translate("menu.quit")));

			if (this.client.session == null) {
				this.multiplayerButton.active = false;
			}
		} else {
			int var5 = this.height / 4 + 40;
			int var6 = this.width / 2 + 100;
			this.buttons.clear();
			this.buttons.add(new GuiAetherButton(1, this.width / 2 - 110, var5, var1.translate("menu.singleplayer")));
			this.buttons.add(this.multiplayerButton = new GuiAetherButton(2, this.width / 2 - 110, var5 + 24, var1.translate("menu.multiplayer")));
			this.buttons.add(new GuiAetherButton(3, this.width / 2 - 110, var5 + 48, var1.translate("menu.mods")));
			this.buttons.add(new ButtonWidget(5, this.width - 24, 4, 20, 20, var1.translate("W")));
			this.buttons.add(new ButtonWidget(6, this.width - 48, 4, 20, 20, var1.translate("T")));
			this.buttons.add(new GuiAetherButton(0, this.width / 2 - 110, var5 + 72, 98, 20, var1.translate("menu.options")));
			this.buttons.add(new GuiAetherButton(4, this.width / 2 + 2 - 10, var5 + 72, 98, 20, var1.translate("menu.quit")));

			if (this.client.session == null) {
				this.multiplayerButton.active = false;
			}
		}
	}

	protected String getSaveName(int i) {
		if (this.saveList.size() < i + 1) {
			return null;
		} else {
			String s = ((WorldMetadata) this.saveList.get(i)).getWorldName();

			if (s == null || MathHelper.isStringEmpty(s)) {
				TranslationStorage stringtranslate = TranslationStorage.getInstance();
				s = stringtranslate.translate("selectWorld.world") + " " + (i + 1);
			}

			return s;
		}
	}

	private void loadSaves() {
		WorldStorage isaveformat = this.client.getWorldStorage();
		this.saveList = isaveformat.getMetadata();
		Collections.sort(this.saveList);
		this.selectedWorld = -1;
	}

	protected String getSaveFileName(int i) {
		return this.saveList.size() < i + 1 ? null : ((WorldMetadata) this.saveList.get(i)).getFileName();
	}

	/**
	 * @author Aether team
	 * @reason add aether stuff
	 */
	@Overwrite
	protected void buttonClicked(ButtonWidget var1) {
		if (var1.id == 0) {
			this.client.openScreen(new OptionsScreen(this, this.client.options));
		}

		if (var1.id == 1) {
			this.client.openScreen(new GuiSelectWorldAether(this, musicId));
		}

		if (var1.id == 2) {
			this.client.openScreen(new GuiMultiplayerAether(this, musicId));
		}

		if (var1.id == 3) {
			this.client.openScreen(new TexturePacksScreen(this));
		}

		if (var1.id == 4) {
			this.client.scheduleStop();
		}

		if (var1.id == 6) {
			themeOption = !themeOption;
		}

		if (var1.id == 7) {
			this.client.openScreen(null);
			mmactive = false;

			SoundSystem sound = SoundHelperAccessor.getSoundSystem();
			sound.stop("sound_" + musicId);
			((SoundHelperAccessor) this.client.soundHelper).setMusicCountdown(6000);

			musicId = -1;
		}

		if (var1.id == 5) {
			TranslationStorage var11 = TranslationStorage.getInstance();

			if (!renderOption) {
				renderOption = true;
				this.loadSaves();
				String var2 = this.getSaveFileName(0);
				String var3 = this.getSaveName(0);

				if (var3 == null) {
					renderOption = false;
					return;
				}

				this.client.createOrLoadWorld(var2, var3, 0L);
				int var5 = this.height / 4 + 20;
				this.buttons.clear();
				this.buttons.add(new GuiAetherButton(1, this.width / 4 - 100, var5, var11.translate("menu.singleplayer")));
				this.buttons.add(this.multiplayerButton = new GuiAetherButton(2, this.width / 4 - 100, var5 + 24, var11.translate("menu.multiplayer")));
				this.buttons.add(new GuiAetherButton(3, this.width / 4 - 100, var5 + 48, var11.translate("menu.mods")));
				this.buttons.add(new ButtonWidget(5, this.width - 24, 4, 20, 20, var11.translate("W")));
				this.buttons.add(new ButtonWidget(6, this.width - 48, 4, 20, 20, var11.translate("T")));
				this.buttons.add(new ButtonWidget(7, this.width - 72, 4, 20, 20, var11.translate("Q")));
				this.buttons.add(new GuiAetherButton(0, this.width / 4 - 100, var5 + 72, var11.translate("menu.options")));
				this.buttons.add(new GuiAetherButton(4, this.width / 4 - 100, var5 + 96, var11.translate("menu.quit")));

				if (this.client.session == null) {
					this.multiplayerButton.active = false;
				}
			} else {
				renderOption = false;
				this.client.world = null;
				this.client.player = null;
				int var5 = this.height / 4 + 40;
				this.buttons.clear();
				this.buttons.add(new GuiAetherButton(1, this.width / 2 - 110, var5, var11.translate("menu.singleplayer")));
				this.buttons.add(this.multiplayerButton = new GuiAetherButton(2, this.width / 2 - 110, var5 + 24, var11.translate("menu.multiplayer")));
				this.buttons.add(new GuiAetherButton(3, this.width / 2 - 110, var5 + 48, var11.translate("menu.mods")));
				this.buttons.add(new ButtonWidget(5, this.width - 24, 4, 20, 20, var11.translate("W")));
				this.buttons.add(new ButtonWidget(6, this.width - 48, 4, 20, 20, var11.translate("T")));
				this.buttons.add(new GuiAetherButton(0, this.width / 2 - 110, var5 + 72, 98, 20, var11.translate("menu.options")));
				this.buttons.add(new GuiAetherButton(4, this.width / 2 + 2 - 10, var5 + 72, 98, 20, var11.translate("menu.quit")));

				if (this.client.session == null) {
					this.multiplayerButton.active = false;
				}
			}
		}
	}

	/**
	 * @author Aether team
	 * @reason add aether stuff
	 */
	@Overwrite
	public void render(int var1, int var2, float var3) {
		if (themeOption) {
			if (renderOption) {
				Tessellator var4 = Tessellator.INSTANCE;
				boolean var5 = true;
				byte var6 = 15;
				byte var7 = 15;
				GL11.glBindTexture(3553, this.client.textureManager.getTextureId("/aether/title/mclogomod1.png"));
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				this.blit(var6 + 0, var7 + 0, 0, 0, 155, 44);
				this.blit(var6 + 155, var7 + 0, 0, 45, 155, 44);
				var4.color(16777215);
				String var8 = "Minecraft Beta 1.7.3";
				this.drawTextWithShadow(this.textRenderer, var8, this.width - this.textRenderer.getTextWidth(var8) - 5, this.height - 20, 16777215);
				String var9 = "Copyright Mojang AB. Do not distribute.";
				this.drawTextWithShadow(this.textRenderer, var9, this.width - this.textRenderer.getTextWidth(var9) - 5, this.height - 10, 5263440);
			} else {
				this.drawAetherDefaultBackground();
				Tessellator var4 = Tessellator.INSTANCE;
				short var5 = 274;
				int var6 = this.width / 2 - var5 / 2;
				byte var7 = 30;
				GL11.glBindTexture(3553, this.client.textureManager.getTextureId("/aether/title/mclogomod1.png"));
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				this.blit(var6 + 30, var7 + 0, 0, 0, 155, 44);
				this.blit(var6 + 185, var7 + 0, 0, 45, 155, 44);
				var4.color(16777215);
				GL11.glPushMatrix();
				GL11.glTranslatef((float) (this.width / 2 + 90), 70.0F, 0.0F);
				GL11.glRotatef(-20.0F, 0.0F, 0.0F, 1.0F);
				float var8 = 1.8F - MathHelper.abs(MathHelper.sin((float) (System.currentTimeMillis() % 1000L) / 1000.0F * (float) Math.PI * 2.0F) * 0.1F);
				var8 = var8 * 100.0F / (float) (this.textRenderer.getTextWidth(this.splashMessage) + 32);
				GL11.glScalef(var8, var8, var8);
				this.drawTextWithShadowCentred(this.textRenderer, this.splashMessage, 0, -8, 16776960);
				GL11.glPopMatrix();
				this.drawTextWithShadow(this.textRenderer, "Minecraft Beta 1.7.3", 2, 2, 5263440);
				String var9 = "Copyright Mojang AB. Do not distribute.";
				this.drawTextWithShadow(this.textRenderer, var9, this.width - this.textRenderer.getTextWidth(var9) - 2, this.height - 10, 16777215);
			}
		} else if (renderOption) {
			Tessellator var4 = Tessellator.INSTANCE;
			boolean var5 = true;
			String var8 = "Minecraft Beta 1.7.3";
			this.drawTextWithShadow(this.textRenderer, var8, this.width - this.textRenderer.getTextWidth(var8) - 5, this.height - 20, 16777215);
			String var9 = "Copyright Mojang AB. Do not distribute.";
			this.drawTextWithShadow(this.textRenderer, var9, this.width - this.textRenderer.getTextWidth(var9) - 5, this.height - 10, 5263440);
			this.drawMiniLogo();
		} else {
			this.renderBackground();
			Tessellator var4 = Tessellator.INSTANCE;
			short var5 = 274;
			int var6 = this.width / 2 - var5 / 2;
			byte var7 = 30;
			GL11.glBindTexture(3553, this.client.textureManager.getTextureId("/title/mclogo.png"));
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.blit(var6 + 0, var7 + 0, 0, 0, 155, 44);
			this.blit(var6 + 155, var7 + 0, 0, 45, 155, 44);
			var4.color(16777215);
			GL11.glPushMatrix();
			GL11.glTranslatef((float) (this.width / 2 + 90), 70.0F, 0.0F);
			GL11.glRotatef(-20.0F, 0.0F, 0.0F, 1.0F);
			float var8 = 1.8F - MathHelper.abs(MathHelper.sin((float) (System.currentTimeMillis() % 1000L) / 1000.0F * (float) Math.PI * 2.0F) * 0.1F);
			var8 = var8 * 100.0F / (float) (this.textRenderer.getTextWidth(this.splashMessage) + 32);
			GL11.glScalef(var8, var8, var8);
			this.drawTextWithShadowCentred(this.textRenderer, this.splashMessage, 0, -8, 16776960);
			GL11.glPopMatrix();
			this.drawTextWithShadow(this.textRenderer, "Minecraft Beta 1.7.3", 2, 2, 5263440);
			String var9 = "Copyright Mojang AB. Do not distribute.";
			this.drawTextWithShadow(this.textRenderer, var9, this.width - this.textRenderer.getTextWidth(var9) - 2, this.height - 10, 16777215);
		}

		this.drawTextWithShadow(this.textRenderer, this.hoverText, this.width - 72, 28, 16777215);
		super.render(var1, var2, var3);
	}

	@Override
	protected void mouseReleased(int i, int j, int k) {
		this.hoverText = "";

		for (Object o : this.buttons) {
			ButtonWidget button = (ButtonWidget) o;

			if (i >= button.x && j >= button.y && i < button.x + ((ButtonWidgetAccessor) button).getWidth() && j < button.y + ((ButtonWidgetAccessor) button).getHeight()) {
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
	}

	public void drawMiniLogo() {
		Tessellator var4 = Tessellator.INSTANCE;
		byte var6 = 15;
		byte var7 = 15;
		GL11.glBindTexture(3553, this.client.textureManager.getTextureId("/title/mclogo.png"));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPushMatrix();
		GL11.glScalef(0.8F, 0.8F, 0.8F);
		this.blit(var6 + 0, var7 + 0, 0, 0, 155, 44);
		this.blit(var6 + 155, var7 + 0, 0, 45, 155, 44);
		GL11.glPopMatrix();
		var4.color(16777215);
	}

	public void drawAetherDefaultBackground() {
		this.drawAetherWorldBackground(0);
	}

	public void drawAetherWorldBackground(int i) {
		this.drawAetherBackground(i);
	}

	public void drawAetherBackground(int i) {
		GL11.glDisable(2896);
		GL11.glDisable(2912);
		Tessellator tessellator = Tessellator.INSTANCE;
		GL11.glBindTexture(3553, this.client.textureManager.getTextureId("/aether/gui/aetherBG.png"));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		float f = 32.0F;
		tessellator.start();
		tessellator.color(10066329);
		tessellator.vertex(0.0, (double) this.height, 0.0, 0.0, (double) ((float) this.height / f + (float) i));
		tessellator.vertex((double) this.width, (double) this.height, 0.0, (double) ((float) this.width / f), (double) ((float) this.height / f + (float) i));
		tessellator.vertex((double) this.width, 0.0, 0.0, (double) ((float) this.width / f), (double) (0 + i));
		tessellator.vertex(0.0, 0.0, 0.0, 0.0, (double) (0 + i));
		tessellator.tessellate();
	}

	@Override
	public boolean isPauseScreen() {
		return this.closeTicks >= 30;
	}

	@Override
	public void onClose() {
		this.client.options.hideHud = false;
		this.client.options.thirdPerson = false;
	}
}
