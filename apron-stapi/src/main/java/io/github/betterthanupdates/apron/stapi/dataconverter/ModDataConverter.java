package io.github.betterthanupdates.apron.stapi.dataconverter;


import io.github.betterthanupdates.apron.stapi.dataconverter.claysoldier.ClaySoldierDatabase;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.event.datafixer.DataFixerRegisterEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;

import java.util.ArrayList;
import java.util.List;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public final class ModDataConverter {
	private static final List<ModDatabase> datafixers = new ArrayList<>();
	@EventListener
	private static void registerFixer(DataFixerRegisterEvent event) {
		datafixers.add(new ClaySoldierDatabase());

		datafixers.forEach(ModDatabase::register);
		datafixers.forEach(ModDatabase::registerDataFixer);
	}
}
