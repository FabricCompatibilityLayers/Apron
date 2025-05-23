package shockahpi;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ClientPlayerEntity;
import playerapi.PlayerBase;
import net.minecraft.achievement.Achievements;
import net.minecraft.client.Minecraft;
import io.github.betterthanupdates.Legacy;
import io.github.betterthanupdates.playerapi.client.entity.player.PlayerAPIClientPlayerEntity;

@Legacy
public class PlayerBaseSAPI extends PlayerBase {
	public int portal;

	public PlayerBaseSAPI(ClientPlayerEntity p) {
		super(p);
	}

	public boolean onLivingUpdate() {
		if (FabricLoader.getInstance().isModLoaded("station-dimensions-v0")) return false;

		Minecraft mc = this.player.minecraft;
		if (!mc.field_2773.method_1988(Achievements.OPEN_INVENTORY)) {
			mc.field_2819.method_1966(Achievements.OPEN_INVENTORY);
		}

		this.player.field_505 = this.player.field_504;
		if (this.portal != 0) {
			DimensionBase dimensionbase = DimensionBase.getDimByNumber(this.portal);
			ClientPlayerEntity var10000;
			if (dimensionbase != null && this.player.field_512) {
				if (!this.player.world.isRemote && this.player.field_1595 != null) {
					this.player.method_1376((Entity) null);
				}

				if (mc.currentScreen != null) {
					mc.setScreen((Screen) null);
				}

				if (this.player.field_504 == 0.0F) {
					mc.soundManager.method_2009(dimensionbase.soundTrigger, 1.0F, this.player.random.nextFloat() * 0.4F + 0.8F);
				}

				var10000 = this.player;
				var10000.field_504 += 0.0125F;
				if (this.player.field_504 >= 1.0F) {
					this.player.field_504 = 1.0F;
					if (!this.player.world.isRemote) {
						this.player.field_511 = 10;
						mc.soundManager.method_2009(dimensionbase.soundTravel, 1.0F, this.player.random.nextFloat() * 0.4F + 0.8F);
						DimensionBase.usePortal(this.portal);
					}
				}

				this.player.field_512 = false;
			} else {
				if (this.player.field_504 > 0.0F) {
					var10000 = this.player;
					var10000.field_504 -= 0.05F;
				}

				if (this.player.field_504 < 0.0F) {
					this.player.field_504 = 0.0F;
				}
			}
		}

		if (this.player.field_511 > 0) {
			--this.player.field_511;
		}

		this.player.field_161.method_1942(this.player);
		if (this.player.field_161.field_2536 && this.player.field_1640 < 0.2F) {
			this.player.field_1640 = 0.2F;
		}

		this.player.pushOutOfBlock(this.player.x - (double)this.player.spacingXZ * 0.35, this.player.boundingBox.minY + 0.5, this.player.z + (double)this.player.spacingXZ * 0.35);
		this.player.pushOutOfBlock(this.player.x - (double)this.player.spacingXZ * 0.35, this.player.boundingBox.minY + 0.5, this.player.z - (double)this.player.spacingXZ * 0.35);
		this.player.pushOutOfBlock(this.player.x + (double)this.player.spacingXZ * 0.35, this.player.boundingBox.minY + 0.5, this.player.z - (double)this.player.spacingXZ * 0.35);
		this.player.pushOutOfBlock(this.player.x + (double)this.player.spacingXZ * 0.35, this.player.boundingBox.minY + 0.5, this.player.z + (double)this.player.spacingXZ * 0.35);
		((PlayerAPIClientPlayerEntity) this.player).superOnLivingUpdate();
		return true;
	}

	public boolean respawn() {
		if (FabricLoader.getInstance().isModLoaded("station-dimensions-v0")) return false;

		DimensionBase.respawn(false, 0);
		return true;
	}
}
