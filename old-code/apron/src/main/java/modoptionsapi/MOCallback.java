package modoptionsapi;

public abstract class MOCallback {
	public MOCallback() {
	}

	public abstract boolean onClick(ModOption modOption);

	public boolean onGlobalChange(boolean newValue, ModOption option) {
		return true;
	}
}
