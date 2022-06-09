package io.github.betterthanupdates.babricated;

import fr.catcore.modremapperapi.api.ModRemapper;
import fr.catcore.modremapperapi.api.RemapLibrary;
import fr.catcore.modremapperapi.remapping.RemapUtil;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.tinyremapper.TinyRemapper;

import java.util.*;

public class BabricatedModRemapper implements ModRemapper {
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
						new RemapLibrary(
								BabricatedForge.MODLOADER_URL,
								new ArrayList<>(),
								"modloader.zip"
						),
						new RemapLibrary(
								BabricatedForge.MODLOADERMP_CLIENT_URL,
								new ArrayList<>(),
								"modloadermp-client.zip"
						),
						new RemapLibrary(
								BabricatedForge.FORGE_CLIENT_URL,
								new ArrayList<>(),
								"forge-client.zip"
						)
				};
				break;
			case SERVER:
				libraries = new RemapLibrary[] {
						new RemapLibrary(
								BabricatedForge.MODLOADERMP_SERVER_URL,
								new ArrayList<>(),
								"modloadermp-server.zip"
						),
						new RemapLibrary(
								BabricatedForge.FORGE_SERVER_URL,
								new ArrayList<>(),
								"forge-server.zip"
						)
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
		list.add("ModLoader", "modloader/ModLoader");
	}

	@Override
	public Optional<TinyRemapper.ApplyVisitorProvider> getPostRemappingVisitor() {
		return Optional.empty();
	}
}
