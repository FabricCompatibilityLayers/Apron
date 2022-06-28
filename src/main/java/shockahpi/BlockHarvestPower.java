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

	@Override
	public boolean equals(Object other) {
		if (other instanceof BlockHarvestPower) {
			return this.blockID == ((BlockHarvestPower) other).blockID;
		} else if (other instanceof Integer) {
			return this.blockID == (Integer) other;
		}

		return false;
	}
}
