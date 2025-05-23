package io.github.betterthanupdates.apron;

import io.github.betterthanupdates.apron.api.ApronApi;
import io.github.betterthanupdates.apron.impl.client.ApronClientImpl;
import io.github.betterthanupdates.apron.impl.server.ApronServerImpl;
import net.fabricmc.api.EnvType;

public class APIInternal {
	static ApronApi instance;

	public static ApronApi getInstance() {
		if (instance == null) {
			if (Apron.getEnvironment() == EnvType.SERVER) {
				instance = new ApronServerImpl();
			} else {
				instance = new ApronClientImpl();
			}

			instance.onInitialized();
		}

		return instance;
	}
}
