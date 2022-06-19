package io.github.betterthanupdates.shockahpi.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import shockahpi.SAPI;

import net.minecraft.client.CreativeClientInteractionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.AxixAlignedBoundingBox;
import net.minecraft.util.math.Vec3d;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

	@Shadow
	private Minecraft client;

	@Shadow
	private Entity field_2352;

	/**
	 * @author
	 * @reason
	 */
	@Overwrite
	public void method_1838(float f) {
		if (this.client.viewEntity != null) {
			if (this.client.world != null) {
				double d = (double)this.client.interactionManager.getBlockReachDistance();
				this.client.hitResult = this.client.viewEntity.raytrace(d, f);
				double d1 = d;
				Vec3d vec3d = this.client.viewEntity.getPosition(f);
				if (this.client.hitResult != null) {
					d1 = this.client.hitResult.field_1988.distanceTo(vec3d);
				}

				if (this.client.interactionManager instanceof CreativeClientInteractionManager) {
					d = 32.0;
					d1 = 32.0;
				} else {
					float reach = SAPI.reachGetEntity();
					if (d1 > (double)reach) {
						d1 = (double)reach;
					}

					d = d1;
				}

				Vec3d vec3d1 = this.client.viewEntity.getRotation(f);
				Vec3d vec3d2 = vec3d.translate(vec3d1.x * d, vec3d1.y * d, vec3d1.z * d);
				this.field_2352 = null;
				float f1 = 1.0F;
				List<Entity> list = this.client
						.world
						.getEntities(
								this.client.viewEntity,
								this.client
										.viewEntity
										.boundingBox
										.duplicateAndExpand(vec3d1.x * d, vec3d1.y * d, vec3d1.z * d)
										.expand((double)f1, (double)f1, (double)f1)
						);
				double d2 = 0.0;

				for (Entity entity : list) {
					if (entity.method_1356()) {
						float f2 = entity.method_1369();
						AxixAlignedBoundingBox axisalignedbb = entity.boundingBox.expand((double) f2, (double) f2, (double) f2);
						HitResult movingobjectposition = axisalignedbb.method_89(vec3d, vec3d2);
						if (axisalignedbb.contains(vec3d)) {
							if (0.0 < d2 || d2 == 0.0) {
								this.field_2352 = entity;
								d2 = 0.0;
							}
						} else if (movingobjectposition != null) {
							double d3 = vec3d.distanceTo(movingobjectposition.field_1988);
							if (d3 < d2 || d2 == 0.0) {
								this.field_2352 = entity;
								d2 = d3;
							}
						}
					}
				}

				if (this.field_2352 != null && !(this.client.interactionManager instanceof CreativeClientInteractionManager)) {
					this.client.hitResult = new HitResult(this.field_2352);
				}

			}
		}
	}

	@Inject(method = "tick", at = @At("HEAD"))
	private void sapi$tick(float par1, CallbackInfo ci) {
		if (this.client.world != null) {
			this.client.world.autoSaveInterval = 6000;
		}
	}
}
