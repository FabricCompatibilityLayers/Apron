package io.github.betterthanupdates.babricated;

import fr.catcore.modremapperapi.api.ModRemapper;
import fr.catcore.modremapperapi.api.RemapLibrary;
import fr.catcore.modremapperapi.remapping.RemapUtil;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.tinyremapper.TinyRemapper;
import org.jetbrains.annotations.ApiStatus;

import java.nio.file.Path;
import java.util.*;

@ApiStatus.Internal
public final class BabricatedModRemapper implements ModRemapper {
	@Override
	public String[] getJarFolders() {
		return new String[0];
	}

	private Path getLibPath(String name) {
		return BabricatedForge.MOD_CONTAINER.findPath("./libs/" + name + ".zip").get();
	}

	@Override
	public RemapLibrary[] getRemapLibraries() {
		RemapLibrary[] libraries = new RemapLibrary[0];
		switch (FabricLoader.getInstance().getEnvironmentType()) {
			case CLIENT:
				libraries = new RemapLibrary[] {
						new RemapLibrary(getLibPath("modloader-b1.7.3"), new ArrayList<>(), "modloader.zip"),
						new RemapLibrary(getLibPath("modloadermp-1.7.3-unofficial-v2"), new ArrayList<>(), "modloadermp-client.zip"),
						new RemapLibrary(getLibPath("minecraftforge-client-1.0.7-20110907"), new ArrayList<>(), "forge-client.zip")
				};
				break;
			case SERVER:
				libraries = new RemapLibrary[] {
						new RemapLibrary(getLibPath("modloadermp-1.7.3-unofficial-server-v2"), new ArrayList<>(), "modloadermp-server.zip"),
						new RemapLibrary(getLibPath("minecraftforge-server-1.0.7-20110907"), new ArrayList<>(), "forge-server.zip")
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
	}

	@Override
	public Optional<TinyRemapper.ApplyVisitorProvider> getPostRemappingVisitor() {
		return Optional.empty();
	}
}
