package guiapi.widget;

import java.util.ArrayList;

import de.matthiasmann.twl.Widget;

public class SingleRowWidget extends Widget {
	public int defaultHeight = 20;
	public int defaultWidth = 150;
	public ArrayList<Integer> heights = new ArrayList<>();
	public ArrayList<Widget> widgets = new ArrayList<>();
	public ArrayList<Integer> widths = new ArrayList<>();
	public int xSpacing = 3;

	public SingleRowWidget(int defWidth, int defHeight, Widget... widgets) {
		this.setTheme("");
		this.defaultWidth = defWidth;
		this.defaultHeight = defHeight;

		for (Widget widget : widgets) {
			this.add(widget);
		}
	}

	@Override
	public void add(Widget widget) {
		this.add(widget, this.defaultWidth, this.defaultHeight);
	}

	public void add(Widget widget, int width, int height) {
		this.widgets.add(widget);
		this.heights.add(height);
		this.widths.add(width);
		super.add(widget);
	}

	private int getHeight(int idx) {
		return this.heights.get(idx) >= 0 ? this.heights.get(idx) : this.widgets.get(idx).getPreferredHeight();
	}

	@Override
	public int getPreferredHeight() {
		int maxHeights = 0;

		for (int i = 0; i < this.heights.size(); ++i) {
			if (this.getHeight(i) > maxHeights) {
				maxHeights = this.getHeight(i);
			}
		}

		return maxHeights;
	}

	@Override
	public int getPreferredWidth() {
		int totalWidth = (this.widths.size() - 1) * this.xSpacing;
		totalWidth = Math.max(totalWidth, 0);

		for (int i = 0; i < this.widths.size(); ++i) {
			totalWidth += this.getWidth(i);
		}

		return totalWidth;
	}

	private int getWidth(int idx) {
		return this.widths.get(idx) >= 0 ? this.widths.get(idx) : this.widgets.get(idx).getPreferredWidth();
	}

	@Override
	public void layout() {
		int curXPos = 0;

		for (int i = 0; i < this.widgets.size(); ++i) {
			Widget w = this.widgets.get(i);
			w.setPosition(curXPos + this.getX(), this.getY());
			w.setSize(this.getWidth(i), this.getHeight(i));
			curXPos += this.getWidth(i) + this.xSpacing;
		}
	}

	@Override
	public Widget removeChild(int idx) {
		this.widgets.remove(idx);
		this.heights.remove(idx);
		this.widths.remove(idx);
		return super.removeChild(idx);
	}

	@Override
	public boolean removeChild(Widget widget) {
		int idx = this.widgets.indexOf(widget);
		this.widgets.remove(idx);
		this.heights.remove(idx);
		this.widths.remove(idx);
		return super.removeChild(widget);
	}
}
