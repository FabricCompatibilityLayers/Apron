package io.github.betterthanupdates.audiomod.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.sound.SoundEntry;
import net.minecraft.client.sound.SoundHelper;
import net.minecraft.client.sound.SoundMap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
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

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Random;

@Mixin(SoundHelper.class)
public abstract class SoundHelperMixin {

    @Shadow private Random rand;
    @Shadow private int musicCountdown;

    @Shadow private SoundMap sounds;
    @Shadow private SoundMap streaming;
    @Shadow private SoundMap music;
    @Shadow private static boolean initialized;
    @Shadow private GameOptions gameOptions;

    @Shadow protected abstract void setLibsAndCodecs();

    @Shadow private static SoundSystem soundSystem;
    @Shadow private int soundUID;
    // AudioMod Fields
    @Unique
    private SoundMap cave = new SoundMap();
    @Unique
    private Minecraft mc;
    @Unique
    private static final int MUSINTERVAL = 6000;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void audiomod$ctr(CallbackInfo ci) {
        this.musicCountdown = this.rand.nextInt(MUSINTERVAL);
    }

    @Inject(method = "acceptOptions", at = @At("RETURN"))
    private void audiomod$acceptOptions(GameOptions paramkv, CallbackInfo ci) {
        loadModAudio("minecraft/resources/mod/sound", this.sounds);
        loadModAudio("minecraft/resources/mod/streaming", this.streaming);
        loadModAudio("minecraft/resources/mod/music", this.music);
        loadModAudio("minecraft/resources/mod/cavemusic", this.cave);

        try {
            Field minecraft = Minecraft.class.getDeclaredFields()[1];
            minecraft.setAccessible(true);
            this.mc = (Minecraft)minecraft.get(null);
        } catch (Throwable var3) {
        }
    }

    private static void loadModAudio(String folder, SoundMap array) {
        File base = Minecraft.getWorkingDirectory(folder);

        try {
            walkFolder(base, base, array);
        } catch (IOException var4) {
            var4.printStackTrace();
        }
    }

    private static void walkFolder(File root, File folder, SoundMap array) throws IOException {
        if (folder.exists() || folder.mkdirs()) {
            File[] files = folder.listFiles();
            if (files != null && files.length > 0) {
                for (File file : files) {
                    if (!file.getName().startsWith(".")) {
                        BasicFileAttributes basicFileAttributes = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
                        if (basicFileAttributes.isDirectory()) {
                            walkFolder(root, file, array);
                        } else if (basicFileAttributes.isRegularFile()) {
                            String path = file.getPath().substring(root.getPath().length() + 1).replace('\\', '/');
                            array.addSound(path, file);
                        }
                    }
                }
            }
        }
    }

    @Inject(method = "setLibsAndCodecs", at = @At(value = "NEW", target = "paulscode/sound/SoundSystem"))
    private void audiomod$setLibsAndCodecs(CallbackInfo ci) {
        try {
            Class.forName("paulscode.sound.codecs.CodecIBXM");
            SoundSystemConfig.setCodec("xm", CodecIBXM.class);
            SoundSystemConfig.setCodec("s3m", CodecIBXM.class);
            SoundSystemConfig.setCodec("mod", CodecIBXM.class);
        } catch (ClassNotFoundException ignored) {
        }
    }

    /**
     * @author Risugami
     * @reason
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
     * @reason
     */
    @Overwrite
    public void handleBackgroundMusic() {
        if (initialized && this.gameOptions.musicVolume != 0.0F && soundSystem != null) {
            if (!soundSystem.playing("BgMusic") && !soundSystem.playing("streaming")) {
                if (this.musicCountdown > 0) {
                    --this.musicCountdown;
                    return;
                }

                SoundEntry localbh;
                if (this.mc != null
                        && this.mc.player != null
                        && !this.mc
                        .player
                        .world
                        .isAboveGroundCached(MathHelper.floor(this.mc.player.x), MathHelper.floor(this.mc.player.y), MathHelper.floor(this.mc.player.z))) {
                    localbh = this.cave.getRandomSound();
                } else {
                    localbh = this.music.getRandomSound();
                }

                if (localbh != null) {
                    this.musicCountdown = this.rand.nextInt(MUSINTERVAL) + MUSINTERVAL;
                    soundSystem.backgroundMusic("BgMusic", localbh.soundUrl, localbh.soundName, false);
                    soundSystem.setVolume("BgMusic", this.gameOptions.musicVolume);
                    soundSystem.play("BgMusic");
                }
            }

        }
    }

