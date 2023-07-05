package io.github.betterthanupdates.shockahpi.mixin.client;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

import fr.catcore.modremapperapi.api.mixin.Public;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import shockahpi.SAPI;

import net.minecraft.block.Block;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.menu.AchievementsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widgets.OptionButtonWidget;
import net.minecraft.client.render.Tessellator;
import net.minecraft.stat.achievement.Achievement;
import net.minecraft.stat.achievement.Achievements;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.betterthanupdates.shockahpi.client.gui.screen.ShockAhPIAchievementsScreen;

@Mixin(AchievementsScreen.class)
public class AchievementsScreenMixin extends Screen implements ShockAhPIAchievementsScreen {

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

	@Redirect(method = "method_1998", at = @At(value = "FIELD", target = "Lnet/minecraft/block/Block;texture:I"))
	public int method_1998$redirectTexture(Block instance, @Local Random random, @Local(index = 12) int k3, @Local(index = 24) int i9, @Local(index = 13) int i4, @Local(index = 22) int l8) {
		return SAPI.acGetCurrentPage().bgGetSprite(random, k3 + i9, i4 + l8);
	}

	@WrapWithCondition(method = "method_1998", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/menu/AchievementsScreen;blit(IIIIII)V", ordinal = 0))
	public boolean method_1998(AchievementsScreen instance, int i, int j, int k, int l, int m, int n, @Local(index = 25) int var25) {
		return var25 != -1;
	}

	@Inject(method = "method_1998", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/menu/AchievementsScreen;drawLineHorizontal(IIII)V"))
	public void method_1998(CallbackInfo ci, @Local Achievement achievement) {
		this.draw = this.isVisibleLine(achievement);
	}

	@ModifyVariable(method = "method_1998", index = 16, at = @At(value = "STORE", ordinal = 1))
	public int method_1998(int value, @Local Achievement achievement) {
		if (!this.isVisibleAchievement(achievement, 1)) {
			return -25;
		} else {
			return value;
		}
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

		float f1 = (float) (paramInt5 >> 24 & 0xFF) / 255.0F;
		float f2 = (float) (paramInt5 >> 16 & 0xFF) / 255.0F;
		float f3 = (float) (paramInt5 >> 8 & 0xFF) / 255.0F;
		float f4 = (float) (paramInt5 & 0xFF) / 255.0F;
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
