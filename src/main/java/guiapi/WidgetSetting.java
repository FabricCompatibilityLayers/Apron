package guiapi;

import java.util.ArrayList;

import de.matthiasmann.twl.Widget;

public abstract class WidgetSetting extends Widget {
	public static ArrayList<WidgetSetting> all = new ArrayList<>();
	public String niceName;

	public static void updateAll() {
		for (WidgetSetting widgetSetting : all) {
			widgetSetting.update();
		}
	}

	public WidgetSetting(String nicename) {
		this.niceName = nicename;
		all.add(this);
	}

	public void add(Widget child) {
		String T = child.getTheme();

		if (T.length() == 0) {
			child.setTheme("/-defaults");
		} else if (T.charAt(0) != '/') {
			child.setTheme("/" + T);
		}

		super.add(child);
	}

	public abstract void addCallback(Runnable runnable);

	public void layout() {
		for (int i = 0; i < this.getNumChildren(); ++i) {
			Widget w = this.getChild(i);
			w.setPosition(this.getX(), this.getY());
			w.setSize(this.getWidth(), this.getHeight());
		}
	}

	public abstract void removeCallback(Runnable runnable);

	public abstract void update();

	public abstract String userString();
}
