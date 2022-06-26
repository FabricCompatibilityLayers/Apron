package shockahpi;

import playerapi.PlayerBase;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.stat.achievement.Achievements;

import io.github.betterthanupdates.Legacy;
import io.github.betterthanupdates.shockahpi.client.entity.player.ShockAhPIClientPlayerEntity;

@Legacy
public class PlayerBaseSAPI extends PlayerBase {
	
	public int portal;

	
	public PlayerBaseSAPI(AbstractClientPlayerEntity p) {
		super(p);
	}

	
	public boolean onLivingUpdate() {
		Minecraft mc = this.player.client;

		if (!mc.statFileWriter.isAchievementUnlocked(Achievements.OPEN_INVENTORY)) {
			mc.achievement.setAchievement(Achievements.OPEN_INVENTORY);
		}

		this.player.field_505 = this.player.field_504;

		if (this.portal != 0) {
			DimensionBase dimensionbase = DimensionBase.getDimByNumber(this.portal);

			if (this.player.field_512) {
				if (!this.player.world.isClient && this.player.vehicle != null) {
					this.player.startRiding(null);
				}

				if (mc.currentScreen != null) {
					mc.openScreen(null);
				}

				if (this.player.field_504 == 0.0F) {
					mc.soundHelper.playSound(dimensionbase.soundTrigger, 1.0F, this.player.rand.nextFloat() * 0.4F + 0.8F);
				}

				this.player.field_504 += 0.0125F;

				if (this.player.field_504 >= 1.0F) {
					this.player.field_504 = 1.0F;

					if (!this.player.world.isClient) {
						this.player.field_511 = 10;
						mc.soundHelper.playSound(dimensionbase.soundTravel, 1.0F, this.player.rand.nextFloat() * 0.4F + 0.8F);
						DimensionBase.usePortal(this.portal);
					}
				}

				this.player.field_512 = false;
			} else {
				if (this.player.field_504 > 0.0F) {
					this.player.field_504 -= 0.05F;
				}

				if (this.player.field_504 < 0.0F) {
					this.player.field_504 = 0.0F;
				}
			}
		}

		if (this.player.field_511 > 0) {
			--this.player.field_511;
		}

		this.player.playerKeypressManager.updatePlayer(this.player);

		if (this.player.playerKeypressManager.sneak && this.player.field_1640 < 0.2F) {
			this.player.field_1640 = 0.2F;
		}

		this.player
				.method_1372(this.player.x - (double) this.player.width * 0.35, this.player.boundingBox.minY + 0.5, this.player.z + (double) this.player.width * 0.35);
		this.player
				.method_1372(this.player.x - (double) this.player.width * 0.35, this.player.boundingBox.minY + 0.5, this.player.z - (double) this.player.width * 0.35);
		this.player
				.method_1372(this.player.x + (double) this.player.width * 0.35, this.player.boundingBox.minY + 0.5, this.player.z - (double) this.player.width * 0.35);
		this.player
				.method_1372(this.player.x + (double) this.player.width * 0.35, this.player.boundingBox.minY + 0.5, this.player.z + (double) this.player.width * 0.35);
		((ShockAhPIClientPlayerEntity) this.player).superOnLivingUpdate();
		return true;
	}

	
	public boolean respawn() {
		DimensionBase.respawn(false, 0);
		return true;
	}
}
