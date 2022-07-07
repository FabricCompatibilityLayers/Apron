package guiapi;

import de.matthiasmann.twl.EditField;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.model.StringModel;
import de.matthiasmann.twl.utils.CallbackSupport;

public class WidgetText extends WidgetSetting implements StringModel {
	private Runnable[] callbacks;
	public Label displayLabel;
	public EditField editField;
	public int setmode = 0;
	public SettingText settingReference;

	public WidgetText(SettingText setting, String title) {
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

	public void addCallback(Runnable callback) {
		this.callbacks = CallbackSupport.addCallbackToList(this.callbacks, callback, Runnable.class);
	}

	public String getValue() {
		return this.settingReference.get();
	}

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

	public void removeCallback(Runnable callback) {
		this.callbacks = CallbackSupport.removeCallbackFromList(this.callbacks, callback);
	}

	public void setValue(String _value) {
		GuiModScreen.clicksound();
		ModSettings.dbgout(String.format("setvalue %s", this.editField.getText()));

		if (this.setmode <= 0) {
			this.setmode = -1;
			this.settingReference.set(this.editField.getText(), ModSettingScreen.guiContext);
			this.setmode = 0;
		}

		CallbackSupport.fireCallbacks(this.callbacks);
	}

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

	public String userString() {
		return String.format("%s: %s", this.niceName, this.settingReference.get(ModSettingScreen.guiContext));
	}
}
