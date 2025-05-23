package modoptionsapi.gui;

import modoptionsapi.ModOptionsGuiController;
import modoptionsapi.ModSliderOption;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.ButtonWidget;

public class Slider extends ButtonWidget {
	private static final int SMALL_WIDTH = 150;
	private static final int WIDE_WIDTH = 200;
	public boolean dragging;
	private ModSliderOption option;
	private ModOptionsGuiController gui;
	private boolean worldMode;

	public Slider(int i, int j, int k, ModSliderOption op, ModOptionsGuiController gui, boolean worldMode) {
		super(i, j, k, 150, 20, gui.getDisplayString(op, worldMode));
		this.gui = gui;
		this.worldMode = worldMode;
		this.dragging = false;
		this.option = op;
		this.text = gui.getDisplayString(op, worldMode);
	}

	public String getName() {
		return this.option.getName();
	}

	public void setWide(boolean wide) {
		if (wide) {
			this.width = 200;
		} else {
			this.width = 150;
		}
	}

	private float getInternalValue(ModSliderOption option) {
		float val = 0.0F;

		if (this.worldMode) {
			val = option.getLocalValue();
		} else {
			val = option.getGlobalValue();
		}

		return option.transformValue(val, 0, 1);
	}

	protected void method_1188(Minecraft minecraft, int i, int j) {
		if (this.visible) {
			if (this.dragging) {
				float value = (float) (i - (this.x + 4)) / (float) (this.width - 8);
				value = this.option.untransformValue(value, 0, 1);

				if (this.worldMode) {
					if (this.option.useGlobalValue()) {
						this.option.setGlobal(false);
					}

					this.option.setLocalValue(value);
				} else {
					this.option.setGlobalValue(value);
				}

				this.updateDisplayString();
			}

			float sliderValue = this.getInternalValue(this.option);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.drawTexture(this.x + (int) (sliderValue * (float) (this.width - 8)), this.y, 0, 66, 4, 20);
			this.drawTexture(this.x + (int) (sliderValue * (float) (this.width - 8)) + 4, this.y, 196, 66, 4, 20);
		}
	}

	public boolean altMousePressed(Minecraft minecraft, int i, int j, boolean rightClick) {
		if (rightClick && super.isMouseOver(minecraft, i, j)) {
			return true;
		} else {
			return !rightClick && this.isMouseOver(minecraft, i, j);
		}
	}

	public boolean isMouseOver(Minecraft minecraft, int i, int j) {
		if ((!this.option.hasCallback() || this.option.getCallback().onClick(this.option)) && super.isMouseOver(minecraft, i, j)) {
			float value = (float) (i - (this.x + 4)) / (float) (this.width - 8);
			value = this.option.untransformValue(value, 0, 1);

			if (this.worldMode) {
				if ((!this.option.hasCallback() || this.option.getCallback().onGlobalChange(false, this.option)) && this.option.useGlobalValue()) {
					this.option.setGlobal(false);
				}

				this.option.setLocalValue(value);
			} else {
				this.option.setGlobalValue(value);
			}

			this.updateDisplayString();
			this.dragging = true;
			return true;
		} else {
			return false;
		}
	}

	public void updateDisplayString() {
		this.text = this.gui.getDisplayString(this.option, this.worldMode);
	}

	public void mouseReleased(int i, int j) {
		this.dragging = false;
	}

	protected int getHoverState(boolean flag) {
		return 0;
	}
}
