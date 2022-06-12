package shockahpi;

import net.minecraft.util.NetherTeleporter;
import net.minecraft.world.dimension.Dimension;

import java.util.ArrayList;
import java.util.LinkedList;

public class DimensionBase {
	public static final ArrayList<DimensionBase> LIST = new ArrayList<>();
	public static final LinkedList<Integer> ORDER = new LinkedList<>();
	public final int number;
	public final Class<? extends Dimension> worldProviderClass;
	public final Class<? extends NetherTeleporter> teleporterClass;
	public String name = "Dimension";
	public String soundTrigger = "portal.trigger";
	public String soundTravel = "portal.travel";

	public DimensionBase(int number,
	                     Class<? extends Dimension> worldProviderClass,
	                     Class<? extends NetherTeleporter> teleporterClass) {
		this.number = number;
		this.worldProviderClass = worldProviderClass;
		this.teleporterClass = teleporterClass;
		LIST.add(this);
	}
}
