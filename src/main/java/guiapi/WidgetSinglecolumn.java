package guiapi;

import de.matthiasmann.twl.Widget;

public class WidgetSinglecolumn extends WidgetClassicTwocolumn {
	public WidgetSinglecolumn(Widget... widgets) {
		super(widgets);
		this.childWidth = 200;
	}

	public int getPreferredHeight() {
		int totalheight = this.verticalPadding;

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

			totalheight += height + this.defaultPadding;
		}

		return totalheight;
	}

	public int getPreferredWidth() {
		return Math.max(this.getParent().getWidth(), this.childWidth);
	}

	public void layout() {
		int totalheight = this.verticalPadding;

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
			w.setPosition(this.getX() + this.getWidth() / 2 - w.getWidth() / 2, this.getY() + totalheight);
			totalheight += height + this.defaultPadding;
		}
	}
}
