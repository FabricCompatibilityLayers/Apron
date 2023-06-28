package io.github.betterthanupdates.shockahpi.mixin.client;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

import fr.catcore.modremapperapi.api.mixin.Public;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import shockahpi.SAPI;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.menu.AchievementsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widgets.OptionButtonWidget;
import net.minecraft.client.render.RenderHelper;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.entity.ItemRenderer;
import net.minecraft.client.resource.language.Internationalization;
import net.minecraft.stat.achievement.Achievement;
import net.minecraft.stat.achievement.Achievements;
import net.minecraft.util.io.StatsFileWriter;
import net.minecraft.util.math.MathHelper;

import io.github.betterthanupdates.shockahpi.client.gui.screen.ShockAhPIAchievementsScreen;

@Mixin(AchievementsScreen.class)
public class AchievementsScreenMixin extends Screen implements ShockAhPIAchievementsScreen {
	@Shadow
	private StatsFileWriter statsFileWriter;
	@Shadow
	protected int field_2618;
	@Shadow
	protected int field_2619;
	@Shadow
	@Final
	private static int field_2631;
	@Shadow
	@Final
	private static int field_2630;
	@Shadow
	@Final
	private static int field_2629;
	@Shadow
	@Final
	private static int field_2628;
	@Shadow
	protected double field_2622;
	@Shadow
	protected double field_2624;
	@Shadow
	protected double field_2623;
	@Shadow
	protected double field_2625;

	// SAPI Fields
	@Unique
	private boolean draw = true;

	@Inject(method = "initVanillaScreen", at = @At("RETURN"))
	private void sapi$init(CallbackInfo ci) {
		this.buttons.add(new OptionButtonWidget(11, this.width / 2 - 113, this.height / 2 + 74, 20, 20, "<"));
		this.buttons.add(new OptionButtonWidget(12, this.width / 2 - 93, this.height / 2 + 74, 20, 20, ">"));
	}

