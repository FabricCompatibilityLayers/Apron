package modloadermp;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.SERVER)
public class Pair {
	private final Object left;
	private final Object right;

	public Pair(Object obj, Object obj1) {
		this.left = obj;
		this.right = obj1;
	}

	public Object getLeft() {
		return this.left;
	}

	public Object getRight() {
		return this.right;
	}

	public int hashCode() {
		return this.left.hashCode() ^ this.right.hashCode();
	}

	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		} else if (!(obj instanceof Pair)) {
			return false;
		} else {
			Pair pair = (Pair)obj;
			return this.left.equals(pair.getLeft()) && this.right.equals(pair.getRight());
		}
	}
}
