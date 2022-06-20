package modloadermp;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.SERVER)
public class Pair<L, R> {
	private final L left;
	private final R right;

	public Pair(L obj, R obj1) {
		this.left = obj;
		this.right = obj1;
	}

	public L getLeft() {
		return this.left;
	}

	public R getRight() {
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
			Pair<?, ?> pair = (Pair<?, ?>) obj;
			return this.left.equals(pair.getLeft()) && this.right.equals(pair.getRight());
		}
	}
}
