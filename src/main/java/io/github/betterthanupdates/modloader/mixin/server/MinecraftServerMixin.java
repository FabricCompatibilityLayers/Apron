package io.github.betterthanupdates.modloader.mixin.server;

import java.util.logging.Level;
import java.util.logging.Logger;

import modloader.ModLoader;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandSource;
import net.minecraft.server.world.ServerWorld;

@Environment(EnvType.SERVER)
@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin implements Runnable, CommandSource {
	@Shadow
	protected abstract boolean start();

	@Shadow
	private boolean running;

	@Shadow
	public static Logger logger;

	@Shadow
	public ServerWorld[] worlds;

	@Shadow
	protected abstract void skipNight();

	@Shadow
	public abstract void processQueuedCommands();

	@Shadow
	protected abstract void stopServer();

	@Shadow
	public boolean stopped;

	@Inject(method = "start", at = @At(value = "INVOKE", target = "Ljava/util/logging/Logger;info(Ljava/lang/String;)V", remap = false))
	private void modloader$start(CallbackInfoReturnable<Boolean> cir) {
		ModLoader.Init((MinecraftServer) (Object) this);
	}

	/**
	 * @author Risugami
	 * @reason yes
	 */
	@Overwrite(remap = false)
	public void run() {
		try {
			if (this.start()) {
				long l = System.currentTimeMillis();

				for (long l1 = 0L; this.running; Thread.sleep(1L)) {
					ModLoader.OnTick((MinecraftServer) (Object) this);
					long l2 = System.currentTimeMillis();
					long l3 = l2 - l;

					if (l3 > 2000L) {
						logger.warning("Can't keep up! Did the system time change, or is the server overloaded?");
						l3 = 2000L;
					}

					if (l3 < 0L) {
						logger.warning("Time ran backwards! Did the system time change?");
						l3 = 0L;
					}

					l1 += l3;
					l = l2;

					if (this.worlds[0].canSkipNight()) {
						this.skipNight();
						l1 = 0L;
					} else {
						while (l1 > 50L) {
							l1 -= 50L;
							this.skipNight();
						}
					}
				}
			} else {
				while (this.running) {
					this.processQueuedCommands();

					try {
						Thread.sleep(10L);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "Unexpected exception", e);

			while (this.running) {
				this.processQueuedCommands();

				try {
					Thread.sleep(10L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} finally {
			try {
				this.stopServer();
				this.stopped = true;
			} catch (Throwable e) {
				e.printStackTrace();
			} finally {
				try {
					this.stopServer();
					this.stopped = true;
				} catch (Throwable e) {
					e.printStackTrace();
				} finally {
					System.exit(0);
				}
			}
		}
	}
}
