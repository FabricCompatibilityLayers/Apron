package guiapi.widget;

import java.util.ArrayList;

import de.matthiasmann.twl.Widget;

public abstract class SettingWidget extends Widget {
	public static ArrayList<SettingWidget> all = new ArrayList<>();
	public String niceName;

	public static void updateAll() {
		for (SettingWidget settingWidget : all) {
			settingWidget.update();
		}
	}

	public SettingWidget(String nicename) {
		this.niceName = nicename;
		all.add(this);
	}

	@Override
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

	@Override
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
