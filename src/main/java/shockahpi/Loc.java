package shockahpi;

import java.util.ArrayList;

import net.minecraft.entity.BlockEntity;
import net.minecraft.util.Vec3i;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import io.github.betterthanupdates.Legacy;

@SuppressWarnings("unused")
@Legacy
public class Loc {
	@Legacy
	public final double x;
	@Legacy
	public final double y;
	@Legacy
	public final double z;

	@Legacy
	public Loc() {
		this(0, 0, 0);
	}

	@Legacy
	public Loc(int x, int z) {
		this(x, 0, z);
	}

	@Legacy
	public Loc(int x, int y, int z) {
		this((double) x, (double) y, (double) z);
	}

	@Legacy
	public Loc(double x, double z) {
		this(x, 0.0, z);
	}

	@Legacy
	public Loc(World world) {
		this(world.getSpawnPosition().x, world.getSpawnPosition().y, world.getSpawnPosition().z);
	}

	@Legacy
	public Loc(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Legacy
	public int x() {
		return (int) this.x;
	}

	@Legacy
	public int y() {
		return (int) this.y;
	}

	@Legacy
	public int z() {
		return (int) this.z;
	}

	@Legacy
	public Loc add(int x, int y, int z) {
		return new Loc(this.x + (double) x, this.y + (double) y, this.z + (double) z);
	}

	@Legacy
	public Loc add(double x, double y, double z) {
		return new Loc(this.x + x, this.y + y, this.z + z);
	}

	@Legacy
	public Loc add(Loc other) {
		return new Loc(this.x + other.x, this.y + other.y, this.z + other.z);
	}

	@Legacy
	public Loc subtract(int x, int y, int z) {
		return new Loc(this.x - (double) x, this.y - (double) y, this.z - (double) z);
	}

	@Legacy
	public Loc subtract(double x, double y, double z) {
		return new Loc(this.x - x, this.y - y, this.z - z);
	}

	@Legacy
	public Loc subtract(Loc other) {
		return new Loc(this.x - other.x, this.y - other.y, this.z - other.z);
	}

	@Legacy
	public Loc multiply(double xMult, double yMult, double zMult) {
		return new Loc(this.x * xMult, this.y * yMult, this.z * zMult);
	}

	@Legacy
	public Loc getSide(int side) {
		if (side == 0) {
			return new Loc(this.x, this.y - 1.0, this.z);
		} else if (side == 1) {
			return new Loc(this.x, this.y + 1.0, this.z);
		} else if (side == 2) {
			return new Loc(this.x, this.y, this.z - 1.0);
		} else if (side == 3) {
			return new Loc(this.x, this.y, this.z + 1.0);
		} else if (side == 4) {
			return new Loc(this.x - 1.0, this.y, this.z);
		} else {
			return side == 5 ? new Loc(this.x + 1.0, this.y, this.z) : this;
		}
	}

	@Legacy
	public boolean equals(Object other) {
		if (other instanceof Loc) {
			Loc otherLoc = (Loc) other;
			return this.x == otherLoc.x && this.y == otherLoc.y && this.z == otherLoc.z;
		} else {
			return false;
		}
	}

	@Legacy
	public String toString() {
		return "(" + this.x + "," + this.y + "," + this.z + ")";
	}

	@Legacy
	public int distSimple(Loc other) {
		return (int) (Math.abs(this.x - other.x) + Math.abs(this.y - other.y) + Math.abs(this.z - other.z));
	}

	@Legacy
	public double distAdv(Loc other) {
		return Math.sqrt(Math.pow(this.x - other.x, 2.0) + Math.pow(this.y - other.y, 2.0) + Math.pow(this.z - other.z, 2.0));
	}

	@Legacy
	public static Loc[] vecAdjacent() {
		return new Loc[] {new Loc(0, 0, 1), new Loc(0, 0, -1), new Loc(0, 1, 0), new Loc(0, -1, 0), new Loc(1, 0, 0), new Loc(-1, 0, 0)};
	}

	@Legacy
	public Loc[] adjacent() {
		Loc[] array = vecAdjacent();

		for (int i = 0; i < array.length; ++i) {
			array[i] = this.add(array[i]);
		}

		return array;
	}

	@Legacy
	public static Loc[] vecAdjacent2D() {
		return new Loc[] {new Loc(0, 1), new Loc(0, -1), new Loc(1, 0), new Loc(-1, 0)};
	}

	@Legacy
	public Loc[] adjacent2D() {
		Loc[] array = vecAdjacent();

		for (int i = 0; i < array.length; ++i) {
			array[i] = this.add(array[i]);
		}

		return array;
	}

	@Legacy
	public static ArrayList<Loc> vecInRadius(int maxR, boolean advanced) {
		ArrayList<Loc> toReturn = new ArrayList<>();
		Loc start = new Loc();

		for (int x = -maxR; x <= maxR; ++x) {
			for (int z = -maxR; z <= maxR; ++z) {
				for (int y = -maxR; y <= maxR; ++y) {
					Loc check = new Loc(x, y, z);
					double dist = advanced ? start.distAdv(check) : (double) start.distSimple(check);

					if (dist <= (double) maxR) {
						toReturn.add(check);
					}
				}
			}
		}

		return toReturn;
	}

	@Legacy
	public ArrayList<Loc> inRadius(int maxR, boolean advanced) {
		ArrayList<Loc> toReturn = new ArrayList<>();

		for (int x = -maxR; x <= maxR; ++x) {
			for (int z = -maxR; z <= maxR; ++z) {
				for (int y = -maxR; y <= maxR; ++y) {
					Loc check = new Loc(x, y, z).add(this);
					double dist = advanced ? this.distAdv(check) : (double) this.distSimple(check);

					if (dist <= (double) maxR) {
						toReturn.add(check);
					}
				}
			}
		}

		return toReturn;
	}

	@Legacy
	public static ArrayList<Loc> vecInRadius2D(int maxR, boolean advanced) {
		ArrayList<Loc> toReturn = new ArrayList<>();
		Loc start = new Loc();

		for (int x = -maxR; x <= maxR; ++x) {
			for (int z = -maxR; z <= maxR; ++z) {
				Loc check = new Loc(x, z);
				double dist = advanced ? start.distAdv(check) : (double) start.distSimple(check);

				if (dist <= (double) maxR) {
					toReturn.add(check);
				}
			}
		}

		return toReturn;
	}

	@Legacy
	public ArrayList<Loc> inRadius2D(int maxR, boolean advanced) {
		ArrayList<Loc> toReturn = new ArrayList<>();

		for (int x = -maxR; x <= maxR; ++x) {
			for (int z = -maxR; z <= maxR; ++z) {
				Loc check = new Loc(x, z).add(this);
				double dist = advanced ? this.distAdv(check) : (double) this.distSimple(check);

				if (dist <= (double) maxR) {
					toReturn.add(check);
				}
			}
		}

		return toReturn;
	}

	@Legacy
	public int getBlock(BlockView blockAc) {
		return blockAc.getBlockId(this.x(), this.y(), this.z());
	}

	@Legacy
	public Loc setBlockNotify(World world, int blockID) {
		world.setBlock(this.x(), this.y(), this.z(), blockID);
		return this;
	}

	@Legacy
	public Loc setBlock(World world, int blockID) {
		world.setBlockInChunk(this.x(), this.y(), this.z(), blockID);
		return this;
	}

	@Legacy
	public int getMeta(BlockView blockAc) {
		return blockAc.getBlockMeta(this.x(), this.y(), this.z());
	}

	@Legacy
	public Loc setMeta(World world, int meta) {
		world.method_223(this.x(), this.y(), this.z(), meta);
		return this;
	}

	@Legacy
	public Loc setMetaNotify(World world, int meta) {
		world.setBlockMeta(this.x(), this.y(), this.z(), meta);
		return this;
	}

	@Legacy
	public Loc setBlockAndMeta(World world, int blockID, int meta) {
		world.setBlockWithMetadata(this.x(), this.y(), this.z(), blockID, meta);
		return this;
	}

	@Legacy
	public Loc setBlockAndMetaNotify(World world, int blockID, int meta) {
		world.placeBlockWithMetaData(this.x(), this.y(), this.z(), blockID, meta);
		return this;
	}

	@Legacy
	public BlockEntity getTileEntity(BlockView blockAc) {
		return blockAc.getBlockEntity(this.x(), this.y(), this.z());
	}

	@Legacy
	public Loc setTileEntity(World world, BlockEntity tileEntity) {
		world.setBlockEntity(this.x(), this.y(), this.z(), tileEntity);
		return this;
	}

	@Legacy
	public Loc removeTileEntity(World world) {
		world.removeBlockEntity(this.x(), this.y(), this.z());
		return this;
	}

	@Legacy
	public int getLight(World world) {
		return world.getLightLevel(this.x(), this.y(), this.z());
	}

	@Legacy
	public Loc notify(World world) {
		world.notifyOfNeighborChange(this.x(), this.y(), this.z(), this.getBlock(world));
		return this;
	}

	@Legacy
	public Loc setSpawnPoint(World world) {
		world.setSpawnPosition(new Vec3i(this.x(), this.y(), this.z()));
		return this;
	}
}
