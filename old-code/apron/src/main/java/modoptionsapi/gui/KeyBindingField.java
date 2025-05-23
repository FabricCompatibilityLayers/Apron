package modoptionsapi.gui;

import modoptionsapi.ModKeyOption;
import modoptionsapi.ModOptionsGuiController;

import net.minecraft.client.Minecraft;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;

public class KeyBindingField extends TextInputField {
	private boolean wide = false;

	public KeyBindingField(int id, Screen guiscreen, TextRenderer fontrenderer, int i, int j, ModKeyOption op, ModOptionsGuiController gui, boolean global) {
		super(id, i, j, fontrenderer, gui);
		this.active = true;
		this.parentGuiScreen = guiscreen;
		this.x = i;
		this.y = j;
		this.width = 70;
		this.height = 20;
		this.option = op;
		this.global = global;
	}

	public void setWide(boolean wide) {
		wide = true;
	}

	private void setKey(Integer c) {
		((ModKeyOption) this.option).setValue(c, this.global);
	}

	private Integer getKey() {
		return ((ModKeyOption) this.option).getValue(this.global);
	}

	public void textboxKeyTyped(char c, int i) {
		Integer val = i;

		if (this.active && this.isFocused()) {
			if (c == '\t') {
				this.parentGuiScreen.handleTab();
			} else if (c != 22) {
				if (!ModKeyOption.isKeyBound(val)) {
					this.setKey(val);
					this.setFocused(false);
				} else if (val.equals(((ModKeyOption) this.option).getValue(this.global))) {
					this.setFocused(false);
				}
			}
		}
	}

	public void render(Minecraft minecraft, int i, int j) {
		Integer key = this.getKey();
		String text = this.gui.getDisplayString(this.option, !this.global);
		String name = this.option.getName();

		if (this.wide) {
			int var10000 = this.parentGuiScreen.width / 2 - this.width;
		} else {
			int var9 = this.parentGuiScreen.width - this.width;
		}

		boolean blink = this.getCursorCounter() / 6 % 2 == 0;

		if (this.isFocused() && !blink) {
			this.text = "> " + text + " <";
		} else {
			this.text = text;
		}

		super.render(minecraft, i, j);
		this.drawTextWithShadow(this.fontRenderer, this.option.getName(), this.x + 85, this.y + (this.height - 8) / 2, 16777215);
	}
}
