package io.github.betterthanupdates.stapi.event;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.event.entity.player.PlayerEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import reforged.Reforged;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class ApronCustomReachImpl {
	@EventListener
	private static void getReach(PlayerEvent.Reach event) {
		double reforgedReach = Reforged.reachGetEntityPlayer(event.player);

		if (reforgedReach != 3d && reforgedReach != event.currentReach) {
			event.currentReach = reforgedReach;
		}
	}
}
