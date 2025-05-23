package io.github.betterthanupdates.apron.mixin.client.sound;

import net.minecraft.client.sound.SoundManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import paulscode.sound.SoundSystem;

@Mixin(SoundManager.class)
public interface SoundManagerAccessor {
	@Accessor
	int getField_2671();
	@Accessor
	void setField_2675(int countdown);

	@Accessor
	static SoundSystem getSoundSystem() {
		return null;
	}
}
