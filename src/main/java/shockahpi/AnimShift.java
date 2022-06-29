package shockahpi;

import io.github.betterthanupdates.Legacy;

@Legacy
public class AnimShift extends AnimBase {
	private final int x;
	private final int y;

	public AnimShift(int spriteID, String spritePath, int x, int y) {
		super(spriteID, spritePath);
		this.x = x;
		this.y = y;
		this.getCleanFrame();
	}

	@Override
	public void updateTexture() {
		this.animFrame();
		this.copyFrameToArray();
	}

	@Override
	public void animFrame() {
		this.shiftFrame(this.x, this.y, true, true);
	}
}
