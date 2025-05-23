package io.github.betterthanupdates.apron.stapi.compat;

import io.github.fabriccompatibiltylayers.modremappingapi.api.v1.MappingBuilder;
import io.github.fabriccompatibiltylayers.modremappingapi.api.v1.ModRemapper;
import io.github.fabriccompatibiltylayers.modremappingapi.api.v1.RemapLibrary;
import io.github.fabriccompatibiltylayers.modremappingapi.api.v1.VisitorInfos;
import net.fabricmc.api.EnvType;
import org.spongepowered.asm.mixin.Mixins;

import java.util.List;

public class ApronStAPICompatRemapper implements ModRemapper {
	@Override
	public String[] getJarFolders() {
		return new String[0];
	}

	@Override
	public void addRemapLibraries(List<RemapLibrary> list, EnvType envType) {

	}

	@Override
	public void registerMappings(MappingBuilder mappingBuilder) {

	}

	@Override
	public void registerPreVisitors(VisitorInfos visitorInfos) {

	}

	@Override
	public void registerPostVisitors(VisitorInfos visitorInfos) {

	}

	@Override
	public void afterRemap() {
		Mixins.addConfiguration("apron-stapi-compat.mixins.json");
	}
}
