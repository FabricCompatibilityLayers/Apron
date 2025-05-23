package modoptionsapi.gui;

import modoptionsapi.ModOption;
import modoptionsapi.ModOptionsGuiController;

import net.minecraft.client.Minecraft;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;

public abstract class TextInputField extends ButtonWidget {
	protected ModOption<?> option;
	protected boolean global;
	protected final TextRenderer fontRenderer;
	private boolean isFocused = false;
	protected Screen parentGuiScreen;
	private int cursorCounter;
	protected ModOptionsGuiController gui;

	public TextInputField(int i, int j, int k, TextRenderer r, ModOptionsGuiController gui) {
		super(i, j, k, "");
		this.gui = gui;
		this.active = true;
		this.fontRenderer = r;
	}

	public abstract void textboxKeyTyped(char c, int i);

	public void render(Minecraft minecraft, int i, int j) {
		super.render(minecraft, i, j);
	}

	protected int getCursorCounter() {
		return this.cursorCounter;
	}

	public void updateCursorCounter() {
		++this.cursorCounter;
	}

	public void setFocused(boolean flag) {
		if (flag && !this.isFocused) {
			this.cursorCounter = 0;
		}

		this.isFocused = flag;
	}

	public boolean isFocused() {
		return this.isFocused;
	}

	public ModOption<?> getOption() {
		return this.option;
	}
}
