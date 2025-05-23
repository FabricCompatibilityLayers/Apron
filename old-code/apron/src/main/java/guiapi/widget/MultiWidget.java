package guiapi.widget;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.model.SimpleButtonModel;
import guiapi.ModScreen;
import guiapi.ModSettingScreen;
import guiapi.ModSettings;
import guiapi.setting.MultiSetting;

public class MultiWidget extends SettingWidget implements Runnable {
	public Button button;
	public MultiSetting value;

	public MultiWidget(MultiSetting setting, String title) {
		super(title);
		this.setTheme("");
		this.value = setting;
		this.value.displayWidget = this;
		SimpleButtonModel model = new SimpleButtonModel();
		this.button = new Button(model);
		model.addActionCallback(this);
		this.add(this.button);
		this.update();
	}

	@Override
	public void addCallback(Runnable paramRunnable) {
		this.button.getModel().addActionCallback(paramRunnable);
	}

	@Override
	public void removeCallback(Runnable paramRunnable) {
		this.button.getModel().removeActionCallback(paramRunnable);
	}

	@Override
	public void run() {
		this.value.next(ModSettingScreen.guiContext);
		this.update();
		ModScreen.clicksound();
	}

	@Override
	public void update() {
		this.button.setText(this.userString());
		ModSettings.dbgout("multi update " + this.userString());
	}

	@Override
	public String userString() {
		return this.niceName.length() > 0
				? String.format("%s: %s", this.niceName, this.value.getLabel(ModSettingScreen.guiContext))
				: this.value.getLabel(ModSettingScreen.guiContext);
	}
}
