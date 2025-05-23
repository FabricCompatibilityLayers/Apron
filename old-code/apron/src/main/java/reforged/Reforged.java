package reforged;

import java.util.ArrayList;

import forge.MinecraftForge;
import modloader.ModLoader;
import net.legacyfabric.fabric.api.logger.v1.Logger;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import io.github.betterthanupdates.Legacy;
import io.github.betterthanupdates.apron.Apron;
import io.github.betterthanupdates.apron.api.ApronApi;

@Legacy
public class Reforged {
	private static final ApronApi APRON = ApronApi.getInstance();
	protected static final Logger LOGGER = Apron.getLogger("Reforged");

	private static boolean searchedForIDResolver;
	private static boolean foundIDResolver;
	private static final ArrayList<IReachEntity> reachesEntity = new ArrayList<>();
	public static boolean disableVersionCheckCrash = false;

	public Reforged() {
	}

	public static boolean hasIDResolver() {
		if (!searchedForIDResolver) {
			searchedForIDResolver = true;
			foundIDResolver = ModLoader.isModLoaded("mod_IDResolver");
		}

		return foundIDResolver;
	}

	public static boolean hasSAPI() {
		return true;
	}

	public static void reachAdd(IReachEntity reachEntity) {
		reachesEntity.add(reachEntity);
	}

	public static float reachGetEntityPlayer(PlayerEntity player) {
		ItemStack itemstack = player.inventory.getSelectedItem();

		for (IReachEntity reachEntity : reachesEntity) {
			if (reachEntity.reachEntityItemMatches(itemstack)) {
				return reachEntity.getReachEntity(itemstack);
			}
		}

		return 3.0F;
	}

	public static void versionDetect(String modName, int major, int minor, int revision) {
		if (disableVersionCheckCrash) {
			ReforgedHooks.touch();
			LOGGER.warn("%s: Reforged version detect was called, but strict crashing is disabled. Expected version %d.%d.%d",
					modName, major, minor, revision);
		} else {
			if (major != ReforgedHooks.majorVersion) {
				MinecraftForge.killMinecraft(modName, "Reforged Major Version Mismatch, expecting " + major + ".x.x");
			} else if (minor != ReforgedHooks.minorVersion) {
				if (minor > ReforgedHooks.minorVersion) {
					MinecraftForge.killMinecraft(modName, "Reforged Too Old, need at least " + major + "." + minor + "." + revision);
				} else {
					LOGGER.warn("%s: Reforged minor version mismatch, expecting %d.%d.x, may lead to unexpected behavior",
							modName, major, minor);
				}
			} else if (revision > ReforgedHooks.revisionVersion) {
				MinecraftForge.killMinecraft(modName, "Reforged Too Old, need at least " + major + "." + minor + "." + revision);
			}
		}
	}

	public static void versionDetectStrict(String modName, int major, int minor, int revision) {
		if (disableVersionCheckCrash) {
			ReforgedHooks.touch();
			LOGGER.warn("%s: Reforged version detect was called, but strict crashing is disabled. Expected version %d.%d.%d",
					modName, major, minor, revision);
		} else {
			if (major != ReforgedHooks.majorVersion) {
				MinecraftForge.killMinecraft(modName, "Reforged Major Version Mismatch, expecting " + major + ".x.x");
			} else if (minor != ReforgedHooks.minorVersion) {
				if (minor > ReforgedHooks.minorVersion) {
					MinecraftForge.killMinecraft(modName, "Reforged Too Old, need at least " + major + "." + minor + "." + revision);
				} else {
					MinecraftForge.killMinecraft(modName, "Reforged minor version mismatch, expecting " + major + "." + minor + ".x");
				}
			} else if (revision > ReforgedHooks.revisionVersion) {
				MinecraftForge.killMinecraft(modName, "Reforged Too Old, need at least " + major + "." + minor + "." + revision);
			}
		}
	}
}
