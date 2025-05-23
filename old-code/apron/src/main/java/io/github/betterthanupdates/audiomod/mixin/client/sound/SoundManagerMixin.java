package io.github.betterthanupdates.audiomod.mixin.client.sound;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Random;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.class_266;
import net.minecraft.class_267;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.codecs.CodecIBXM;

import net.minecraft.client.Minecraft;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.util.math.MathHelper;

import io.github.betterthanupdates.apron.api.ApronApi;

/**
 * Believe it or not, this is all of AudioMod!
 *
 * @see paulscode.sound.codecs.CodecIBXM
 */
@Mixin(SoundManager.class)
public abstract class SoundManagerMixin {
	@Shadow
	private Random field_2674;
	@Shadow
	private int field_2675;
	@Shadow
	private class_266 field_2668;
	@Shadow
	private class_266 field_2669;
	@Shadow
	private class_266 field_2670;

	@Shadow
	private static SoundSystem soundSystem;

	// AudioMod Fields
	@Unique
	private final class_266 cave = new class_266();
	@Unique
	private Minecraft client;
	@Unique
	private static final int MUSIC_INTERVAL = 6000;

	@Inject(method = "<init>", at = @At("RETURN"))
	private void audiomod$ctr(CallbackInfo ci) {
		this.field_2675 = this.field_2674.nextInt(MUSIC_INTERVAL);
	}

	@Inject(method = "method_2012", at = @At("RETURN"))
	private void audiomod$acceptOptions(GameOptions options, CallbackInfo ci) {
		loadModAudio("./resources/mod/sound", this.field_2668);
		loadModAudio("./resources/mod/streaming", this.field_2669);
		loadModAudio("./resources/mod/music", this.field_2670);
		loadModAudio("./resources/mod/cavemusic", this.cave);

		this.client = (Minecraft) ApronApi.getInstance().getGame();
	}

	/**
	 * @author Risugami
	 */
	private static void loadModAudio(String folder, class_266 array) {
		File base = new File(FabricLoader.getInstance().getGameDir().toFile(), folder);

		walkFolder(base, base, array);
	}

	/**
	 * @author Risugami
	 */
	private static void walkFolder(File root, File folder, class_266 sounds) {
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
								sounds.method_959(path, file);
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
	@Inject(method = "method_2019", at = @At(value = "INVOKE", ordinal = 2, shift = At.Shift.AFTER, remap = false,
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
	@WrapOperation(method = "method_2008", at = @At(value = "FIELD", target = "Lnet/minecraft/client/sound/SoundManager;field_2673:Z", ordinal = 1))
	public boolean updateMusicVolume(Operation<Boolean> original) {
		return original.call() && soundSystem != null;
	}

	/**
	 * @author Risugami
	 * @reason AudioMod patches
	 */
	@WrapOperation(method = "method_2017", at = @At(value = "FIELD", target = "Lnet/minecraft/client/sound/SoundManager;field_2673:Z"))
	public boolean handleBackgroundMusic(Operation<Boolean> original) {
		return original.call() && soundSystem != null;
	}

	/**
	 * @author Risugami
	 * @reason AudioMod patches
	 */
	@WrapOperation(method = "method_2017", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_266;method_957()Lnet/minecraft/class_267;"))
	public class_267 handleBackgroundMusic(class_266 instance, Operation<class_267> operation) {
		if (this.client != null
				&& this.client.player != null
				&& !this.client
				.player
				.world
				.method_249(MathHelper.floor(this.client.player.x), MathHelper.floor(this.client.player.y), MathHelper.floor(this.client.player.z))) {
			return operation.call(this.cave);
		} else {
			return operation.call(this.field_2670);
		}
	}

	/**
	 * @author Risugami
	 * @reason AudioMod patches
	 */
	@WrapOperation(method = "method_2013", at = @At(value = "FIELD", target = "Lnet/minecraft/client/sound/SoundManager;field_2673:Z"))
	public boolean setSoundPosition(Operation<Boolean> original) {
		return original.call() && soundSystem != null;
	}

	/**
	 * @author Risugami
	 * @reason AudioMod patches
	 */
	@WrapOperation(method = "method_2010", at = @At(value = "FIELD", target = "Lnet/minecraft/client/sound/SoundManager;field_2673:Z"))
	public boolean playSound(Operation<Boolean> original) {
		return original.call() && soundSystem != null;
	}

	/**
	 * @author Risugami
	 * @reason AudioMod patches
	 */
	@WrapOperation(method = "method_2015", at = @At(value = "FIELD", target = "Lnet/minecraft/client/sound/SoundManager;field_2673:Z"))
	public boolean playSound$1(Operation<Boolean> original) {
		return original.call() && soundSystem != null;
	}

	/**
	 * @author Risugami
	 * @reason AudioMod patches
	 */
	@WrapOperation(method = "method_2009", at = @At(value = "FIELD", target = "Lnet/minecraft/client/sound/SoundManager;field_2673:Z"))
	public boolean playSound$2(Operation<Boolean> original) {
		return original.call() && soundSystem != null;
	}
}
