package io.github.betterthanupdates.apron.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import paulscode.sound.SoundSystem;

import net.minecraft.client.sound.SoundHelper;

@Mixin(SoundHelper.class)
public interface SoundHelperAccessor {
	@Accessor
	int getSoundUID();
	@Accessor
	void setMusicCountdown(int countdown);

	@Accessor
	static SoundSystem getSoundSystem() {
		return null;
	}
}
