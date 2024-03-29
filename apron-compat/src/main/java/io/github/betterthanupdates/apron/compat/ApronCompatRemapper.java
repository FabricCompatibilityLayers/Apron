package io.github.betterthanupdates.apron.compat;

import fr.catcore.modremapperapi.api.ModRemapper;
import fr.catcore.modremapperapi.remapping.RemapUtil;
import fr.catcore.modremapperapi.remapping.VisitorInfos;
import org.spongepowered.asm.mixin.Mixins;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApronCompatRemapper implements ModRemapper {
	@Override
	public String[] getJarFolders() {
		return new String[0];
	}

	@Override
	public Map<String, List<String>> getExclusions() {
		return new HashMap<>();
	}

	@Override
	public void getMappingList(RemapUtil.MappingList mappingList) {

	}

	@Override
	public void registerVisitors(VisitorInfos infos) {
		// InfSprites
		String[][] toolFixes = new String[][] {
				new String[]{"net/mine_diver/infsprites/render/Tessellators", "io/github/betterthanupdates/apron/compat/InfSpriteTessellators"}
		};

		for (String[] entry : toolFixes) {
			infos.registerSuperType(new VisitorInfos.Type(entry[0]), new VisitorInfos.Type(entry[1]));
			infos.registerTypeAnnotation(new VisitorInfos.Type(entry[0]), new VisitorInfos.Type(entry[1]));
			infos.registerMethodTypeIns(new VisitorInfos.Type(entry[0]), new VisitorInfos.Type(entry[1]));
			infos.registerMethodFieldIns(new VisitorInfos.MethodNamed(entry[0], ""), new VisitorInfos.MethodNamed(entry[1], ""));
			infos.registerMethodMethodIns(new VisitorInfos.MethodNamed(entry[0], ""), new VisitorInfos.MethodNamed(entry[1], ""));
		}
	}

	@Override
	public void afterRemap() {
		Mixins.addConfiguration("apron-compat.mixins.json");
	}
}
