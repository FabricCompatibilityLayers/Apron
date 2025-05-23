package modoptionsapi.gui;

import modoptionsapi.ModBooleanOption;
import modoptionsapi.ModKeyOption;
import modoptionsapi.ModMappedMultiOption;
import modoptionsapi.ModMultiOption;
import modoptionsapi.ModOption;
import modoptionsapi.ModOptions;
import modoptionsapi.ModOptionsAPI;
import modoptionsapi.ModOptionsGuiController;
import modoptionsapi.ModSliderOption;
import modoptionsapi.ModTextOption;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.OptionButtonWidget;
import net.minecraft.client.option.GameOptions;

public class ModMenu extends Screen {
	protected ButtonWidget curButton = null;
	private int sliderMiddle = -1;
	private boolean draggingSlider = false;
	protected String screenTitle;
	private Screen parentScreen;
	private GameOptions options;
	private ModOptions modOptions = null;
	private ModOptionsGuiController gui = null;
	private boolean worldMode = false;
	private boolean multiplayerWorld = false;
	private String worldName;

	public ModMenu(Screen guiscreen) {
		this(guiscreen, "Mod Options List", false, false);
	}

	public ModMenu(GameMenuScreen guiscreen, String name, boolean mult) {
		this(guiscreen, "World Specific Mod Options", true, mult);
		this.worldName = name;
	}

	public ModMenu(Screen guiscreen, ModOptions options, String name, boolean multi) {
		this(guiscreen, options.getName() + " Options", true, multi);
		this.modOptions = options;
		this.worldName = name;
		this.gui = this.modOptions.getGuiController();
	}

	public ModMenu(Screen guiscreen, ModOptions options) {
		this(guiscreen, options.getName() + " Options", false, false);
		this.modOptions = options;
		this.gui = this.modOptions.getGuiController();
	}

	private ModMenu(Screen parent, String title, boolean world, boolean mult) {
		this.parentScreen = parent;
		this.worldMode = world;
		this.screenTitle = title;
		this.multiplayerWorld = mult;

		if (!Keyboard.areRepeatEventsEnabled()) {
			Keyboard.enableRepeatEvents(true);
		}
	}

	public void init() {
		if (this.modOptions == null) {
			ModOptions[] options;

			if (this.multiplayerWorld) {
				options = ModOptionsAPI.getMultiplayerMods();
			} else if (this.worldMode) {
				options = ModOptionsAPI.getSingleplayerMods();
			} else {
				options = ModOptionsAPI.getAllMods();
			}

			this.loadModList(options);
		} else {
			this.loadModOptions();
		}
	}

	private void loadModList(ModOptions[] options) {
		int xPos = this.width / 2 - 100;

		for (int i = 0; i < options.length; ++i) {
			int yPos = this.height / 6 + i * 24 + 12;
			this.buttons.add(new ButtonWidget(i, xPos, yPos, options[i].getName()));
		}

		int yPos = this.height / 6 + 168;
		this.buttons.add(new ButtonWidget(200, xPos, yPos, "Done"));
	}

	private void loadModOptions() {
		this.screenTitle = this.modOptions.getName();
		ModOption<?>[] ops = this.modOptions.getOptions();
		ModOptions[] subOps;

		if (!this.worldMode) {
			subOps = this.modOptions.getSubOptions();
		} else if (this.multiplayerWorld) {
			subOps = this.modOptions.getMultiplayerSubOptions();
		} else {
			subOps = this.modOptions.getSingleplayerSubOptions();
		}

		int id = 0;
		int pos = 2;

		for (ModOption<?> op : ops) {
			this.addModOptionButton(op, id, pos);

			if (this.gui.isWide(op)) {
				pos = pos + 2 + pos % 2;
			} else {
				++pos;
			}

			++id;
		}

		for (int x = 0; x < subOps.length; ++x) {
			int xPos = this.width / 2 - 100;
			int yPos = this.height / 6 + 24 * (pos + 1 >> 1);
			this.buttons.add(new ButtonWidget(x + 101, xPos, yPos, subOps[x].getName()));
			pos = pos + 2 + pos % 2;
		}

		this.buttons.add(new ButtonWidget(200, this.width / 2 - 100, this.height / 6 + 168, "Done"));
	}

