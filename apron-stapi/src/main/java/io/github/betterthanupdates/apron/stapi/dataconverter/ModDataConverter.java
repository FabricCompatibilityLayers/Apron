package io.github.betterthanupdates.apron.stapi.dataconverter;

import com.mojang.datafixers.DataFixerBuilder;
import com.mojang.datafixers.schemas.Schema;
import io.github.betterthanupdates.apron.stapi.dataconverter.claysoldier.ClaySoldierDataFixer;
import io.github.betterthanupdates.apron.stapi.dataconverter.claysoldier.ClaySoldierSchema;
import net.fabricmc.loader.api.FabricLoader;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.datafixer.DataFixers;
import net.modificationstation.stationapi.api.datafixer.TypeReferences;
import net.modificationstation.stationapi.api.event.datafixer.DataFixerRegisterEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.registry.ModID;

import java.util.Set;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public final class ModDataConverter {
	@EventListener
	private static void registerFixer(DataFixerRegisterEvent event) {
		ModID clayManId = ModID.of("mod_ClayMan");
		int clayManVersion = FabricLoader.getInstance().isModLoaded("claysoldiers") ? 1 : 0;

		DataFixers.registerFixer(clayManId, executor -> {
			DataFixerBuilder builder = new DataFixerBuilder(clayManVersion);

			Schema schema = builder.addSchema(1, ClaySoldierSchema::new);
			builder.addFixer(new ClaySoldierDataFixer("ClayManToClaySoldierItemFix", schema));

			return builder.buildOptimized(Set.of(TypeReferences.LEVEL), executor);
		}, clayManVersion);
	}
}
