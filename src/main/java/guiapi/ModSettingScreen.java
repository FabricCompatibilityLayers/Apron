package guiapi;

import java.util.ArrayList;

import de.matthiasmann.twl.Widget;

public class ModSettingScreen {
	public static String guiContext = "";
	public static ArrayList<ModSettingScreen> modScreens = new ArrayList<>();
	public String buttonTitle;
	public String niceName;
	public Widget theWidget;
	public WidgetClassicTwocolumn widgetColumn;

	public ModSettingScreen(String name) {
		this(name, name);
	}

	public ModSettingScreen(String nicename, String buttontitle) {
		modScreens.add(this);
		this.buttonTitle = buttontitle;
		this.niceName = nicename;
		this.widgetColumn = new WidgetClassicTwocolumn();
		this.theWidget = new WidgetSimplewindow(this.widgetColumn, this.niceName);
	}

	public ModSettingScreen(Widget widget, String buttontitle) {
		modScreens.add(this);
		this.buttonTitle = buttontitle;
		this.theWidget = widget;
	}

	public void append(Widget newwidget) {
		if (this.widgetColumn != null) {
			this.widgetColumn.add(newwidget);
		} else {
			this.theWidget.add(newwidget);
		}
	}

	public void remove(Widget child) {
		if (this.widgetColumn != null) {
			this.widgetColumn.removeChild(child);
		} else {
			this.theWidget.removeChild(child);
		}
	}

	public void setSingleColumn(Boolean value) {
		boolean isSingle = this.widgetColumn instanceof WidgetSinglecolumn;

		if (isSingle != value) {
			WidgetClassicTwocolumn w2 = value ? new WidgetSinglecolumn() : new WidgetClassicTwocolumn();

			for (int i = 0; i < this.widgetColumn.getNumChildren(); ++i) {
				w2.add(this.widgetColumn.getChild(i));
			}

			this.widgetColumn = w2;
			this.theWidget = new WidgetSimplewindow(this.widgetColumn, ((WidgetSimplewindow) this.theWidget).titleWidget.getText());
		}
	}
}