	private void addModOptionButton(ModOption<?> op, int id, int pos) {
		String display = this.gui.getDisplayString(op, this.worldMode);
		boolean isWide = this.gui.isWide(op);
		int xPos;
		int yPos;

		if (!isWide) {
			xPos = this.width / 2 - 155 + pos % 2 * 160;
			yPos = this.height / 6 + 24 * (pos >> 1);
		} else {
			xPos = this.width / 2 - 100;
			yPos = this.height / 6 + 24 * (pos + 1 >> 1);
		}

		if (op instanceof ModSliderOption) {
			Slider btn = new Slider(id, xPos, yPos, (ModSliderOption) op, this.gui, this.worldMode);
			btn.setWide(isWide);
			this.buttons.add(btn);
		} else if (op instanceof ModTextOption) {
			this.buttons.add(new TextField(id, this, this.textRenderer, xPos, yPos, (ModTextOption) op, this.gui, !this.worldMode));
		} else if (op instanceof ModKeyOption) {
			KeyBindingField btn = new KeyBindingField(id, this, this.textRenderer, xPos, yPos, (ModKeyOption) op, this.gui, !this.worldMode);
			this.buttons.add(btn);
			btn.setWide(isWide);
		} else if (!isWide) {
			this.buttons.add(new OptionButtonWidget(id, xPos, yPos, display));
		} else {
			this.buttons.add(new ButtonWidget(id, xPos, yPos, display));
		}
	}

	public void render(int i, int j, float f) {
		this.renderBackground();
		this.drawCenteredTextWithShadow(this.textRenderer, this.screenTitle, this.width / 2, 20, 16777215);

		if (this.sliderMiddle == -1) {
			this.setInitialSlider();
		} else if (this.getSliderTop() > this.getSliderAreaTop()) {
			this.sliderMiddle = this.getUpperSliderBound();
		} else if (this.getSliderBottom() < this.getSliderAreaBottom()) {
			this.setInitialSlider();
		}

		this.addButtons(i, j);
		int sliderTop = this.getSliderTop();
		int sliderBottom = this.getSliderBottom();
		int sliderLeft = this.getSliderLeft();
		int sliderRight = this.getSliderRight();
		this.fill(sliderLeft, this.getSliderAreaBottom(), sliderRight, this.getSliderAreaTop(), Integer.MIN_VALUE);
		this.fill(sliderLeft, sliderBottom, sliderRight, sliderTop, -3355444);
		this.sliderDragged(i, j);
	}

	private void addButtons(int i, int j) {
		int contentTop = this.getContentTop();
		int contentBottom = this.getContentBottom();
		int bottom = this.getSliderAreaBottom();
		int top = this.getSliderAreaTop();

		for (Object button : this.buttons) {
			ButtonWidget btn = (ButtonWidget) button;

			if (btn.id != 200) {
				int y = btn.y;
				btn.y = y - contentBottom;

				if (btn.y > bottom && btn.y + 20 < top) {
					btn.render(this.minecraft, i, j);
				}

				btn.y = y;
			} else {
				btn.render(this.minecraft, i, j);
			}
		}
	}

	protected void mouseClicked(int i, int j, int k) {
		this.setCurrentButton(null);

		if (k == 0) {
			if (i > this.getSliderLeft() && i < this.getSliderRight() && j > this.getSliderBottom() && j < this.getSliderTop()) {
				this.setSliderMiddle(j);
			} else if (i > this.getSliderLeft() && i < this.getSliderRight() && j > this.getSliderAreaBottom() && j < this.getSliderAreaTop()) {
				this.setSliderMiddle(j);
			} else {
				for (Object button : this.buttons) {
					ButtonWidget guibutton = (ButtonWidget) button;

					if (this.buttonPressed(guibutton, i, j, false)) {
						this.setCurrentButton(guibutton);
					}
				}
			}
		}

		if (k == 1) {
			for (Object button : this.buttons) {
				ButtonWidget guibutton = (ButtonWidget) button;

				if (this.buttonPressed(guibutton, i, j, true)) {
					this.altActionPerformed(guibutton);
					this.minecraft.soundManager.method_2009("random.click", 1.0F, 1.0F);
					this.setCurrentButton(guibutton);
				}
			}
		}
	}

	protected void keyPressed(char c, int i) {
		if (i == 1) {
			this.changeScreen(null);
		} else if (this.curButton instanceof TextInputField) {
			this.handleTextAction((TextInputField) this.curButton, c, i);
		}
	}

	private void handleTextAction(TextInputField txt, char c, int i) {
		txt.textboxKeyTyped(c, i);
	}

