package io.github.betterthanupdates.shockahpi.client.entity.player;

import shockahpi.PlayerBase;

import java.util.List;

/**
 * Implements getter for public field patch in ClientPlayerEntity.
 */
public interface SapiClientPlayerEntity {
	List<PlayerBase> getPlayerBases();
}
