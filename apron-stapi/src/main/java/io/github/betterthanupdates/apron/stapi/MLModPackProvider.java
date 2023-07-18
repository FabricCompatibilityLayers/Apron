package io.github.betterthanupdates.apron.stapi;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.zip.ZipFile;

import net.modificationstation.stationapi.api.resource.ResourceType;
import net.modificationstation.stationapi.impl.resource.ResourcePackProfile;
import net.modificationstation.stationapi.impl.resource.ResourcePackProvider;
import net.modificationstation.stationapi.impl.resource.ResourcePackSource;
import net.modificationstation.stationapi.impl.resource.ZippedTexturePackResourcePack;

import io.github.betterthanupdates.apron.LifecycleUtils;

public class MLModPackProvider implements ResourcePackProvider {
	@Override
	public void register(Consumer<ResourcePackProfile> profileAdder) {
		List<MLZipPack> packs = new ArrayList<>();
		addBuiltIns(packs);

		if (!packs.isEmpty()) {
			ResourcePackProfile resourcePackProfile = ResourcePackProfile.create(
					"ModLoader Mods",
					"ModLoader Mods",
					true,
					name -> new MLModResourcePack(packs),
					ResourceType.CLIENT_RESOURCES,
					ResourcePackProfile.InsertionPosition.TOP,
					ResourcePackSource.NONE
			);

			if (resourcePackProfile != null) profileAdder.accept(resourcePackProfile);
		}
	}

	private static void addBuiltIns(List<MLZipPack> list) {
		for (File file : LifecycleUtils.MOD_FILES) {
			try {
				list.add(new MLZipPack(new ZipFile(file)));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
