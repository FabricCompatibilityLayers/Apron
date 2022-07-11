package guiapi.widget;

import de.matthiasmann.twl.model.SimpleFloatModel;
import guiapi.ModSettingScreen;
import guiapi.ModSettings;
import guiapi.setting.IntSetting;

public class IntWidget extends SettingWidget implements Runnable {
	public IntSetting settingReference;
	public SliderWidget slider;

	public IntWidget(IntSetting setting, String title) {
		super(title);
		this.setTheme("");
		this.settingReference = setting;
		this.settingReference.displayWidget = this;
		SimpleFloatModel simpleModel = new SimpleFloatModel(
				(float) this.settingReference.minimumValue, (float) this.settingReference.maximumValue, (float) this.settingReference.get().intValue()
		);
		this.slider = new SliderWidget(simpleModel);
		this.slider.setFormat(String.format("%s: %%.0f", this.niceName));

		if (this.settingReference.stepValue > 1 && this.settingReference.stepValue <= this.settingReference.maximumValue) {
			this.slider.setStepSize((float) this.settingReference.stepValue);
		}

		simpleModel.addCallback(this);
		this.add(this.slider);
		this.update();
	}

	@Override
	public void addCallback(Runnable paramRunnable) {
		this.slider.getModel().addCallback(paramRunnable);
	}

	@Override
	public void removeCallback(Runnable paramRunnable) {
		this.slider.getModel().removeCallback(paramRunnable);
	}

	@Override
	public void run() {
		ModSettings.dbgout("run " + (int) this.slider.getValue());
		this.settingReference.set((int) this.slider.getValue(), ModSettingScreen.guiContext);
	}

	@Override
	public void update() {
		this.slider.setValue((float) this.settingReference.get(ModSettingScreen.guiContext).intValue());
		this.slider.setFormat(String.format("%s: %%.0f", this.niceName));
		ModSettings.dbgout("update " + this.settingReference.get(ModSettingScreen.guiContext) + " -> " + (int) this.slider.getValue());
	}

	@Override
	public String userString() {
		return String.format("%s: %.0d", this.niceName, this.settingReference.get(ModSettingScreen.guiContext));
	}
}
