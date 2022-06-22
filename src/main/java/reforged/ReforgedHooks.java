package reforged;

public class ReforgedHooks {
	public static final int majorVersion = 1;
	public static final int minorVersion = 0;
	public static final int revisionVersion = 0;

	public ReforgedHooks() {
	}

	public static void touch() {
	}

	static {
		System.out.printf("Reforged V%d.%d.%d Initialized\n", majorVersion, minorVersion, revisionVersion);
	}
}
