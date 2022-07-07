package guiapi;

import de.matthiasmann.twl.model.SimpleFloatModel;

public class WidgetFloat extends WidgetSetting implements Runnable {
	public int decimalPlaces;
	public WidgetSlider slider;
	public SettingFloat settingReference;

	public WidgetFloat(SettingFloat setting, String title) {
		this(setting, title, 2);
	}

	public WidgetFloat(SettingFloat setting, String title, int _decimalPlaces) {
		super(title);
		this.setTheme("");
		this.decimalPlaces = _decimalPlaces;
		this.settingReference = setting;
		this.settingReference.displayWidget = this;
		SimpleFloatModel smodel = new SimpleFloatModel(this.settingReference.minimumValue, this.settingReference.maximumValue, this.settingReference.get());
		smodel.addCallback(this);
		this.slider = new WidgetSlider(smodel);

		if (this.settingReference.stepValue > 0.0F && this.settingReference.stepValue <= this.settingReference.maximumValue) {
			this.slider.setStepSize(this.settingReference.stepValue);
		}

		this.slider.setFormat(String.format("%s: %%.%df", this.niceName, this.decimalPlaces));
		this.add(this.slider);
		this.update();
	}

	public void run() {
		this.settingReference.set(this.slider.getValue(), ModSettingScreen.guiContext);
	}

	public void update() {
		this.slider.setValue(this.settingReference.get(ModSettingScreen.guiContext));
		this.slider.setFormat(String.format("%s: %%.%df", this.niceName, this.decimalPlaces));
	}

	public String userString() {
		String l = String.format("%02d", this.decimalPlaces);
		return String.format("%s: %." + l + "f", this.niceName, this.settingReference);
	}

	public void addCallback(Runnable paramRunnable) {
		this.slider.getModel().addCallback(paramRunnable);
	}

	public void removeCallback(Runnable paramRunnable) {
		this.slider.getModel().removeCallback(paramRunnable);
	}
}
