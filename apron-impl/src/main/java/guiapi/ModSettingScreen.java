package guiapi;

import java.util.ArrayList;

import de.matthiasmann.twl.Widget;
import guiapi.widget.ClassicTwoColumnWidget;
import guiapi.widget.SimpleWindowWidget;
import guiapi.widget.SingleColumnWidget;

public class ModSettingScreen {
	public static String guiContext = "";
	public static ArrayList<ModSettingScreen> modScreens = new ArrayList<>();
	public String buttonTitle;
	public String niceName;
	public Widget theWidget;
	public ClassicTwoColumnWidget widgetColumn;

	public ModSettingScreen(String name) {
		this(name, name);
	}

	public ModSettingScreen(String niceName, String buttonTitle) {
		modScreens.add(this);
		this.buttonTitle = buttonTitle;
		this.niceName = niceName;
		this.widgetColumn = new ClassicTwoColumnWidget();
		this.theWidget = new SimpleWindowWidget(this.widgetColumn, this.niceName);
	}

	public ModSettingScreen(Widget widget, String buttonTitle) {
		modScreens.add(this);
		this.buttonTitle = buttonTitle;
		this.theWidget = widget;
	}

	public void append(Widget newWidget) {
		if (this.widgetColumn != null) {
			this.widgetColumn.add(newWidget);
		} else {
			this.theWidget.add(newWidget);
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
		boolean isSingle = this.widgetColumn instanceof SingleColumnWidget;

		if (isSingle != value) {
			ClassicTwoColumnWidget w2 = value ? new SingleColumnWidget() : new ClassicTwoColumnWidget();

			for (int i = 0; i < this.widgetColumn.getNumChildren(); ++i) {
				w2.add(this.widgetColumn.getChild(i));
			}

			this.widgetColumn = w2;
			this.theWidget = new SimpleWindowWidget(this.widgetColumn, ((SimpleWindowWidget) this.theWidget).titleWidget.getText());
		}
	}
}
