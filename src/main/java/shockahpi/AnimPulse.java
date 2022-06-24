package shockahpi;

import java.awt.*;

import io.github.betterthanupdates.Legacy;

@Legacy
public class AnimPulse extends AnimBase {
	@Legacy
	private int animState = 0;
	@Legacy
	private int animAdd = 1;
	@Legacy
	private final int animMax;
	@Legacy
	private final Color c1;
	@Legacy
	private final Color c2;

	@Legacy
	public AnimPulse(int spriteID, String spritePath, int animMax, Color c1, Color c2) {
		super(spriteID, spritePath);
		this.animMax = animMax;
		this.c1 = c1;
		this.c2 = c2;
	}

	@Legacy
	public void animFrame() {
		this.animState += this.animAdd;

		if (this.animState == this.animMax || this.animState == 0) {
			this.animAdd *= -1;
		}

		this.drawRect(0, 0, this.size, this.size, merge(this.c1, this.c2, (float) this.animState / (float) this.animMax), this.mdBlend);
	}
}
