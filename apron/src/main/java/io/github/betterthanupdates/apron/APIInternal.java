package io.github.betterthanupdates.apron;

import io.github.betterthanupdates.apron.api.ApronApi;
import io.github.betterthanupdates.apron.impl.client.ApronClientImpl;
import io.github.betterthanupdates.apron.impl.server.ApronServerImpl;

public class APIInternal {
	static ApronApi instance;

	public static ApronApi getInstance() {
		if (instance == null) {
			switch (Apron.getEnvironment()) {
				case SERVER:
					instance = new ApronServerImpl();
				case CLIENT:
				default:
					instance = new ApronClientImpl();
			}

			instance.onInitialized();
		}

		return instance;
	}
}
