package io.github.fabriccompatibilitylayers.modloader.compat.modmenu.babric;

import net.fabricmc.loader.api.metadata.ModOrigin;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

public class MLModOrigin implements ModOrigin {
	@Override
	public Kind getKind() {
		return Kind.UNKNOWN;
	}

	@Override
	public List<Path> getPaths() {
		return Collections.emptyList();
	}

	@Override
	public String getParentModId() {
		return null;
	}

	@Override
	public String getParentSubLocation() {
		return null;
	}
}
