package guiapi;

import guiapi.widget.ClassicTwoColumnWidget;
import guiapi.widget.SimpleWindowWidget;

import net.minecraft.client.gui.screen.Screen;

public class ModSelect extends ModScreen {
	private static void selectScreen(Integer i) {
		ModScreen.show(ModSettingScreen.modScreens.get(i).theWidget);
		ModScreen.clicksound();
	}

	public ModSelect(Screen screen) {
		super(screen);
		ClassicTwoColumnWidget w = new ClassicTwoColumnWidget();
		w.verticalPadding = 10;

		for (int i = 0; i < ModSettingScreen.modScreens.size(); ++i) {
			ModSettingScreen m = ModSettingScreen.modScreens.get(i);
			w.add(GuiApiHelper.makeButton(m.buttonTitle, "selectScreen", ModSelect.class, false, new Class[]{Integer.class}, i));
		}

		SimpleWindowWidget mainWidget = new SimpleWindowWidget(w, "Select a Mod");
		mainWidget.hPadding = 0;
		mainWidget.mainWidget.setTheme("scrollpane-notch");
		this.mainwidget = mainWidget;
	}
}
