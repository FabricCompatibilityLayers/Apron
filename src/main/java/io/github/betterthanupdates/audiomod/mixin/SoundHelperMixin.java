package io.github.betterthanupdates.audiomod.mixin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Random;

import net.fabricmc.loader.api.FabricLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.codecs.CodecIBXM;

import net.minecraft.client.Minecraft;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.sound.SoundEntry;
import net.minecraft.client.sound.SoundHelper;
import net.minecraft.client.sound.SoundMap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;

import io.github.betterthanupdates.babricated.api.BabricatedApi;

@Mixin(SoundHelper.class)
public abstract class SoundHelperMixin {
	@Shadow
	private Random rand;
	@Shadow
	private int musicCountdown;

	@Shadow
	private SoundMap sounds;
	@Shadow
	private SoundMap streaming;
	@Shadow
	private SoundMap music;
	@Shadow
	private static boolean initialized;
	@Shadow
	private GameOptions gameOptions;

	@Shadow
	protected abstract void setLibsAndCodecs();

	@Shadow
	private static SoundSystem soundSystem;
	@Shadow
	private int soundUID;
	// AudioMod Fields
	@Unique
	private final SoundMap cave = new SoundMap();
	@Unique
	private Minecraft client;
	@Unique
	private static final int MUSIC_INTERVAL = 6000;

	@Inject(method = "<init>", at = @At("RETURN"))
	private void audiomod$ctr(CallbackInfo ci) {
		this.musicCountdown = this.rand.nextInt(MUSIC_INTERVAL);
	}

	@Inject(method = "acceptOptions", at = @At("RETURN"))
	private void audiomod$acceptOptions(GameOptions options, CallbackInfo ci) {
		loadModAudio("./resources/mod/sound", this.sounds);
		loadModAudio("./resources/mod/streaming", this.streaming);
		loadModAudio("./resources/mod/music", this.music);
		loadModAudio("./resources/mod/cavemusic", this.cave);

		this.client = (Minecraft) BabricatedApi.getInstance().getGame();
	}

	/**
	 * @author Risugami
	 */
	private static void loadModAudio(String folder, SoundMap array) {
		File base = new File(FabricLoader.getInstance().getGameDir().toFile(), folder);

		walkFolder(base, base, array);
	}

	/**
	 * @author Risugami
	 */
	private static void walkFolder(File root, File folder, SoundMap sounds) {
		if (folder.exists() || folder.mkdirs()) {
			File[] files = folder.listFiles();

			if (files != null && files.length > 0) {
				for (File file : files) {
					if (!file.getName().startsWith(".")) {
						try {
							BasicFileAttributes f = Files.readAttributes(file.toPath(), BasicFileAttributes.class);

							if (f.isDirectory()) {
								walkFolder(root, file, sounds);
							} else if (f.isRegularFile()) {
								String path = file.getPath().substring(root.getPath().length() + 1)
										.replace('\\', '/');
								sounds.addSound(path, file);
							}
						} catch (IOException ignored) {
							// Simply don't load the file.
						}
					}
				}
			}
		}
	}

	/**
	 * @author Risugami
	 * @reason IBXM audio codec for Paul's SoundSystem
	 */
	@Inject(method = "setLibsAndCodecs", at = @At(value = "INVOKE", ordinal = 2, shift = At.Shift.AFTER, remap = false,
			target = "Lpaulscode/sound/SoundSystemConfig;setCodec(Ljava/lang/String;Ljava/lang/Class;)V"))
	private void audiomod$setLibsAndCodecs(CallbackInfo ci) {
		SoundSystemConfig.setCodec("xm", CodecIBXM.class);
		SoundSystemConfig.setCodec("s3m", CodecIBXM.class);
		SoundSystemConfig.setCodec("mod", CodecIBXM.class);
	}

	/**
	 * @author Risugami
	 * @reason AudioMod patches
	 */
	@Overwrite
	public void updateMusicVolume() {
		if (!initialized && (this.gameOptions.soundVolume != 0.0F || this.gameOptions.musicVolume != 0.0F)) {
			this.setLibsAndCodecs();
		}

		if (soundSystem != null && initialized) {
			if (this.gameOptions.musicVolume == 0.0F) {
				soundSystem.stop("BgMusic");
			} else {
				soundSystem.setVolume("BgMusic", this.gameOptions.musicVolume);
			}
		}
	}

