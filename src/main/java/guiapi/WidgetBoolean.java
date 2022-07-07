package guiapi;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.model.SimpleButtonModel;

public class WidgetBoolean extends WidgetSetting implements Runnable {
	public Button button;
	public String falseText;
	public SettingBoolean settingReference;
	public String trueText;

	public WidgetBoolean(SettingBoolean setting, String title) {
		this(setting, title, "true", "false");
	}

	public WidgetBoolean(SettingBoolean setting, String title, String truetext, String falsetext) {
		super(title);
		this.setTheme("");
		this.trueText = truetext;
		this.falseText = falsetext;
		SimpleButtonModel bmodel = new SimpleButtonModel();
		this.button = new Button(bmodel);
		bmodel.addActionCallback(this);
		this.add(this.button);
		this.settingReference = setting;
		this.settingReference.displayWidget = this;
		this.update();
	}

	public void addCallback(Runnable paramRunnable) {
		this.button.getModel().addActionCallback(paramRunnable);
	}

	public void removeCallback(Runnable paramRunnable) {
		this.button.getModel().removeActionCallback(paramRunnable);
	}

	public void run() {
		if (this.settingReference != null) {
			this.settingReference.set(!this.settingReference.get(ModSettingScreen.guiContext), ModSettingScreen.guiContext);
		}

		this.update();
		GuiModScreen.clicksound();
	}

	public void update() {
		this.button.setText(this.userString());
	}

	public String userString() {
		if (this.settingReference != null) {
			if (this.niceName.length() > 0) {
				return String.format("%s: %s", this.niceName, this.settingReference.get(ModSettingScreen.guiContext) ? this.trueText : this.falseText);
			} else {
				return this.settingReference.get(ModSettingScreen.guiContext) ? this.trueText : this.falseText;
			}
		} else {
			return this.niceName.length() > 0 ? String.format("%s: %s", this.niceName, "no value") : "no value or title";
		}
	}
}
