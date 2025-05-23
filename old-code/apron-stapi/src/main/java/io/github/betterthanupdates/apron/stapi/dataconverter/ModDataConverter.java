package io.github.betterthanupdates.apron.stapi.dataconverter;


import io.github.betterthanupdates.apron.stapi.dataconverter.claysoldier.ClaySoldierDatabase;
import io.github.betterthanupdates.apron.stapi.dataconverter.stonewall.StoneWallDatabase;

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
		datafixers.add(new StoneWallDatabase());

		datafixers.forEach(ModDatabase::register);
		datafixers.forEach(ModDatabase::registerDataFixer);
	}
}
