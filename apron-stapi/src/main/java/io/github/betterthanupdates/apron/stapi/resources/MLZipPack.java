package io.github.betterthanupdates.apron.stapi.resources;

import static io.github.betterthanupdates.apron.stapi.ApronStAPICompat.LOGGER;
import static net.modificationstation.stationapi.impl.resource.ZipResourcePack.TYPE_NAMESPACE_SPLITTER;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ModID;
import net.modificationstation.stationapi.api.resource.InputSupplier;
import net.modificationstation.stationapi.api.resource.ResourcePack;
import net.modificationstation.stationapi.api.resource.ResourceType;
import net.modificationstation.stationapi.impl.resource.ModResourcePackUtil;
import org.apache.commons.io.IOUtils;

public class MLZipPack {
	private final ZipFile zipFile;

	public MLZipPack(ZipFile zipFile) {
		this.zipFile = zipFile;
	}

	protected InputSupplier<InputStream> openFile(String path) {
		if (zipFile == null) return null;
		ZipEntry zipEntry = zipFile.getEntry(path);
		if (zipEntry == null) return switch (path) {
			case "pack.mcmeta" -> () -> {
				String metadata = ModResourcePackUtil.serializeMetadata(13, "ModLoader mod ResourcePack");
				return IOUtils.toInputStream(metadata, Charsets.UTF_8);
			};
			default -> null;
		};
		return InputSupplier.create(zipFile, zipEntry);
	}

	public void findResources(ResourceType type, ModID namespace, String prefix, ResourcePack.ResultConsumer consumer) {
		if (zipFile == null) return;
		Enumeration<? extends ZipEntry> enumeration = zipFile.entries();
		String string = type.getDirectory() + "/" + namespace + "/";
		String string2 = string + prefix + "/";
		while (enumeration.hasMoreElements()) {
			String string3;
			ZipEntry zipEntry = enumeration.nextElement();
			if (zipEntry.isDirectory() || !(string3 = zipEntry.getName()).startsWith(string2)) continue;
			String string4 = string3.substring(string.length());
			Identifier identifier = Identifier.of(namespace, string4);
			consumer.accept(identifier, InputSupplier.create(zipFile, zipEntry));
		}
	}

	public Set<ModID> getNamespaces(ResourceType type) {
		if (zipFile == null) return Set.of();
		Enumeration<? extends ZipEntry> enumeration = zipFile.entries();
		HashSet<ModID> set = new HashSet<>();
		set.add(ModID.MINECRAFT); // root
		while (enumeration.hasMoreElements()) {
			ArrayList<String> list;
			ZipEntry zipEntry = enumeration.nextElement();
			String string = zipEntry.getName();
			if (!string.startsWith(type.getDirectory() + "/") || (list = Lists.newArrayList(TYPE_NAMESPACE_SPLITTER.split(string))).size() <= 1) continue;
			String string2 = list.get(1);
			if (string2.equals(string2.toLowerCase(Locale.ROOT))) {
				set.add(ModID.of(string2));
				continue;
			}
			LOGGER.warn("Ignored non-lowercase namespace: {} in {}", string2, zipFile.getName());
		}
		return set;
	}
}
