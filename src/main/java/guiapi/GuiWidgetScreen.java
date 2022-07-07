package guiapi;

import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.Widget;
import de.matthiasmann.twl.input.lwjgl.LWJGLInput;
import de.matthiasmann.twl.renderer.lwjgl.LWJGLRenderer;
import de.matthiasmann.twl.theme.ThemeManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ScreenScaler;

public class GuiWidgetScreen extends Widget {
	public static GuiWidgetScreen instance;
	public static int screenheight;
	public static int screenwidth;
	public Widget currentWidget = null;
	public GUI gui = null;
	public Minecraft minecraftInstance;
	public LWJGLRenderer renderer = null;
	public ScreenScaler screenSize = null;
	public ThemeManager theme = null;

	public static GuiWidgetScreen getInstance() {
		if (instance == null) {
			try {
				instance = new GuiWidgetScreen();
				instance.renderer = new LWJGLRenderer();
				String themename = "twlGuiTheme.xml";
				instance.gui = new GUI(instance, instance.renderer, new LWJGLInput());
				ModSettings.dbgout(GuiWidgetScreen.class.getClassLoader().getResource(themename).toString());
				instance.theme = ThemeManager.createThemeManager(GuiWidgetScreen.class.getClassLoader().getResource(themename), instance.renderer);

				if (instance.theme == null) {
					throw new RuntimeException("I don't think you installed the theme correctly ...");
				}

				instance.setTheme("");
				instance.gui.applyTheme(instance.theme);
				instance.minecraftInstance = ModSettings.getMcinst();
				instance.screenSize = new ScreenScaler(
						instance.minecraftInstance.options, instance.minecraftInstance.actualWidth, instance.minecraftInstance.actualHeight
				);
			} catch (Throwable var2) {
				var2.printStackTrace();
				RuntimeException e2 = new RuntimeException("error loading theme");
				e2.initCause(var2);
				throw e2;
			}
		}

		return instance;
	}

	public GuiWidgetScreen() {
	}

	public void layout() {
		this.screenSize = new ScreenScaler(this.minecraftInstance.options, this.minecraftInstance.actualWidth, this.minecraftInstance.actualHeight);

		if (this.currentWidget != null) {
			screenwidth = this.screenSize.getScaledWidth();
			screenheight = this.screenSize.getScaledHeight();
			this.currentWidget.setSize(screenwidth, screenheight);
			this.currentWidget.setPosition(0, 0);
		}
	}

	public void resetScreen() {
		this.removeAllChildren();
		this.currentWidget = null;
	}

	public void setScreen(Widget widget) {
		this.gui.resyncTimerAfterPause();
		this.gui.clearKeyboardState();
		this.gui.clearMouseState();
		this.removeAllChildren();
		this.add(widget);
		this.currentWidget = widget;
	}
}
