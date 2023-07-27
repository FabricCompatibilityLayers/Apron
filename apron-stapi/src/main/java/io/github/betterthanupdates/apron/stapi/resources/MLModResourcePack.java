package io.github.betterthanupdates.apron.stapi.resources;

import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ModID;
import net.modificationstation.stationapi.api.resource.InputSupplier;
import net.modificationstation.stationapi.api.resource.ResourceType;
import net.modificationstation.stationapi.impl.resource.AbstractFileResourcePack;
import org.jetbrains.annotations.Nullable;

public class MLModResourcePack extends AbstractFileResourcePack {
	private final List<MLZipPack> packs;
	protected MLModResourcePack(List<MLZipPack> packs) {
		super("ModLoader Mods", true);
		this.packs = packs;
	}

	private static String toPath(ResourceType type, Identifier id) {
		return String.format(Locale.ROOT, "%s/%s/%s", type.getDirectory(), id.modID, id.id);
	}

	@Override
	public @Nullable InputSupplier<InputStream> openRoot(String... segments) {
		for (MLZipPack pack : packs) {
			InputSupplier<InputStream> result = pack.openFile(String.join("/", segments));

			if (result != null) return result;
		}

		return null;
	}

	@Override
	public @Nullable InputSupplier<InputStream> open(ResourceType type, Identifier id) {
		for (MLZipPack pack : packs) {
			InputSupplier<InputStream> result = pack.openFile(toPath(type, id));

			if (result != null) return result;
		}

		return null;
	}

	@Override
	public void findResources(ResourceType type, ModID namespace, String prefix, ResultConsumer consumer) {
		for (MLZipPack pack : packs) {
			pack.findResources(type, namespace, prefix, consumer);
		}
	}

	@Override
	public Set<ModID> getNamespaces(ResourceType type) {
		HashSet<ModID> list = new HashSet<>();

		for (MLZipPack pack : packs) {
			list.addAll(pack.getNamespaces(type));
		}

		return list;
	}

	@Override
	public void close() {

	}
}
