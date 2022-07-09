package guiapi.widget;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.ScrollPane;
import de.matthiasmann.twl.Widget;
import de.matthiasmann.twl.model.SimpleButtonModel;
import guiapi.GuiApiHelper;

public class SimpleWindowWidget extends Widget {
	public Button backButton = new Button();
	public SingleRowWidget buttonBar = new SingleRowWidget(0, 0);
	public int hPadding = 30;
	public Widget mainWidget = new Widget();
	public Label titleWidget = new Label();
	public int vBottomPadding = 40;
	public int vTopPadding = 30;

	public SimpleWindowWidget() {
		this(new ClassicTwoColumnWidget(), "", true);
	}

	public SimpleWindowWidget(Widget w) {
		this(w, "", true);
	}

	public SimpleWindowWidget(Widget w, String s) {
		this(w, s, true);
	}

	public SimpleWindowWidget(Widget w, String s, Boolean showbackButton) {
		ScrollPane mainWidget_ = new ScrollPane(w);
		mainWidget_.setFixed(ScrollPane.Fixed.HORIZONTAL);
		this.mainWidget = mainWidget_;
		this.setTheme("");
		this.init(showbackButton, s);
	}

	public void init(Boolean showBack, String titleText) {
		if (titleText != null) {
			this.titleWidget = new Label(titleText);
			this.add(this.titleWidget);
		} else {
			this.vTopPadding = 10;
		}

		if (showBack) {
			this.backButton = new Button(new SimpleButtonModel());
			this.backButton.getModel().addActionCallback(GuiApiHelper.backModAction);
			this.backButton.setText("Back");
			this.buttonBar = new SingleRowWidget(200, 20, this.backButton);
			this.add(this.buttonBar);
		} else {
			this.vBottomPadding = 0;
		}

		this.add(this.mainWidget);
	}

	@Override
	public void layout() {
		if (this.buttonBar != null) {
			this.buttonBar.setSize(this.buttonBar.getPreferredWidth(), this.buttonBar.getPreferredHeight());
			this.buttonBar
					.setPosition(this.getWidth() / 2 - this.buttonBar.getPreferredWidth() / 2, this.getHeight() - (this.buttonBar.getPreferredHeight() + 4));
		}

		if (this.titleWidget != null) {
			this.titleWidget.setPosition(this.getWidth() / 2 - this.titleWidget.computeTextWidth() / 2, 10);
			this.titleWidget.setSize(this.titleWidget.computeTextWidth(), this.titleWidget.computeTextHeight());
		}

		this.mainWidget.setPosition(this.hPadding, this.vTopPadding);
		this.mainWidget.setSize(this.getWidth() - this.hPadding * 2, this.getHeight() - (this.vTopPadding + this.vBottomPadding));
	}
}
