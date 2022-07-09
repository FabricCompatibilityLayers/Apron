package guiapi.widget;

import de.matthiasmann.twl.ValueAdjusterFloat;
import de.matthiasmann.twl.model.FloatModel;

public class SliderWidget extends ValueAdjusterFloat {
	public SliderWidget(FloatModel f) {
		super(f);
	}

	@Override
	public void startEdit() {
		this.cancelEdit();
	}
}
