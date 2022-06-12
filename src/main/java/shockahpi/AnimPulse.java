package shockahpi;

import java.awt.*;

public class AnimPulse extends AnimBase {
	private int animState = 0;
	private int animAdd = 1;
	private final int animMax;
	private final Color color1;
	private final Color color2;

	public AnimPulse(int spriteID, String spritePath, int animMax, Color color1, Color color2) {
		super(spriteID, spritePath);
		this.animMax = animMax;
		this.color1 = color1;
		this.color2 = color2;
	}

	@Override
	public void animFrame() {
		this.animState += this.animAdd;

		if (this.animState == this.animMax || this.animState == 0) {
			this.animAdd *= -1;
		}

		this.drawRect(0, 0, this.size, this.size, merge(this.color1, this.color2, (float)this.animState / (float)this.animMax), this.mdBlend);
	}
}
