package shockahpi;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.menu.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widgets.OptionButtonWidget;
import net.minecraft.stat.Stats;
import net.minecraft.world.World;

/**
 * AKA "GuiYesNoFreezeDifficulty".
 */
public class FreezeDifficultyScreen extends Screen {
	public World world;

	public FreezeDifficultyScreen(World world) {
		this.world = world;
	}

	public void init() {
		this.buttons.add(new OptionButtonWidget(0, this.width / 2 - 155 + 0, this.height / 6 + 96, "Okay"));
		this.buttons.add(new OptionButtonWidget(1, this.width / 2 - 155 + 160, this.height / 6 + 96, "Cancel"));
	}

	protected void buttonClicked(ButtonWidget button) {
		if (button.id == 0) {
			ShockAhPI.lockDifficulty = this.client.options.difficulty;
			ShockAhPI.saveNBT(this.client.world);
			this.client.openScreen(null);
		} else {
			this.client.statFileWriter.incrementStat(Stats.leaveGame, 1);

			if (this.client.hasWorld()) {
				this.client.world.disconnect();
			}

			this.client.setWorld(null);
			this.client.openScreen(new TitleScreen());
		}
	}

	public void render(int paramInt1, int paramInt2, float paramFloat) {
		this.renderBackground();
		this.drawTextWithShadowCentred(this.textRenderer, "Warning: Difficulty will be locked in place, because of these mods:", this.width / 2, 70, 16777215);
		this.drawTextWithShadowCentred(this.textRenderer, SAPI.lockedErrorList(), this.width / 2, 90, 16777215);
		super.render(paramInt1, paramInt2, paramFloat);
	}
}
