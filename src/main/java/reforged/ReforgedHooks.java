package reforged;

import io.github.betterthanupdates.Legacy;

@Legacy
public class ReforgedHooks {
	public static final int majorVersion = 1;
	public static final int minorVersion = 0;
	public static final int revisionVersion = 0;

	public ReforgedHooks() {
	}

	public static void touch() {
	}

	static {
		Reforged.LOGGER.info("Reforged V%d.%d.%d Initialized", majorVersion, minorVersion, revisionVersion);
	}
}