	@Inject(method = "buttonClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;buttonClicked(Lnet/minecraft/client/gui/widget/ButtonWidget;)V"))
	private void sapi$buttonClicked(ButtonWidget button, CallbackInfo ci) {
		if (button.id == 11) {
			SAPI.acPagePrev();
		} else if (button.id == 12) {
			SAPI.acPageNext();
		}
	}

	@Inject(method = "drawHeader", at = @At("RETURN"))
	private void sapi$drawHeader(CallbackInfo ci) {
		this.textRenderer.drawText(SAPI.acGetCurrentPageTitle(), this.width / 2 - 69, this.height / 2 + 80, 0);
	}

	/**
	 * @author SAPI
	 * @reason
	 */
	@Overwrite
	protected void method_1998(int i1, int j1, float f) {
		int k1 = MathHelper.floor(this.field_2622 + (this.field_2624 - this.field_2622) * (double)f);
		int l1 = MathHelper.floor(this.field_2623 + (this.field_2625 - this.field_2623) * (double)f);
		if (k1 < field_2628) {
			k1 = field_2628;
		}

		if (l1 < field_2629) {
			l1 = field_2629;
		}

		if (k1 >= field_2630) {
			k1 = field_2630 - 1;
		}

		if (l1 >= field_2631) {
			l1 = field_2631 - 1;
		}

		int i2 = this.client.textureManager.getTextureId("/terrain.png");
		int j2 = this.client.textureManager.getTextureId("/achievement/bg.png");
		int k2 = (this.width - this.field_2618) / 2;
		int l2 = (this.height - this.field_2619) / 2;
		int i3 = k2 + 16;
		int j3 = l2 + 17;
		this.zOffset = 0.0F;
		GL11.glDepthFunc(518);
		GL11.glPushMatrix();
		GL11.glTranslatef(0.0F, 0.0F, -200.0F);
		GL11.glEnable(3553);
		GL11.glDisable(2896);
		GL11.glEnable(32826);
		GL11.glEnable(2903);
		this.client.textureManager.bindTexture(i2);
		int k3 = k1 + 288 >> 4;
		int i4 = l1 + 288 >> 4;
		int j4 = (k1 + 288) % 16;
		int i5 = (l1 + 288) % 16;
		Random random = new Random();

		int l8;
		int k4;
		int j5;
		for(l8 = 0; l8 * 16 - i5 < 155; ++l8) {
			float f5 = 0.6F - (float)(i4 + l8) / 25.0F * 0.3F;
			GL11.glColor4f(f5, f5, f5, 1.0F);

			for(k4 = 0; k4 * 16 - j4 < 224; ++k4) {
				random.setSeed((long)(1234 + k3 + k4));
				random.nextInt();
				j5 = SAPI.acGetCurrentPage().bgGetSprite(random, k3 + k4, i4 + l8);
				if (j5 != -1) {
					this.blit(i3 + k4 * 16 - j4, j3 + l8 * 16 - i5, j5 % 16 << 4, j5 >> 4 << 4, 16, 16);
				}
			}
		}

		GL11.glEnable(2929);
		GL11.glDepthFunc(515);
		GL11.glDisable(3553);

		int l5;
		int k6;
		int k8;
		int i7;
		for(l8 = 0; l8 < Achievements.achievements.size(); ++l8) {
			Achievement ny2 = (Achievement)Achievements.achievements.get(l8);
			if (ny2.parent != null) {
				k4 = ny2.tableColumn * 24 - k1 + 11 + i3;
				j5 = ny2.tableRow * 24 - l1 + 11 + j3;
				l5 = ny2.parent.tableColumn * 24 - k1 + 11 + i3;
				k6 = ny2.parent.tableRow * 24 - l1 + 11 + j3;
				boolean flag = this.statsFileWriter.isAchievementUnlocked(ny2);
				boolean flag1 = this.statsFileWriter.isAchievementUnlockable(ny2);
				k8 = Math.sin((double)(System.currentTimeMillis() % 600L) / 600.0 * Math.PI * 2.0) <= 0.6 ? 130 : 255;
				if (flag) {
					i7 = -9408400;
				} else if (flag1) {
					i7 = '\uff00' + (k8 << 24);
				} else {
					i7 = -16777216;
				}

				this.draw = this.isVisibleLine(ny2);
				this.drawLineHorizontal(k4, l5, j5, i7);
				this.drawLineVertical(l5, j5, k6, i7);
			}
		}

		Achievement ny1 = null;
		ItemRenderer bb1 = new ItemRenderer();
		GL11.glPushMatrix();
		GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
		RenderHelper.enableLighting();
		GL11.glPopMatrix();
		GL11.glDisable(2896);
		GL11.glEnable(32826);
		GL11.glEnable(2903);

		int k7;
		for(k4 = 0; k4 < Achievements.achievements.size(); ++k4) {
			Achievement ny4 = (Achievement)Achievements.achievements.get(k4);
			if (this.isVisibleAchievement(ny4, 1)) {
				l5 = ny4.tableColumn * 24 - k1;
				k6 = ny4.tableRow * 24 - l1;
				if (l5 >= -24 && k6 >= -24 && l5 <= 224 && k6 <= 155) {
					float f2;
					if (this.statsFileWriter.isAchievementUnlocked(ny4)) {
						f2 = 1.0F;
						GL11.glColor4f(f2, f2, f2, 1.0F);
					} else if (this.statsFileWriter.isAchievementUnlockable(ny4)) {
						f2 = Math.sin((double)(System.currentTimeMillis() % 600L) / 600.0 * Math.PI * 2.0) >= 0.6 ? 0.8F : 0.6F;
						GL11.glColor4f(f2, f2, f2, 1.0F);
					} else {
						f2 = 0.3F;
						GL11.glColor4f(f2, f2, f2, 1.0F);
					}

					this.client.textureManager.bindTexture(j2);
					i7 = i3 + l5;
					k7 = j3 + k6;
					if (ny4.isUnusual()) {
						this.blit(i7 - 2, k7 - 2, 26, 202, 26, 26);
					} else {
						this.blit(i7 - 2, k7 - 2, 0, 202, 26, 26);
					}

					if (!this.statsFileWriter.isAchievementUnlockable(ny4)) {
						float f4 = 0.1F;
						GL11.glColor4f(f4, f4, f4, 1.0F);
						bb1.field_1707 = false;
					}

					GL11.glEnable(2896);
					GL11.glEnable(2884);
					bb1.method_1487(this.client.textRenderer, this.client.textureManager, ny4.displayItem, i7 + 3, k7 + 3);
					GL11.glDisable(2896);
					if (!this.statsFileWriter.isAchievementUnlockable(ny4)) {
						bb1.field_1707 = true;
					}

					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					if (i1 >= i3 && j1 >= j3 && i1 < i3 + 224 && j1 < j3 + 155 && i1 >= i7 && i1 <= i7 + 22 && j1 >= k7 && j1 <= k7 + 22) {
						ny1 = ny4;
					}
				}
			}
		}

		GL11.glDisable(2929);
		GL11.glEnable(3042);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.client.textureManager.bindTexture(j2);
		this.blit(k2, l2, 0, 0, this.field_2618, this.field_2619);
		GL11.glPopMatrix();
		this.zOffset = 0.0F;
		GL11.glDepthFunc(515);
		GL11.glDisable(2929);
		GL11.glEnable(3553);
		super.render(i1, j1, f);
		if (ny1 != null) {
			Achievement ny3 = ny1;
			String s1 = ny1.name;
			String s2 = ny1.getDescription();
			k6 = i1 + 12;
			i7 = j1 - 4;
			if (this.statsFileWriter.isAchievementUnlockable(ny1)) {
				k7 = Math.max(this.textRenderer.getTextWidth(s1), 120);
				int j8 = this.textRenderer.method_1902(s2, k7);
				if (this.statsFileWriter.isAchievementUnlocked(ny1)) {
					j8 += 12;
				}

				this.fillGradient(k6 - 3, i7 - 3, k6 + k7 + 3, i7 + j8 + 3 + 12, -1073741824, -1073741824);
				this.textRenderer.method_1904(s2, k6, i7 + 12, k7, -6250336);
				if (this.statsFileWriter.isAchievementUnlocked(ny1)) {
					this.textRenderer.drawTextWithShadow((String)Internationalization.translate("achievement.taken"), k6, i7 + j8 + 4, -7302913);
				}
			} else {
				k7 = Math.max(this.textRenderer.getTextWidth(s1), 120);
				String s3 = Internationalization.translate("achievement.requires", new Object[]{ny3.parent.name});
				k8 = this.textRenderer.method_1902(s3, k7);
				this.fillGradient(k6 - 3, i7 - 3, k6 + k7 + 3, i7 + k8 + 12 + 3, -1073741824, -1073741824);
				this.textRenderer.method_1904(s3, k6, i7 + 12, k7, -9416624);
			}

			this.textRenderer.drawTextWithShadow(s1, k6, i7, this.statsFileWriter.isAchievementUnlockable(ny1) ? (ny1.isUnusual() ? -128 : -1) : (ny1.isUnusual() ? -8355776 : -8355712));
		}

		GL11.glEnable(2929);
		GL11.glEnable(2896);
		RenderHelper.disableLighting();
	}

	@Override
	public void fill(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
		int i;
		if (paramInt1 < paramInt3) {
			i = paramInt1;
			paramInt1 = paramInt3;
			paramInt3 = i;
		}

		if (paramInt2 < paramInt4) {
			i = paramInt2;
			paramInt2 = paramInt4;
			paramInt4 = i;
		}

		float f1 = (float)(paramInt5 >> 24 & 255) / 255.0F;
		float f2 = (float)(paramInt5 >> 16 & 255) / 255.0F;
		float f3 = (float)(paramInt5 >> 8 & 255) / 255.0F;
		float f4 = (float)(paramInt5 & 255) / 255.0F;
		Tessellator localns = Tessellator.INSTANCE;
		GL11.glEnable(3042);
		GL11.glDisable(3553);
		GL11.glBlendFunc(770, 771);
		GL11.glColor4f(f2, f3, f4, f1);
		if (this.draw) {
			localns.start();
			localns.addVertex((double)paramInt1, (double)paramInt4, 0.0);
			localns.addVertex((double)paramInt3, (double)paramInt4, 0.0);
			localns.addVertex((double)paramInt3, (double)paramInt2, 0.0);
			localns.addVertex((double)paramInt1, (double)paramInt2, 0.0);
			localns.tessellate();
		}

		GL11.glEnable(3553);
		GL11.glDisable(3042);
	}

	@Override
	public boolean isVisibleAchievement(Achievement achievement, int deep) {
		if (this.checkHidden(achievement)) {
			return false;
		} else {
			int tabID = SAPI.acGetPage(achievement).getId();
			if (tabID == SAPI.acCurrentPage) {
				return true;
			} else {
				if (deep >= 1) {
					ArrayList<Object> list = new ArrayList(Achievements.achievements);

					int i;
					Achievement tmpAc;
					for(i = 0; i < list.size(); ++i) {
						tmpAc = (Achievement)list.get(i);
						if (tmpAc.id == achievement.id) {
							list.remove(i--);
						} else if (tmpAc.parent == null) {
							list.remove(i--);
						} else if (tmpAc.parent.id != achievement.id) {
							list.remove(i--);
						}
					}

					for(i = 0; i < list.size(); ++i) {
						tmpAc = (Achievement)list.get(i);
						if (this.isVisibleAchievement(tmpAc, deep - 1)) {
							return true;
						}
					}
				}

				return false;
			}
		}
	}

	@Override
	public boolean isVisibleLine(Achievement achievement) {
		return achievement.parent != null && this.isVisibleAchievement(achievement, 1) && this.isVisibleAchievement(achievement.parent, 1);
	}

	@Override
	public boolean checkHidden(Achievement achievement) {
		if (this.client.statFileWriter.isAchievementUnlocked(achievement)) {
			return false;
		} else if (SAPI.acIsHidden(achievement)) {
			return true;
		} else {
			return achievement.parent == null ? false : this.checkHidden(achievement.parent);
		}
	}

	@Public
	private static Class getArrayClass(Class c) {
		try {
			Object e = Array.newInstance(c, 0);
			return e.getClass();
		} catch (Exception var2) {
			throw new IllegalArgumentException(var2);
		}
	}
}
