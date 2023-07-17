package io.github.betterthanupdates.stapi;

public interface StAPIBlock {
	default int getOriginalBlockId() {
		return -1;
	}
}
