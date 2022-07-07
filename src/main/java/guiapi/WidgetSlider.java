package guiapi;

import de.matthiasmann.twl.ValueAdjusterFloat;
import de.matthiasmann.twl.model.FloatModel;

public class WidgetSlider extends ValueAdjusterFloat {
	public WidgetSlider(FloatModel f) {
		super(f);
	}

	public void startEdit() {
		this.cancelEdit();
	}
}
