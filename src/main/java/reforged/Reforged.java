package reforged;

import java.util.ArrayList;

import forge.MinecraftForge;
import io.github.betterthanupdates.Legacy;
import io.github.betterthanupdates.apron.api.ApronApi;
import modloader.ModLoader;

import net.legacyfabric.fabric.api.logger.v1.Logger;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

@Legacy
public class Reforged {
	private static final ApronApi APRON = ApronApi.getInstance();
	protected static final Logger LOGGER = APRON.getLogger("Reforged");

	@Legacy
	private static boolean searchedForIDResolver;
	@Legacy
	private static boolean foundIDResolver;
	@Legacy
	private static boolean searchedForSAPI;
	@Legacy
	private static boolean foundSAPI;
	@Legacy
	private static ArrayList<IReachEntity> reachesEntity = new ArrayList<>();
	@Legacy
	public static boolean disableVersionCheckCrash = false;

	@Legacy
	public Reforged() {
	}

	@Legacy
	public static boolean hasIDResolver() {
		if (!searchedForIDResolver) {
			searchedForIDResolver = true;
			foundIDResolver = ModLoader.isModLoaded("mod_IDResolver");
		}

		return foundIDResolver;
	}

	@Legacy
	public static boolean hasSAPI() {
		if (!searchedForSAPI) {
			searchedForSAPI = true;
			foundSAPI = hasClass("SAPI");
		}

		return foundSAPI;
	}

	@Legacy
	private static boolean hasClass(String s) {
		Class class1 = null;

		try {
			class1 = Class.forName(s);
			return true;
		} catch (ClassNotFoundException var3) {
			return false;
		}
	}

	@Legacy
	public static void reachAdd(IReachEntity ireachentity) {
		reachesEntity.add(ireachentity);
	}

	@Legacy
	public static float reachGetEntityPlayer(PlayerEntity player) {
		ItemStack itemstack = player.inventory.getHeldItem();

		for(IReachEntity ireachentity : reachesEntity) {
			if (ireachentity.reachEntityItemMatches(itemstack)) {
				return ireachentity.getReachEntity(itemstack);
			}
		}

		return 3.0F;
	}

	@Legacy
	public static void versionDetect(String modname, int major, int minor, int revision) {
		if (disableVersionCheckCrash) {
			ReforgedHooks.touch();
			LOGGER.info("%s: Reforged version detect was called, but strict crashing is disabled. Expected version %d.%d.%d",
					modname, major, minor, revision);
		} else {
			if (major != 1) {
				MinecraftForge.killMinecraft(modname, "Reforged Major Version Mismatch, expecting " + major + ".x.x");
			} else if (minor != 0) {
				if (minor > 0) {
					MinecraftForge.killMinecraft(modname, "Reforged Too Old, need at least " + major + "." + minor + "." + revision);
				} else {
					LOGGER.info("%s: Reforged minor version mismatch, expecting %d.%d.x, may lead to unexpected behavior",
							modname, major, minor);
				}
			} else if (revision > 0) {
				MinecraftForge.killMinecraft(modname, "Reforged Too Old, need at least " + major + "." + minor + "." + revision);
			}
		}
	}

	@Legacy
	public static void versionDetectStrict(String modname, int major, int minor, int revision) {
		if (disableVersionCheckCrash) {
			ReforgedHooks.touch();
			LOGGER.info("%s: Reforged version detect was called, but strict crashing is disabled. Expected version %d.%d.%d",
					modname, major, minor, revision);
		} else {
			if (major != 1) {
				MinecraftForge.killMinecraft(modname, "Reforged Major Version Mismatch, expecting " + major + ".x.x");
			} else if (minor != 0) {
				if (minor > 0) {
					MinecraftForge.killMinecraft(modname, "Reforged Too Old, need at least " + major + "." + minor + "." + revision);
				} else {
					MinecraftForge.killMinecraft(modname, "Reforged minor version mismatch, expecting " + major + "." + minor + ".x");
				}
			} else if (revision > 0) {
				MinecraftForge.killMinecraft(modname, "Reforged Too Old, need at least " + major + "." + minor + "." + revision);
			}
		}
	}
}
