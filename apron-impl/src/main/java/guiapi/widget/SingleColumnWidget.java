package guiapi.widget;

import de.matthiasmann.twl.Widget;

public class SingleColumnWidget extends ClassicTwoColumnWidget {
	public SingleColumnWidget(Widget... widgets) {
		super(widgets);
		this.childWidth = 200;
	}

	@Override
	public int getPreferredHeight() {
		int totalHeight = this.verticalPadding;

		for (int i = 0; i < this.getNumChildren(); ++i) {
			Widget widget = this.getChild(i);
			int height = this.childDefaultHeight;

			if (!this.overrideHeight) {
				height = widget.getPreferredHeight();
			}

			if (this.heightOverrideExceptions.containsKey(widget)) {
				Integer heightSet = this.heightOverrideExceptions.get(widget);

				if (heightSet < 1) {
					height = widget.getPreferredHeight();
				}

				heightSet = -heightSet;

				if (heightSet != 0 && heightSet > height) {
					height = heightSet;
				}
			}

			totalHeight += height + this.defaultPadding;
		}

		return totalHeight;
	}

	@Override
	public int getPreferredWidth() {
		return Math.max(this.getParent().getWidth(), this.childWidth);
	}

	@Override
	public void layout() {
		int totalHeight = this.verticalPadding;

		for (int i = 0; i < this.getNumChildren(); ++i) {
			Widget w = this.getChild(i);
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

			w.setSize(this.childWidth, height);
			w.setPosition(this.getX() + this.getWidth() / 2 - w.getWidth() / 2, this.getY() + totalHeight);
			totalHeight += height + this.defaultPadding;
		}
	}
}