    /**
     * @author Risugami
     * @reason
     */
    @Overwrite
    public void setSoundPosition(LivingEntity paramls, float paramFloat) {
        if (initialized && this.gameOptions.soundVolume != 0.0F && soundSystem != null) {
            if (paramls != null) {
                float f1 = paramls.prevYaw + (paramls.yaw - paramls.prevYaw) * paramFloat;
                double d1 = paramls.prevX + (paramls.x - paramls.prevX) * (double)paramFloat;
                double d2 = paramls.prevY + (paramls.y - paramls.prevY) * (double)paramFloat;
                double d3 = paramls.prevZ + (paramls.z - paramls.prevZ) * (double)paramFloat;
                float f2 = MathHelper.cos(-f1 * (float) (Math.PI / 180.0) - (float) Math.PI);
                float f3 = MathHelper.sin(-f1 * (float) (Math.PI / 180.0) - (float) Math.PI);
                float f4 = -f3;
                float f5 = 0.0F;
                float f6 = -f2;
                float f7 = 0.0F;
                float f8 = 1.0F;
                float f9 = 0.0F;
                soundSystem.setListenerPosition((float)d1, (float)d2, (float)d3);
                soundSystem.setListenerOrientation(f4, f5, f6, f7, f8, f9);
            }
        }
    }

    /**
     * @author Risugami
     * @reason
     */
    @Overwrite
    public void playStreaming(String paramString, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5) {
        if (initialized && this.gameOptions.soundVolume != 0.0F && soundSystem != null) {
            String str = "streaming";
            if (soundSystem.playing("streaming")) {
                soundSystem.stop("streaming");
            }

            if (paramString != null) {
                SoundEntry localbh = this.streaming.getRandomSoundForId(paramString);
                if (localbh != null && paramFloat4 > 0.0F) {
                    if (soundSystem.playing("BgMusic")) {
                        soundSystem.stop("BgMusic");
                    }

                    float f1 = 16.0F;
                    soundSystem.newStreamingSource(true, str, localbh.soundUrl, localbh.soundName, false, paramFloat1, paramFloat2, paramFloat3, 2, f1 * 4.0F);
                    soundSystem.setVolume(str, 0.5F * this.gameOptions.soundVolume);
                    soundSystem.play(str);
                }

            }
        }
    }

    /**
     * @author Risugami
     * @reason
     */
    @Overwrite
    public void playSound(String paramString, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5) {
        if (initialized && this.gameOptions.soundVolume != 0.0F && soundSystem != null) {
            SoundEntry localbh = this.sounds.getRandomSoundForId(paramString);
            if (localbh != null && paramFloat4 > 0.0F) {
                this.soundUID = (this.soundUID + 1) % 256;
                String str = "sound_" + this.soundUID;
                float f1 = 16.0F;
                if (paramFloat4 > 1.0F) {
                    f1 *= paramFloat4;
                }

                soundSystem.newSource(paramFloat4 > 1.0F, str, localbh.soundUrl, localbh.soundName, false, paramFloat1, paramFloat2, paramFloat3, 2, f1);
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
     * @reason
     */
    @Overwrite
    public void playSound(String paramString, float paramFloat1, float paramFloat2) {
        if (initialized && this.gameOptions.soundVolume != 0.0F && soundSystem != null) {
            SoundEntry localbh = this.sounds.getRandomSoundForId(paramString);
            if (localbh != null) {
                this.soundUID = (this.soundUID + 1) % 256;
                String str = "sound_" + this.soundUID;
                soundSystem.newSource(false, str, localbh.soundUrl, localbh.soundName, false, 0.0F, 0.0F, 0.0F, 0, 0.0F);
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
