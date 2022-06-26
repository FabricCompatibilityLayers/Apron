/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.1.
 */

package forge;

import io.github.betterthanupdates.Legacy;

@Legacy
public class ArmorProperties {
	public int damageRemove = 0;
	public boolean allowRegularComputation = false;

	public ArmorProperties() {
	}

	public ArmorProperties(int damageRemove, boolean allowRegularComputation) {
		this.damageRemove = damageRemove;
		this.allowRegularComputation = allowRegularComputation;
	}
}
