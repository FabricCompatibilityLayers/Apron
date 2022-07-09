package guiapi.widget;

import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.ToggleButton;
import de.matthiasmann.twl.model.SimpleBooleanModel;
import guiapi.ModScreen;
import guiapi.ModSettingScreen;
import guiapi.setting.KeySetting;
import org.lwjgl.input.Keyboard;

public class KeybindingWidget extends SettingWidget implements Runnable {
	public SimpleBooleanModel booleanModel;
	public int CLEARKEY = 211;
	public int NEVERMINDKEY = 1;
	public KeySetting settingReference;
	public ToggleButton toggleButton;

	public KeybindingWidget(KeySetting setting, String title) {
		super(title);
		this.setTheme("");
		this.settingReference = setting;
		this.settingReference.displayWidget = this;
		this.booleanModel = new SimpleBooleanModel(false);
		this.toggleButton = new ToggleButton(this.booleanModel);
		this.add(this.toggleButton);
		this.update();
	}

	@Override
	public void addCallback(Runnable paramRunnable) {
		this.booleanModel.addCallback(paramRunnable);
	}

	@Override
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
			ModScreen.clicksound();
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void keyboardFocusLost() {
		ModScreen.clicksound();
		this.booleanModel.setValue(false);
	}

	@Override
	public void removeCallback(Runnable paramRunnable) {
		this.booleanModel.removeCallback(paramRunnable);
	}

	@Override
	public void run() {
		ModScreen.clicksound();
	}

	@Override
	public void update() {
		this.toggleButton.setText(this.userString());
	}

	@Override
	public String userString() {
		return String.format("%s: %s", this.niceName, Keyboard.getKeyName(this.settingReference.get(ModSettingScreen.guiContext)));
	}
}
