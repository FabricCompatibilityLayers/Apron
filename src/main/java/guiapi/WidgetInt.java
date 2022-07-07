package guiapi;

import de.matthiasmann.twl.model.SimpleFloatModel;

public class WidgetInt extends WidgetSetting implements Runnable {
	public SettingInt settingReference;
	public WidgetSlider slider;

	public WidgetInt(SettingInt setting, String title) {
		super(title);
		this.setTheme("");
		this.settingReference = setting;
		this.settingReference.displayWidget = this;
		SimpleFloatModel smodel = new SimpleFloatModel(
				(float) this.settingReference.minimumValue, (float) this.settingReference.maximumValue, (float) this.settingReference.get().intValue()
		);
		this.slider = new WidgetSlider(smodel);
		this.slider.setFormat(String.format("%s: %%.0f", this.niceName));

		if (this.settingReference.stepValue > 1 && this.settingReference.stepValue <= this.settingReference.maximumValue) {
			this.slider.setStepSize((float) this.settingReference.stepValue);
		}

		smodel.addCallback(this);
		this.add(this.slider);
		this.update();
	}

	public void addCallback(Runnable paramRunnable) {
		this.slider.getModel().addCallback(paramRunnable);
	}

	public void removeCallback(Runnable paramRunnable) {
		this.slider.getModel().removeCallback(paramRunnable);
	}

	public void run() {
		ModSettings.dbgout("run " + (int) this.slider.getValue());
		this.settingReference.set((int) this.slider.getValue(), ModSettingScreen.guiContext);
	}

	public void update() {
		this.slider.setValue((float) this.settingReference.get(ModSettingScreen.guiContext).intValue());
		this.slider.setFormat(String.format("%s: %%.0f", this.niceName));
		ModSettings.dbgout("update " + this.settingReference.get(ModSettingScreen.guiContext) + " -> " + (int) this.slider.getValue());
	}

	public String userString() {
		return String.format("%s: %.0d", this.niceName, this.settingReference.get(ModSettingScreen.guiContext));
	}
}