	protected void buttonClicked(ButtonWidget guibutton) {
		if (guibutton.id == 200) {
			this.changeScreen(this.parentScreen);
		} else if (this.modOptions == null) {
			ModOptions modOp = ModOptionsAPI.getModOptions(guibutton.text);

			if (this.worldMode) {
				this.minecraft.setScreen(new ModMenu(this, modOp, this.worldName, this.multiplayerWorld));
			} else {
				this.minecraft.setScreen(new ModMenu(this, modOp));
			}
		} else if (guibutton.id >= 100 || !(guibutton instanceof Slider)) {
			if (guibutton.id > 100 && guibutton.id < 200) {
				ModOptions modOp = this.modOptions.getSubOption(guibutton.text);

				if (this.worldMode) {
					this.minecraft.setScreen(new ModMenu(this, modOp, this.worldName, this.multiplayerWorld));
				} else {
					this.minecraft.setScreen(new ModMenu(this, modOp));
				}
			} else if (guibutton.id < 100) {
				this.optionButtonPressed(guibutton);
			}
		}
	}

	protected void altActionPerformed(ButtonWidget guibutton) {
		if (this.modOptions != null && guibutton.id < 100 && this.worldMode) {
			ModOption<?>[] ops = this.modOptions.getOptions();
			ModOption<?> option = ops[guibutton.id];
			option.setGlobal(!option.useGlobalValue());
			this.updateDisplayString(option, guibutton);
		} else if (this.worldMode && (this.modOptions == null || guibutton.id < 200)) {
			ModOptions modOp;

			if (this.modOptions == null) {
				modOp = ModOptionsAPI.getModOptions(guibutton.text);
			} else {
				modOp = this.modOptions.getSubOption(guibutton.text);
			}

			if (modOp != null) {
				modOp.globalReset(true);
			}
		}
	}

	protected void mouseReleased(int i, int j, int k) {
		if (!(this.curButton instanceof TextInputField) && this.curButton != null && k == 0) {
			this.curButton.mouseReleased(i, j);
			this.curButton = null;
		} else if (this.draggingSlider && k == 0) {
			this.draggingSlider = false;
		}
	}

	public void tick() {
		super.tick();

		for (Object obj : this.buttons) {
			if (obj instanceof TextInputField) {
				((TextInputField) obj).updateCursorCounter();
			}
		}
	}

	public void changeScreen(Screen screen) {
		this.saveChanges();
		this.minecraft.setScreen(screen);

		if (!(screen instanceof ModMenu)) {
			Keyboard.enableRepeatEvents(false);
		}

		if (this.worldMode && screen == null) {
			this.minecraft.method_2133();
		}
	}

	private void sliderDragged(int i, int j) {
		if (this.draggingSlider) {
			this.setSliderMiddle(j);
		}
	}

	private void setCurrentButton(ButtonWidget btn) {
		if (this.curButton instanceof TextInputField) {
			((TextInputField) this.curButton).setFocused(false);
		}

		this.curButton = btn;

		if (this.curButton instanceof TextInputField) {
			((TextInputField) this.curButton).setFocused(true);
		}

		if (this.curButton != null) {
			this.minecraft.soundManager.method_2009("random.click", 1.0F, 1.0F);
			this.buttonClicked(this.curButton);
		}
	}

	protected boolean buttonPressed(ButtonWidget btn, int i, int j) {
		return this.buttonPressed(btn, i, j, false);
	}

	protected boolean buttonPressed(ButtonWidget btn, int i, int j, boolean rightClick) {
		int contentBottom = this.getContentBottom();
		int bottom = this.getSliderAreaBottom();
		int top = this.getSliderAreaTop();
		boolean flag = false;

		if (btn.id != 200) {
			int y = btn.y;
			btn.y = y - contentBottom;

			if (btn.y > bottom && btn.y + 20 < top) {
				if (btn instanceof Slider) {
					flag = ((Slider) btn).altMousePressed(this.minecraft, i, j, rightClick);
				} else if (btn.isMouseOver(this.minecraft, i, j)) {
					flag = true;
				}
			}

			btn.y = y;
		} else {
			flag = btn.isMouseOver(this.minecraft, i, j);
		}

		return flag;
	}

	private void setInitialSlider() {
		this.sliderMiddle = this.getLowerSliderBound();
	}

	private void setSliderMiddle(int val) {
		if (val > this.getUpperSliderBound()) {
			this.sliderMiddle = this.getUpperSliderBound();
		} else if (val < this.getLowerSliderBound()) {
			this.sliderMiddle = this.getLowerSliderBound();
		} else {
			this.sliderMiddle = val;
		}

		this.draggingSlider = true;
		this.setCurrentButton(this.curButton);
	}

	private int getUpperSliderBound() {
		return this.getSliderAreaTop() - this.getSliderHeight() / 2;
	}

