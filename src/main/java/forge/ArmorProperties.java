/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.1.
 */

package forge;

import io.github.betterthanupdates.Legacy;

@Legacy
public class ArmorProperties {
	@Legacy
	public int damageRemove = 0;
	@Legacy
	public boolean allowRegularComputation = false;

	@Legacy
	public ArmorProperties() {
	}

	@Legacy
	public ArmorProperties(int damageRemove, boolean allowRegularComputation) {
		this.damageRemove = damageRemove;
		this.allowRegularComputation = allowRegularComputation;
	}
}
