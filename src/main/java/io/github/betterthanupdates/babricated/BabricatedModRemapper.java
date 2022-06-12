package io.github.betterthanupdates.babricated;

import fr.catcore.modremapperapi.api.ModRemapper;
import fr.catcore.modremapperapi.api.RemapLibrary;
import fr.catcore.modremapperapi.remapping.RemapUtil;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.tinyremapper.TinyRemapper;
import org.jetbrains.annotations.ApiStatus;

import java.util.*;

@ApiStatus.Internal
public final class BabricatedModRemapper implements ModRemapper {
	@Override
	public String[] getJarFolders() {
		return new String[0];
	}

	@Override
	public RemapLibrary[] getRemapLibraries() {
		RemapLibrary[] libraries = new RemapLibrary[0];
		switch (FabricLoader.getInstance().getEnvironmentType()) {
			case CLIENT:
				libraries = new RemapLibrary[] {
						new RemapLibrary("", new ArrayList<>(), "modloader.zip"),
						new RemapLibrary("", new ArrayList<>(), "modloadermp-client.zip"),
						new RemapLibrary("", new ArrayList<>(), "forge-client.zip"),
						new RemapLibrary("", new ArrayList<>(), "shockahpi.zip")
				};
				break;
			case SERVER:
				libraries = new RemapLibrary[] {
						new RemapLibrary("", new ArrayList<>(), "modloadermp-server.zip"),
						new RemapLibrary("", new ArrayList<>(), "forge-server.zip")
				};
				break;
		}

		return libraries;
	}

	@Override
	public Map<String, List<String>> getExclusions() {
		return new HashMap<>();
	}

	@Override
	public void getMappingList(RemapUtil.MappingList list) {
		// ModLoader mappings
		list.add("BaseMod", "modloader/BaseMod");
		list.add("EntityRendererProxy", "modloader/EntityRendererProxy");
		list.add("MLProp", "modloader/MLProp");
		list.add("ModLoader", "modloader/ModLoader");
		list.add("ModTextureAnimation", "modloader/ModTextureAnimation");
		list.add("ModTextureStatic", "modloader/ModTextureStatic");

		// ModLoaderMP mappings
		list.add("BaseModMp", "modloadermp/BaseModMp");
		list.add("ISpawnable", "modloadermp/ISpawnable");
		list.add("ModLoaderMp", "modloadermp/ModLoaderMp");
		list.add("NetClientHandlerEntity", "modloadermp/NetClientHandlerEntity");
		list.add("Packet230ModLoader", "modloadermp/Packet230ModLoader");

		// ShockAhPI
		// TODO: Rename classes on right side to fit Yarn standards
		list.add("ACPage", "shockahpi/ACPage");
		list.add("AnimBase", "shockahpi/AnimBase");
		list.add("AnimPulse", "shockahpi/AnimPulse");
		list.add("AnimShift", "shockahpi/AnimShift");
		list.add("BlockHarvestPower", "shockahpi/BlockHarvestPower");
		list.add("DimensionBase", "shockahpi/DimensionBase");
		list.add("IInterceptBlockSet", "shockahpi/IInterceptBlockSet");
		list.add("IInterceptHarvest", "shockahpi/IInterceptHarvest");
		list.add("INBT", "shockahpi/INBT");
		list.add("IReachBlock", "shockahpi/IReachBlock");
		list.add("IReachEntity", "shockahpi/IReachEntity");
		list.add("mod_SAPI", "shockahpi/mod_SAPI");
		list.add("SAPI", "shockahpi/SAPI");
	}

	@Override
	public Optional<TinyRemapper.ApplyVisitorProvider> getPostRemappingVisitor() {
		return Optional.empty();
	}
}
