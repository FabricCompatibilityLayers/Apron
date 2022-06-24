package shockahpi;

import io.github.betterthanupdates.Legacy;

@Legacy
public class BlockHarvestPower {
	@Legacy
	public final int blockID;
	@Legacy
	public final float percentage;

	@Legacy
	public BlockHarvestPower(int blockID, float percentage) {
		this.blockID = blockID;
		this.percentage = percentage;
	}

	@Legacy
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		} else if (other instanceof BlockHarvestPower) {
			return this.blockID == ((BlockHarvestPower)other).blockID;
		} else if (other instanceof Integer) {
			return this.blockID == (Integer)other;
		} else {
			return false;
		}
	}
}
