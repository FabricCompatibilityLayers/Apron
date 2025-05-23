package modoptionsapi.gui;

import modoptionsapi.ModOptionsGuiController;
import modoptionsapi.ModTextOption;

import net.minecraft.client.Minecraft;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.CharacterUtils;

public class TextField extends TextInputField {
	public TextField(int id, Screen guiscreen, TextRenderer fontrenderer, int i, int j, ModTextOption op, ModOptionsGuiController gui, boolean global) {
		super(id, i, j, fontrenderer, gui);
		this.parentGuiScreen = guiscreen;
		this.x = i - 50;
		this.y = j;
		this.width = 300;
		this.height = 20;
		this.option = op;
		this.global = global;
	}

	protected void setText(String s) {
		int maxlen = ((ModTextOption) this.option).getMaxLength();

		if (s.length() > maxlen && maxlen > 0) {
			s = s.substring(0, maxlen - 1);
		}

		((ModTextOption) this.option).setValue(s, this.global);
	}

	public String getText() {
		return ((ModTextOption) this.option).getValue(this.global);
	}

	public void textboxKeyTyped(char c, int i) {
		String text = this.getText();
		String s = Screen.getClipboard();
		int max = ((ModTextOption) this.option).getMaxLength();

		if (this.active && this.isFocused()) {
			if (c == '\t') {
				this.parentGuiScreen.handleTab();
			} else if (c == 22) {
				if (s == null) {
					s = "";
				}

				int j = 32 - text.length();

				if (j > s.length()) {
					j = s.length();
				}

				if (j > 0) {
					text = text + s.substring(0, j);
				}
			} else if (i == 14 && text.length() > 0) {
				text = text.substring(0, text.length() - 1);
			} else if (CharacterUtils.VALID_CHARACTERS.indexOf(c) >= 0 && (text.length() < max || max == 0)) {
				text = text + c;
			}

			this.setText(text);
		}
	}

	public void render(Minecraft minecraft, int i, int j) {
		String text = this.getText();
		String name = this.option.getName();
		int maxlen = ((ModTextOption) this.option).getMaxLength();
		int len = text.length();
		int padding = 30;
		String counterStr = maxlen > 0 ? "(" + len + "/" + maxlen + ")" : "";
		int nameWidth = this.fontRenderer.getWidth(name);
		int textWidth = this.fontRenderer.getWidth(text);
		int counterWidth = this.fontRenderer.getWidth(counterStr);

		if (maxlen <= 0) {
			padding -= 10;
		}

		if (nameWidth + textWidth + counterWidth + padding > this.width) {
			while (nameWidth + textWidth + counterWidth + padding > this.width) {
				text = text.substring(1, len - 1);
				--len;
				textWidth = this.fontRenderer.getWidth(text);
			}
		}

		this.fill(this.x - 1, this.y - 1, this.x + this.width + 1, this.y + this.height + 1, -6250336);
		this.fill(this.x, this.y, this.x + this.width, this.y + this.height, -16777216);

		if (this.active) {
			this.drawTextWithShadow(this.fontRenderer, this.option.getName(), this.x + 4, this.y + (this.height - 8) / 2, 7368816);
			boolean flag = this.isFocused() && this.getCursorCounter() / 6 % 2 == 0;
			this.drawTextWithShadow(this.fontRenderer, text + (flag ? "_" : ""), this.x + nameWidth + 10, this.y + (this.height - 8) / 2, 14737632);

			if (maxlen > 0) {
				this.drawTextWithShadow(this.fontRenderer, counterStr, this.x + 300 - 5 - counterWidth, this.y + (this.height - 8) / 2, 7368816);
			}
		} else {
			this.drawTextWithShadow(this.fontRenderer, this.getText(), this.x + 4, this.y + (this.height - 8) / 2, 7368816);
		}
	}
}
