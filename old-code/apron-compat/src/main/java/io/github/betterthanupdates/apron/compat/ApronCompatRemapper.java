package io.github.betterthanupdates.apron.compat;

import io.github.fabriccompatibiltylayers.modremappingapi.api.v1.MappingBuilder;
import io.github.fabriccompatibiltylayers.modremappingapi.api.v1.ModRemapper;
import io.github.fabriccompatibiltylayers.modremappingapi.api.v1.RemapLibrary;
import io.github.fabriccompatibiltylayers.modremappingapi.api.v1.VisitorInfos;
import net.fabricmc.api.EnvType;
import org.spongepowered.asm.mixin.Mixins;

import java.util.List;

public class ApronCompatRemapper implements ModRemapper {
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
		// InfSprites
		String[][] classFixes = new String[][] {
				new String[]{"net/mine_diver/infsprites/render/Tessellators", "io/github/betterthanupdates/apron/compat/InfSpriteTessellators"}
		};

		for (String[] entry : classFixes) {
			visitorInfos.registerSuperType(entry[0], entry[1]);
			visitorInfos.registerTypeAnnotation(entry[0], entry[1]);
			visitorInfos.registerMethodTypeIns(entry[0], entry[1]);

			visitorInfos.registerFieldRef(entry[0], "", "", new VisitorInfos.FullClassMember(entry[1], "", null));
			visitorInfos.registerMethodInvocation(entry[0], "", "", new VisitorInfos.FullClassMember(entry[1], "", null));
		}
	}

	@Override
	public void afterRemap() {
		Mixins.addConfiguration("apron-compat.mixins.json");
	}
}
