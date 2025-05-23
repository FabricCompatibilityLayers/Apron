package guiapi.widget;

import de.matthiasmann.twl.EditField;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.model.StringModel;
import de.matthiasmann.twl.utils.CallbackSupport;
import guiapi.ModScreen;
import guiapi.ModSettingScreen;
import guiapi.ModSettings;
import guiapi.setting.TextSetting;

public class TextWidget extends SettingWidget implements StringModel {
	private Runnable[] callbacks;
	public Label displayLabel;
	public EditField editField;
	public int setmode = 0;
	public TextSetting settingReference;

	public TextWidget(TextSetting setting, String title) {
		super(title);
		this.setTheme("");
		this.settingReference = setting;
		this.settingReference.displayWidget = this;
		this.editField = new EditField();
		this.add(this.editField);

		if (title != null) {
			this.displayLabel = new Label();
			this.displayLabel.setText(String.format("%s: ", this.niceName));
			this.add(this.displayLabel);
		}

		this.editField.setModel(this);
		this.update();
	}

	@Override
	public void addCallback(Runnable callback) {
		this.callbacks = CallbackSupport.addCallbackToList(this.callbacks, callback, Runnable.class);
	}

	@Override
	public String getValue() {
		return this.settingReference.get();
	}

	@Override
	public void layout() {
		if (this.displayLabel != null) {
			this.displayLabel.setPosition(this.getX(), this.getY() + this.getHeight() / 2 - this.displayLabel.computeTextHeight() / 2);
			this.displayLabel.setSize(this.displayLabel.computeTextWidth(), this.displayLabel.computeTextHeight());
			this.editField.setPosition(this.getX() + this.displayLabel.computeTextWidth(), this.getY());
			this.editField.setSize(this.getWidth() - this.displayLabel.computeTextWidth(), this.getHeight());
		} else {
			this.editField.setPosition(this.getX(), this.getY());
			this.editField.setSize(this.getWidth(), this.getHeight());
		}
	}

	@Override
	public void removeCallback(Runnable callback) {
		this.callbacks = CallbackSupport.removeCallbackFromList(this.callbacks, callback);
	}

	@Override
	public void setValue(String _value) {
		ModScreen.clicksound();
		ModSettings.dbgout(String.format("setvalue %s", this.editField.getText()));

		if (this.setmode <= 0) {
			this.setmode = -1;
			this.settingReference.set(this.editField.getText(), ModSettingScreen.guiContext);
			this.setmode = 0;
		}

		CallbackSupport.fireCallbacks(this.callbacks);
	}

	@Override
	public void update() {
		ModSettings.dbgout("update");

		if (this.displayLabel != null) {
			this.displayLabel.setText(String.format("%s: ", this.niceName));
		}

		if (this.setmode >= 0) {
			this.setmode = 1;
			this.editField.setText(this.settingReference.get(ModSettingScreen.guiContext));
			this.setmode = 0;
		}

		ModSettings.dbgout(String.format("update %s", this.editField.getText()));
	}

	@Override
	public String userString() {
		return String.format("%s: %s", this.niceName, this.settingReference.get(ModSettingScreen.guiContext));
	}
}
