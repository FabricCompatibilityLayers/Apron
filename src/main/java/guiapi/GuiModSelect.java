package guiapi;

import net.minecraft.client.gui.screen.Screen;

public class GuiModSelect extends GuiModScreen {
	private static void selectScreen(Integer i) {
		GuiModScreen.show(ModSettingScreen.modScreens.get(i).theWidget);
		GuiModScreen.clicksound();
	}

	public GuiModSelect(Screen screen) {
		super(screen);
		WidgetClassicTwocolumn w = new WidgetClassicTwocolumn();
		w.verticalPadding = 10;

		for (int i = 0; i < ModSettingScreen.modScreens.size(); ++i) {
			ModSettingScreen m = ModSettingScreen.modScreens.get(i);
			w.add(GuiApiHelper.makeButton(m.buttonTitle, "selectScreen", GuiModSelect.class, false, new Class[]{Integer.class}, i));
		}

		WidgetSimplewindow mainwidget = new WidgetSimplewindow(w, "Select a Mod");
		mainwidget.hPadding = 0;
		mainwidget.mainWidget.setTheme("scrollpane-notch");
		this.mainwidget = mainwidget;
	}
}
