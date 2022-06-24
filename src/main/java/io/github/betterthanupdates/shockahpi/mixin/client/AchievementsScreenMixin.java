package io.github.betterthanupdates.shockahpi.mixin.client;

import java.util.ArrayList;
import java.util.Random;

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

import io.github.betterthanupdates.shockahpi.SAPIAchievementsScreen;

@Mixin(AchievementsScreen.class)
public class AchievementsScreenMixin extends Screen implements SAPIAchievementsScreen {
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

	@Inject(method = "init", at = @At("RETURN"))
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

		for(int l8 = 0; l8 * 16 - i5 < 155; ++l8) {
			float f5 = 0.6F - (float)(i4 + l8) / 25.0F * 0.3F;
			GL11.glColor4f(f5, f5, f5, 1.0F);

			for(int i9 = 0; i9 * 16 - j4 < 224; ++i9) {
				random.setSeed((long)(1234 + k3 + i9));
				random.nextInt();
				int k9 = SAPI.acGetCurrentPage().bgGetSprite(random, k3 + i9, i4 + l8);
				if (k9 != -1) {
					this.blit(i3 + i9 * 16 - j4, j3 + l8 * 16 - i5, k9 % 16 << 4, k9 >> 4 << 4, 16, 16);
				}
			}
		}

		GL11.glEnable(2929);
		GL11.glDepthFunc(515);
		GL11.glDisable(3553);

		for(int l3 = 0; l3 < Achievements.achievements.size(); ++l3) {
			Achievement ny2 = (Achievement)Achievements.achievements.get(l3);
			if (ny2.parent != null) {
				int k4 = ny2.tableColumn * 24 - k1 + 11 + i3;
				int j5 = ny2.tableRow * 24 - l1 + 11 + j3;
				int k5 = ny2.parent.tableColumn * 24 - k1 + 11 + i3;
				int i6 = ny2.parent.tableRow * 24 - l1 + 11 + j3;
				int l6 = 0;
				boolean flag = this.statsFileWriter.isAchievementUnlocked(ny2);
				boolean flag1 = this.statsFileWriter.isAchievementUnlockable(ny2);
				char c1 = (char)(Math.sin((double)(System.currentTimeMillis() % 600L) / 600.0 * Math.PI * 2.0) <= 0.6 ? 130 : 255);
				if (flag) {
					l6 = -9408400;
				} else if (flag1) {
					l6 = 65280 + (c1 << 24);
				} else {
					l6 = -16777216;
				}

				this.draw = this.isVisibleLine(ny2);
				this.drawLineHorizontal(k4, k5, j5, l6);
				this.drawLineVertical(k5, j5, i6, l6);
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

		for(int l4 = 0; l4 < Achievements.achievements.size(); ++l4) {
			Achievement ny4 = (Achievement)Achievements.achievements.get(l4);
			if (this.isVisibleAchievement(ny4, 1)) {
				int l5 = ny4.tableColumn * 24 - k1;
				int j6 = ny4.tableRow * 24 - l1;
				if (l5 >= -24 && j6 >= -24 && l5 <= 224 && j6 <= 155) {
					if (this.statsFileWriter.isAchievementUnlocked(ny4)) {
						float f1 = 1.0F;
						GL11.glColor4f(f1, f1, f1, 1.0F);
					} else if (this.statsFileWriter.isAchievementUnlockable(ny4)) {
						float f2 = Math.sin((double)(System.currentTimeMillis() % 600L) / 600.0 * Math.PI * 2.0) >= 0.6 ? 0.8F : 0.6F;
						GL11.glColor4f(f2, f2, f2, 1.0F);
					} else {
						float f3 = 0.3F;
						GL11.glColor4f(f3, f3, f3, 1.0F);
					}

					this.client.textureManager.bindTexture(j2);
					int i7 = i3 + l5;
					int k7 = j3 + j6;
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
			String s1 = ny1.name;
			String s2 = ny1.getDescription();
			int k6 = i1 + 12;
			int j7 = j1 - 4;
			if (this.statsFileWriter.isAchievementUnlockable(ny1)) {
				int l7 = Math.max(this.textRenderer.getTextWidth(s1), 120);
				int j8 = this.textRenderer.method_1902(s2, l7);
				if (this.statsFileWriter.isAchievementUnlocked(ny1)) {
					j8 += 12;
				}

				this.fillGradient(k6 - 3, j7 - 3, k6 + l7 + 3, j7 + j8 + 3 + 12, -1073741824, -1073741824);
				this.textRenderer.method_1904(s2, k6, j7 + 12, l7, -6250336);
				if (this.statsFileWriter.isAchievementUnlocked(ny1)) {
					try {
						this.textRenderer.drawTextWithShadow(Internationalization.translate("achievement.taken"), k6, j7 + j8 + 4, -7302913);
					} catch (Exception var28) {
						var28.printStackTrace();
					}
				}
			} else {
				try {
					int i8 = Math.max(this.textRenderer.getTextWidth(s1), 120);
					String s3 = Internationalization.translate("achievement.requires", ny1.parent.name);
					int k8 = this.textRenderer.method_1902(s3, i8);
					this.fillGradient(k6 - 3, j7 - 3, k6 + i8 + 3, j7 + k8 + 12 + 3, -1073741824, -1073741824);
					this.textRenderer.method_1904(s3, k6, j7 + 12, i8, -9416624);
				} catch (Exception var27) {
					var27.printStackTrace();
				}
			}

			this.textRenderer
					.drawTextWithShadow(
							s1, k6, j7, this.statsFileWriter.isAchievementUnlockable(ny1) ? (ny1.isUnusual() ? -128 : -1) : (ny1.isUnusual() ? -8355776 : -8355712)
					);
		}

		GL11.glEnable(2929);
		GL11.glEnable(2896);
		RenderHelper.disableLighting();
	}

	@Override
	public void fill(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
		if (paramInt1 < paramInt3) {
			int i = paramInt1;
			paramInt1 = paramInt3;
			paramInt3 = i;
		}

		if (paramInt2 < paramInt4) {
			int i = paramInt2;
			paramInt2 = paramInt4;
			paramInt4 = i;
		}

		float f1 = (float)(paramInt5 >> 24 & 0xFF) / 255.0F;
		float f2 = (float)(paramInt5 >> 16 & 0xFF) / 255.0F;
		float f3 = (float)(paramInt5 >> 8 & 0xFF) / 255.0F;
		float f4 = (float)(paramInt5 & 0xFF) / 255.0F;
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
					ArrayList<Achievement> list = new ArrayList(Achievements.achievements);

					for(int i = 0; i < list.size(); ++i) {
						Achievement tmpAc = list.get(i);
						if (tmpAc.id == achievement.id) {
							list.remove(i--);
						} else if (tmpAc.parent == null) {
							list.remove(i--);
						} else if (tmpAc.parent.id != achievement.id) {
							list.remove(i--);
						}
					}

					for (Achievement tmpAc : list) {
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
		if (achievement == null) {
			return true;
		} else if (this.client.statFileWriter.isAchievementUnlocked(achievement)) {
			return false;
		} else if (SAPI.acIsHidden(achievement)) {
			return true;
		} else {
			return achievement.parent != null && this.checkHidden(achievement.parent);
		}
	}
}
