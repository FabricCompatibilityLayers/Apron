package io.github.betterthanupdates.audiomod.mixin.client;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Random;

import net.fabricmc.loader.api.FabricLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.codecs.CodecIBXM;

import net.minecraft.client.Minecraft;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.sound.SoundEntry;
import net.minecraft.client.sound.SoundHelper;
import net.minecraft.client.sound.SoundMap;
import net.minecraft.util.math.MathHelper;

import io.github.betterthanupdates.apron.api.ApronApi;

/**
 * Believe it or not, this is all of AudioMod!
 *
 * @see paulscode.sound.codecs.CodecIBXM
 */
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
	private static SoundSystem soundSystem;
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

		this.client = (Minecraft) ApronApi.getInstance().getGame();
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
	@Redirect(method = "updateMusicVolume", at = @At(value = "FIELD", target = "Lnet/minecraft/client/sound/SoundHelper;initialized:Z", ordinal = 1))
	public boolean updateMusicVolume() {
		return initialized && soundSystem != null;
	}

	/**
	 * @author Risugami
	 * @reason AudioMod patches
	 */
	@Redirect(method = "handleBackgroundMusic", at = @At(value = "FIELD", target = "Lnet/minecraft/client/sound/SoundHelper;initialized:Z"))
	public boolean handleBackgroundMusic() {
		return initialized && soundSystem != null;
	}

	/**
	 * @author Risugami
	 * @reason AudioMod patches
	 */
	@Redirect(method = "handleBackgroundMusic", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sound/SoundMap;getRandomSound()Lnet/minecraft/client/sound/SoundEntry;"))
	public SoundEntry handleBackgroundMusic(SoundMap instance) {
		if (this.client != null
				&& this.client.player != null
				&& !this.client
				.player
				.world
				.isAboveGroundCached(MathHelper.floor(this.client.player.x), MathHelper.floor(this.client.player.y), MathHelper.floor(this.client.player.z))) {
			return this.cave.getRandomSound();
		} else {
			return this.music.getRandomSound();
		}
	}

	/**
	 * @author Risugami
	 * @reason AudioMod patches
	 */
	@Redirect(method = "setSoundPosition", at = @At(value = "FIELD", target = "Lnet/minecraft/client/sound/SoundHelper;initialized:Z"))
	public boolean setSoundPosition() {
		return initialized && soundSystem != null;
	}

	/**
	 * @author Risugami
	 * @reason AudioMod patches
	 */
	@Redirect(method = "playStreaming", at = @At(value = "FIELD", target = "Lnet/minecraft/client/sound/SoundHelper;initialized:Z"))
	public boolean playSound() {
		return initialized && soundSystem != null;
	}

	/**
	 * @author Risugami
	 * @reason AudioMod patches
	 */
	@Redirect(method = "playSound(Ljava/lang/String;FFFFF)V", at = @At(value = "FIELD", target = "Lnet/minecraft/client/sound/SoundHelper;initialized:Z"))
	public boolean playSound$1() {
		return initialized && soundSystem != null;
	}

	/**
	 * @author Risugami
	 * @reason AudioMod patches
	 */
	@Redirect(method = "playSound(Ljava/lang/String;FF)V", at = @At(value = "FIELD", target = "Lnet/minecraft/client/sound/SoundHelper;initialized:Z"))
	public boolean playSound$2() {
		return initialized && soundSystem != null;
	}
}
