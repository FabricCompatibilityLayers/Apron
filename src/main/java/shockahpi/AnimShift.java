package shockahpi;

import io.github.betterthanupdates.Legacy;

@Legacy
public class AnimShift extends AnimBase {
	@Legacy
	private final int h;
	@Legacy
	private final int v;

	@Legacy
	public AnimShift(int spriteID, String spritePath, int h, int v) {
		super(spriteID, spritePath);
		this.h = h;
		this.v = v;
		this.getCleanFrame();
	}

	@Legacy
	public void updateTexture() {
		this.animFrame();
		this.copyFrameToArray();
	}

	@Legacy
	public void animFrame() {
		this.shiftFrame(this.h, this.v, true, true);
	}
}
