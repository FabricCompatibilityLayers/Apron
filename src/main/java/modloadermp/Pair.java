package modloadermp;

import java.util.AbstractMap;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import io.github.betterthanupdates.Legacy;

/**
 * A pair is a common type to use for storing two pieces of data together.<br>
 * Pairs are effectively the exact same thing as {@link java.util.AbstractMap.SimpleEntry}.
 *
 * @param <L> The type of data to store on the left.
 * @param <R> The type of data to store on the right.
 * @see java.util.AbstractMap.SimpleEntry
 */
@Legacy
@Deprecated
@Environment(EnvType.SERVER)
public class Pair<L, R> extends AbstractMap.SimpleEntry<L, R> {
	@Legacy
	public Pair(L left, R right) {
		super(left, right);
	}

	/**
	 * @return {@link #getKey()}
	 * @see #getKey()
	 */
	@Legacy
	public L getLeft() {
		return this.getKey();
	}

	/**
	 * @return {@link #getValue()} ()}
	 * @see #getValue()
	 */
	@Legacy
	public R getRight() {
		return this.getValue();
	}

	@Legacy
	@Override
	public int hashCode() {
		return this.getLeft().hashCode() ^ this.getRight().hashCode();
	}

	@Legacy
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Pair)) {
			return false;
		} else {
			Pair<?, ?> pair = (Pair<?, ?>) other;
			return this.getLeft().equals(pair.getLeft()) && this.getRight().equals(pair.getRight());
		}
	}
}
