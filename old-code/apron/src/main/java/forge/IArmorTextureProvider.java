/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.1.
 */

package forge;

import io.github.betterthanupdates.Legacy;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Legacy
@Environment(EnvType.CLIENT)
public interface IArmorTextureProvider {
	String getArmorTextureFile();
}
