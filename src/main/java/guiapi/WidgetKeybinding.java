package guiapi;

import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.ToggleButton;
import de.matthiasmann.twl.model.SimpleBooleanModel;
import org.lwjgl.input.Keyboard;

public class WidgetKeybinding extends WidgetSetting implements Runnable {
	public SimpleBooleanModel booleanModel;
	public int CLEARKEY = 211;
	public int NEVERMINDKEY = 1;
	public SettingKey settingReference;
	public ToggleButton toggleButton;

	public WidgetKeybinding(SettingKey setting, String title) {
		super(title);
		this.setTheme("");
		this.settingReference = setting;
		this.settingReference.displayWidget = this;
		this.booleanModel = new SimpleBooleanModel(false);
		this.toggleButton = new ToggleButton(this.booleanModel);
		this.add(this.toggleButton);
		this.update();
	}

	public void addCallback(Runnable paramRunnable) {
		this.booleanModel.addCallback(paramRunnable);
	}

	public boolean handleEvent(Event evt) {
		if (evt.isKeyEvent() && !evt.isKeyPressedEvent() && this.booleanModel.getValue()) {
			System.out.println(Keyboard.getKeyName(evt.getKeyCode()));
			int tmpvalue = evt.getKeyCode();

			if (tmpvalue == this.CLEARKEY) {
				this.settingReference.set(0, ModSettingScreen.guiContext);
			} else if (tmpvalue != this.NEVERMINDKEY) {
				this.settingReference.set(tmpvalue, ModSettingScreen.guiContext);
			}

			this.booleanModel.setValue(false);
			this.update();
			GuiModScreen.clicksound();
			return true;
		} else {
			return false;
		}
	}

	public void keyboardFocusLost() {
		GuiModScreen.clicksound();
		this.booleanModel.setValue(false);
	}

	public void removeCallback(Runnable paramRunnable) {
		this.booleanModel.removeCallback(paramRunnable);
	}

	public void run() {
		GuiModScreen.clicksound();
	}

	public void update() {
		this.toggleButton.setText(this.userString());
	}

	public String userString() {
		return String.format("%s: %s", this.niceName, Keyboard.getKeyName(this.settingReference.get(ModSettingScreen.guiContext)));
	}
}
