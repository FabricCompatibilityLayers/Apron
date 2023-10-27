package io.github.betterthanupdates.apron.stapi.dataconverter.claysoldier;

import net.modificationstation.stationapi.api.registry.ModID;

import io.github.betterthanupdates.apron.stapi.dataconverter.ModDatabase;

public class ClaySoldierDatabase extends ModDatabase {
	public ClaySoldierDatabase() {
		super(ModID.of("mod_ClayMan"), ModID.of("claysoldiers"));
	}

	@Override
	public void register() {
		item(256 + 6849, "claydisruptor");
		item(256 + 6850, "claydoll");
		item(256 + 6851, "reddoll");
		item(256 + 6852, "yellowdoll");
		item(256 + 6853, "greendoll");
		item(256 + 6854, "bluedoll");
		item(256 + 6855, "horsedoll");
		item(256 + 6856, "orangedoll");
		item(256 + 6857, "purpledoll");
		entity("ClaySoldier", "claysoldier");
		entity("DirtHorse", "dirthorse");
		entity("GravelChunk", "gravelchunk");
	}
}
