package shockahpi;

import io.github.betterthanupdates.Legacy;

@Legacy
public class BlockHarvestPower {
	public final int blockID;
	public final float percentage;

	public BlockHarvestPower(int blockID, float percentage) {
		this.blockID = blockID;
		this.percentage = percentage;
	}
}