	private int getLowerSliderBound() {
		return this.getSliderAreaBottom() + this.getSliderHeight() / 2;
	}

	private int getSliderTop() {
		return this.sliderMiddle + this.getSliderHeight() / 2;
	}

	private int getSliderBottom() {
		return this.sliderMiddle - this.getSliderHeight() / 2;
	}

	private int getSliderHeight() {
		int contHeight = this.getContentHeight();
		int areaHeight = this.getSliderAreaHeight();
		return contHeight < areaHeight ? areaHeight : (int) ((double) areaHeight / (double) contHeight * (double) areaHeight);
	}

	private int getContentHeight() {
		int height = 1;
		int bottom = this.getSliderAreaBottom();

		for (Object button : this.buttons) {
			ButtonWidget guibutton = (ButtonWidget) button;

			if (guibutton.id != 200 && guibutton.y - bottom > height) {
				height = guibutton.y - bottom;
			}
		}

		return height + 100;
	}

	private int getContentTop() {
		int top = this.getSliderTop() - this.getSliderAreaBottom();
		int contHeight = this.getContentHeight();
		int areaHeight = this.getSliderAreaHeight();
		double prop;

		if (contHeight < areaHeight) {
			prop = 1.0;
		} else {
			prop = (double) this.getContentHeight() / (double) this.getSliderAreaHeight();
		}

		return (int) ((double) top * prop);
	}

	private int getContentBottom() {
		int bot = this.getSliderBottom() - this.getSliderAreaBottom();
		int contHeight = this.getContentHeight();
		int areaHeight = this.getSliderAreaHeight();
		double prop;

		if (contHeight < areaHeight) {
			prop = 1.0;
		} else {
			prop = (double) (this.getContentHeight() / this.getSliderAreaHeight());
		}

		return (int) ((double) bot * prop);
	}

	private int getSliderLeft() {
		return this.width - 20;
	}

	private int getSliderRight() {
		return this.width - 10;
	}

	private int getSliderAreaTop() {
		return this.height - 40;
	}

	private int getSliderAreaBottom() {
		return 40;
	}

	private int getSliderAreaHeight() {
		int height = this.getSliderAreaTop() - this.getSliderAreaBottom();
		return height > 0 ? height : 1;
	}

	private void optionButtonPressed(ButtonWidget btn) {
		ModOption<?>[] ops = this.modOptions.getOptions();
		ModOption<?> option = ops[btn.id];

		if (!option.hasCallback() || option.getCallback().onClick(option)) {
			if (this.worldMode && option.useGlobalValue() && (!option.hasCallback() || option.getCallback().onGlobalChange(false, option))) {
				option.setGlobal(false);
			}

			this.updateDisplayString(option, btn);
		}
	}

	private void updateDisplayString(ModOption<?> option, ButtonWidget btn) {
		if (option instanceof ModMultiOption) {
			btn.text = this.optionPressed((ModMultiOption) option);
		} else if (option instanceof ModBooleanOption) {
			btn.text = this.optionPressed((ModBooleanOption) option);
		} else if (option instanceof ModMappedMultiOption) {
			btn.text = this.optionPressed((ModMappedMultiOption) option);
		} else if (btn instanceof Slider) {
			((Slider) btn).updateDisplayString();
		}
	}

	private String optionPressed(ModMultiOption op) {
		if (!this.worldMode) {
			String nextVal = op.getNextValue(op.getGlobalValue());
			op.setGlobalValue(nextVal);
		} else {
			String nextVal = op.getNextValue(op.getLocalValue());
			op.setLocalValue(nextVal);
		}

		return this.gui.getDisplayString(op, this.worldMode);
	}

	private String optionPressed(ModBooleanOption op) {
		if (!this.worldMode) {
			boolean nextVal = !op.getGlobalValue();
			op.setGlobalValue(nextVal);
		} else {
			boolean nextVal = !op.getLocalValue();
			op.setLocalValue(nextVal);
		}

		return this.gui.getDisplayString(op, this.worldMode);
	}

	private String optionPressed(ModMappedMultiOption op) {
		if (!this.worldMode) {
			Integer nextVal = op.getNextValue(op.getGlobalValue());
			op.setGlobalValue(nextVal);
		} else {
			Integer nextVal = op.getNextValue(op.getLocalValue());
			op.setLocalValue(nextVal);
		}

		return this.gui.getDisplayString(op, this.worldMode);
	}

	private void saveChanges() {
		if (this.modOptions != null) {
			if (this.worldMode) {
				this.modOptions.save(this.worldName, this.multiplayerWorld);
			} else {
				this.modOptions.save();
			}
		}
	}
}
