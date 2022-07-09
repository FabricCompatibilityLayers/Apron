package guiapi.widget;

import de.matthiasmann.twl.model.SimpleFloatModel;
import guiapi.ModSettingScreen;
import guiapi.setting.FloatSetting;

public class FloatWidget extends SettingWidget implements Runnable {
	public int decimalPlaces;
	public SliderWidget slider;
	public FloatSetting settingReference;

	public FloatWidget(FloatSetting setting, String title) {
		this(setting, title, 2);
	}

	public FloatWidget(FloatSetting setting, String title, int _decimalPlaces) {
		super(title);
		this.setTheme("");
		this.decimalPlaces = _decimalPlaces;
		this.settingReference = setting;
		this.settingReference.displayWidget = this;
		SimpleFloatModel smodel = new SimpleFloatModel(this.settingReference.minimumValue, this.settingReference.maximumValue, this.settingReference.get());
		smodel.addCallback(this);
		this.slider = new SliderWidget(smodel);

		if (this.settingReference.stepValue > 0.0F && this.settingReference.stepValue <= this.settingReference.maximumValue) {
			this.slider.setStepSize(this.settingReference.stepValue);
		}

		this.slider.setFormat(String.format("%s: %%.%df", this.niceName, this.decimalPlaces));
		this.add(this.slider);
		this.update();
	}

	@Override
	public void run() {
		this.settingReference.set(this.slider.getValue(), ModSettingScreen.guiContext);
	}

	@Override
	public void update() {
		this.slider.setValue(this.settingReference.get(ModSettingScreen.guiContext));
		this.slider.setFormat(String.format("%s: %%.%df", this.niceName, this.decimalPlaces));
	}

	@Override
	public String userString() {
		String l = String.format("%02d", this.decimalPlaces);
		return String.format("%s: %." + l + "f", this.niceName, this.settingReference);
	}

	@Override
	public void addCallback(Runnable paramRunnable) {
		this.slider.getModel().addCallback(paramRunnable);
	}

	@Override
	public void removeCallback(Runnable paramRunnable) {
		this.slider.getModel().removeCallback(paramRunnable);
	}
}