	/**
	 * @author Risugami
	 * @reason AudioMod patches
	 */
	@Overwrite
	public void handleBackgroundMusic() {
		if (initialized && this.gameOptions.musicVolume != 0.0F && soundSystem != null) {
			if (!soundSystem.playing("BgMusic") && !soundSystem.playing("streaming")) {
				if (this.musicCountdown > 0) {
					--this.musicCountdown;
					return;
				}

				SoundEntry entry;
				if (this.client != null
						&& this.client.player != null
						&& !this.client
						.player
						.world
						.isAboveGroundCached(MathHelper.floor(this.client.player.x), MathHelper.floor(this.client.player.y), MathHelper.floor(this.client.player.z))) {
					entry = this.cave.getRandomSound();
				} else {
					entry = this.music.getRandomSound();
				}

				if (entry != null) {
					this.musicCountdown = this.rand.nextInt(MUSIC_INTERVAL) + MUSIC_INTERVAL;
					soundSystem.backgroundMusic("BgMusic", entry.soundUrl, entry.soundName, false);
					soundSystem.setVolume("BgMusic", this.gameOptions.musicVolume);
					soundSystem.play("BgMusic");
				}
			}
		}
	}

	/**
	 * @author Risugami
	 * @reason AudioMod patches
	 */
	@Overwrite
	public void setSoundPosition(LivingEntity entity, float paramFloat) {
		if (initialized && this.gameOptions.soundVolume != 0.0F && soundSystem != null) {
			if (entity != null) {
				float f1 = entity.prevYaw + (entity.yaw - entity.prevYaw) * paramFloat;
				double d1 = entity.prevX + (entity.x - entity.prevX) * (double) paramFloat;
				double d2 = entity.prevY + (entity.y - entity.prevY) * (double) paramFloat;
				double d3 = entity.prevZ + (entity.z - entity.prevZ) * (double) paramFloat;
				float f2 = MathHelper.cos(-f1 * (float) (Math.PI / 180.0) - (float) Math.PI);
				float f3 = MathHelper.sin(-f1 * (float) (Math.PI / 180.0) - (float) Math.PI);
				float f4 = -f3;
				float f5 = 0.0F;
				float f6 = -f2;
				float f7 = 0.0F;
				float f8 = 1.0F;
				float f9 = 0.0F;
				soundSystem.setListenerPosition((float) d1, (float) d2, (float) d3);
				soundSystem.setListenerOrientation(f4, f5, f6, f7, f8, f9);
			}
		}
	}

	/**
	 * @author Risugami
	 * @reason AudioMod patches
	 */
	@Overwrite
	public void playStreaming(String soundId, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5) {
		if (initialized && this.gameOptions.soundVolume != 0.0F && soundSystem != null) {
			String str = "streaming";

			if (soundSystem.playing("streaming")) {
				soundSystem.stop("streaming");
			}

			if (soundId != null) {
				SoundEntry entry = this.streaming.getRandomSoundForId(soundId);

				if (entry != null && paramFloat4 > 0.0F) {
					if (soundSystem.playing("BgMusic")) {
						soundSystem.stop("BgMusic");
					}

					float f1 = 16.0F;
					soundSystem.newStreamingSource(true, str, entry.soundUrl, entry.soundName, false, paramFloat1, paramFloat2, paramFloat3, 2, f1 * 4.0F);
					soundSystem.setVolume(str, 0.5F * this.gameOptions.soundVolume);
					soundSystem.play(str);
				}
			}
		}
	}

	/**
	 * @author Risugami
	 * @reason AudioMod patches
	 */
	@Overwrite
	public void playSound(String paramString, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5) {
		if (initialized && this.gameOptions.soundVolume != 0.0F && soundSystem != null) {
			SoundEntry entry = this.sounds.getRandomSoundForId(paramString);

			if (entry != null && paramFloat4 > 0.0F) {
				this.soundUID = (this.soundUID + 1) % 256;
				String str = "sound_" + this.soundUID;
				float f1 = 16.0F;

				if (paramFloat4 > 1.0F) {
					f1 *= paramFloat4;
				}

				soundSystem.newSource(paramFloat4 > 1.0F, str, entry.soundUrl, entry.soundName, false, paramFloat1, paramFloat2, paramFloat3, 2, f1);
				soundSystem.setPitch(str, paramFloat5);

				if (paramFloat4 > 1.0F) {
					paramFloat4 = 1.0F;
				}

				soundSystem.setVolume(str, paramFloat4 * this.gameOptions.soundVolume);
				soundSystem.play(str);
			}
		}
	}

	/**
	 * @author Risugami
	 * @reason AudioMod patches
	 */
	@Overwrite
	public void playSound(String paramString, float paramFloat1, float paramFloat2) {
		if (initialized && this.gameOptions.soundVolume != 0.0F && soundSystem != null) {
			SoundEntry entry = this.sounds.getRandomSoundForId(paramString);

			if (entry != null) {
				this.soundUID = (this.soundUID + 1) % 256;
				String str = "sound_" + this.soundUID;
				soundSystem.newSource(false, str, entry.soundUrl, entry.soundName, false, 0.0F, 0.0F, 0.0F, 0, 0.0F);

				if (paramFloat1 > 1.0F) {
					paramFloat1 = 1.0F;
				}

				paramFloat1 *= 0.25F;
				soundSystem.setPitch(str, paramFloat2);
				soundSystem.setVolume(str, paramFloat1 * this.gameOptions.soundVolume);
				soundSystem.play(str);
			}
		}
	}
}
