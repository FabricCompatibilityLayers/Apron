package shockahpi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.minecraft.world.World;

import io.github.betterthanupdates.Legacy;

@Legacy
public class GenDeposit {
	private final int blockID;
	private final List<Integer> set1stOn;
	private final List<Integer> setOn;
	private final List<Loc> sides = Arrays.asList(Loc.vecAdjacent());
	private final ArrayList<Loc> check = new ArrayList<>();
	private final ArrayList<Loc> cantSet = new ArrayList<>();
	private ArrayList<Loc> sidesTmp = new ArrayList<>();
	private final World world;
	private final Random rand;

	public GenDeposit(World world, int blockID, Integer[] set1stOn, Integer[] setOn) {
		this.world = world;
		this.blockID = blockID;
		this.set1stOn = Arrays.asList(set1stOn);
		this.setOn = Arrays.asList(setOn);
		this.rand = world.field_214;
	}

	public void gen(int pX, int pY, int pZ, int maxAmount, int maxTries) {
		this.gen(new Loc(pX, pY, pZ), maxAmount, maxTries);
	}

	public void gen(Loc startLoc, int maxAmount, int maxTries) {
		if (this.set1stOn.contains(startLoc.getBlock(this.world))) {
			this.check.add(startLoc);

			while(maxAmount > 0 && maxTries > 0 && !this.check.isEmpty()) {
				--maxTries;
				int i = this.rand.nextInt(this.check.size());
				Loc curLoc = (Loc)this.check.get(i);
				this.check.remove(i);
				this.sidesTmp = new ArrayList(this.sides);
				if (this.setOn.contains(curLoc.getBlock(this.world))) {
					curLoc.setBlock(this.world, this.blockID);
					this.cantSet.add(curLoc);
					this.check.addAll(Arrays.asList(curLoc.adjacent()));
					--maxAmount;
				}

				while(!this.sidesTmp.isEmpty()) {
					i = this.rand.nextInt(this.sidesTmp.size());
					Loc side = (Loc)this.sidesTmp.get(i);
					this.sidesTmp.remove(i);
					if (this.cantSet.contains(side)) {
					}
				}
			}

		}
	}

	public int getGoodY(int pX, int pY, int pZ) {
		int off = 0;
		if (this.set1stOn.contains(this.world.getBlockId(pX, pY, pZ))) {
			return pY;
		} else {
			do {
				++off;
				if (pY + off <= 127 && this.set1stOn.contains(this.world.getBlockId(pX, pY + off, pZ))) {
					return pY + off;
				}

				if (pY - off >= 0 && this.set1stOn.contains(this.world.getBlockId(pX, pY - off, pZ))) {
					return pY - off;
				}
			} while(pY + off <= 127 || pY - off >= 0);

			return -1;
		}
	}
}
