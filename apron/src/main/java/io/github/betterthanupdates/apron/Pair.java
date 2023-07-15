package io.github.betterthanupdates.apron;

import org.jetbrains.annotations.ApiStatus;

import java.util.AbstractMap;

/**
 * A pair is a common type to use for storing two pieces of data together.<br>
 * Pairs are effectively the exact same thing as {@link AbstractMap.SimpleEntry}.
 *
 * @param <L> The type of data to store on the left.
 * @param <R> The type of data to store on the right.
 * @see AbstractMap.SimpleEntry
 */
@ApiStatus.Internal
public class Pair<L, R> extends AbstractMap.SimpleEntry<L, R> {
	public Pair(L left, R right) {
		super(left, right);
	}

	/**
	 * @return {@link #getKey()}
	 * @see #getKey()
	 */
	public L getLeft() {
		return this.getKey();
	}

	/**
	 * @return {@link #getValue()} ()}
	 * @see #getValue()
	 */
	public R getRight() {
		return this.getValue();
	}

	@Override
	public int hashCode() {
		return this.getLeft().hashCode() ^ this.getRight().hashCode();
	}

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
