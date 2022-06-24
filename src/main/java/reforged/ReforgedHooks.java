package reforged;

import io.github.betterthanupdates.Legacy;

@Legacy
public class ReforgedHooks {
	@Legacy
	public static final int majorVersion = 1;
	@Legacy
	public static final int minorVersion = 0;
	@Legacy
	public static final int revisionVersion = 0;

	@Legacy
	public ReforgedHooks() {
	}

	@Legacy
	public static void touch() {
	}

	static {
		Reforged.LOGGER.info("Reforged V%d.%d.%d Initialized", majorVersion, minorVersion, revisionVersion);
	}
}
