package io.github.betterthanupdates.apron;

public interface StAPIBlock {
	default int getOriginalBlockId() {
		return -1;
	}
}
