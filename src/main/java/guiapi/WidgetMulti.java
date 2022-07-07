package guiapi;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.model.SimpleButtonModel;

public class WidgetMulti extends WidgetSetting implements Runnable {
	public Button button;
	public SettingMulti value;

	public WidgetMulti(SettingMulti setting, String title) {
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

	public void addCallback(Runnable paramRunnable) {
		this.button.getModel().addActionCallback(paramRunnable);
	}

	public void removeCallback(Runnable paramRunnable) {
		this.button.getModel().removeActionCallback(paramRunnable);
	}

	public void run() {
		this.value.next(ModSettingScreen.guiContext);
		this.update();
		GuiModScreen.clicksound();
	}

	public void update() {
		this.button.setText(this.userString());
		ModSettings.dbgout("multi update " + this.userString());
	}

	public String userString() {
		return this.niceName.length() > 0
				? String.format("%s: %s", this.niceName, this.value.getLabel(ModSettingScreen.guiContext))
				: this.value.getLabel(ModSettingScreen.guiContext);
	}
}
