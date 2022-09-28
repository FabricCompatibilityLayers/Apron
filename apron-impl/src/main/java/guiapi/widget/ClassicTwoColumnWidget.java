package guiapi.widget;

import java.util.HashMap;
import java.util.Map;

import de.matthiasmann.twl.Widget;

public class ClassicTwoColumnWidget extends Widget {
	public int childDefaultHeight = 20;
	public int childWidth = 150;
	public int defaultPadding = 4;
	public Map<Widget, Integer> heightOverrideExceptions = new HashMap<>();
	public boolean overrideHeight = true;
	public int splitDistance = 10;
	public int verticalPadding = 0;

	public ClassicTwoColumnWidget(Widget... widgets) {
		for (Widget widget : widgets) {
			this.add(widget);
		}

		this.setTheme("");
	}

	@Override
	public int getPreferredHeight() {
		int totalHeight = this.verticalPadding;

		for (int i = 0; i < this.getNumChildren(); i += 2) {
			Widget w = this.getChild(i);
			Widget w2 = null;

			if (i + 1 != this.getNumChildren()) {
				w2 = this.getChild(i + 1);
			}

			int height = this.childDefaultHeight;

			if (!this.overrideHeight) {
				height = w.getPreferredHeight();
			}

			if (this.heightOverrideExceptions.containsKey(w)) {
				Integer heightSet = this.heightOverrideExceptions.get(w);

				if (heightSet < 1) {
					height = w.getPreferredHeight();
				}

				heightSet = -heightSet;

				if (heightSet != 0 && heightSet > height) {
					height = heightSet;
				}
			}

			if (w2 != null) {
				int temp = height;

				if (!this.overrideHeight) {
					temp = w2.getPreferredHeight();
				}

				if (this.heightOverrideExceptions.containsKey(w2)) {
					Integer heightSet = this.heightOverrideExceptions.get(w2);

					if (heightSet < 1) {
						height = w2.getPreferredHeight();
					}

					heightSet = -heightSet;

					if (heightSet != 0 && heightSet > height) {
						height = heightSet;
					}
				}

				if (temp > height) {
					height = temp;
				}
			}

			totalHeight += height + this.defaultPadding;
		}

		return totalHeight;
	}

	@Override
	public int getPreferredWidth() {
		return this.getParent().getWidth();
	}

	@Override
	public void layout() {
		if (this.getParent().getTheme().equals("scrollpane-notch")) {
			this.verticalPadding = 10;
		}

		int totalHeight = this.verticalPadding;

		for (int i = 0; i < this.getNumChildren(); i += 2) {
			Widget w = this.getChild(i);
			Widget w2 = null;

			try {
				w2 = this.getChild(i + 1);
			} catch (IndexOutOfBoundsException ignored) {
			}

			int height = this.childDefaultHeight;

			if (!this.overrideHeight) {
				height = w.getPreferredHeight();
			}

			if (this.heightOverrideExceptions.containsKey(w)) {
				Integer heightSet = this.heightOverrideExceptions.get(w);

				if (heightSet < 1) {
					height = w.getPreferredHeight();
				}

				heightSet = -heightSet;

				if (heightSet != 0 && heightSet > height) {
					height = heightSet;
				}
			}

			if (w2 != null) {
				int temp = height;

				if (!this.overrideHeight) {
					temp = w2.getPreferredHeight();
				}

				if (this.heightOverrideExceptions.containsKey(w2)) {
					Integer heightSet = this.heightOverrideExceptions.get(w2);

					if (heightSet < 1) {
						height = w2.getPreferredHeight();
					}

					heightSet = -heightSet;

					if (heightSet != 0 && heightSet > height) {
						height = heightSet;
					}
				}

				if (temp > height) {
					height = temp;
				}
			}

			w.setSize(this.childWidth, height);
			w.setPosition(this.getX() + this.getWidth() / 2 - (this.childWidth + this.splitDistance / 2), this.getY() + totalHeight);

			if (w2 != null) {
				w2.setSize(this.childWidth, height);
				w2.setPosition(this.getX() + this.getWidth() / 2 + this.splitDistance / 2, this.getY() + totalHeight);
			}

			totalHeight += height + this.defaultPadding;
		}
	}
}
