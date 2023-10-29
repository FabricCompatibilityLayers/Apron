package io.github.betterthanupdates.apron.compat.mixin.client.betterthanwolves;

import java.util.List;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.AbstractAnimalEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.animal.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.food.FoodItem;
import net.minecraft.mod_FCBetterThanWolves;
import net.minecraft.util.io.CompoundTag;
import net.minecraft.util.math.AxixAlignedBoundingBox;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

@Mixin(WolfEntity.class)
public abstract class WolfEntityMixin extends AbstractAnimalEntity {
	@Shadow
	public abstract void setAngry(boolean bl);

	private boolean bIsFed;

	public WolfEntityMixin(World arg) {
		super(arg);
	}

	@Inject(method = "<init>", at = @At("RETURN"))
	private void btw$ctr(World par1, CallbackInfo ci) {
		this.bIsFed = false;
	}

	@Inject(method = "writeAdditional", at = @At("RETURN"))
	private void btw$writeAdditional(CompoundTag par1, CallbackInfo ci) {
		par1.put("bIsFed", this.bIsFed);
	}

	@Inject(method = "readAdditional", at = @At("RETURN"))
	private void btw$readAdditional(CompoundTag par1, CallbackInfo ci) {
		this.bIsFed = par1.getBoolean("bIsFed");
	}

	@ModifyReturnValue(method = "getMobDrops", at = @At("RETURN"))
	private int btw$getMobDrops(int original) {
		return !this.world.isClient ? mod_FCBetterThanWolves.fcWolfRaw.id : original;
	}

	@Redirect(method = "tickHandSwing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getPlayerByName(Ljava/lang/String;)Lnet/minecraft/entity/player/PlayerEntity;", ordinal = 0))
	private PlayerEntity btw$tickHandSwing(World instance, String s) {
		PlayerEntity value = instance.getPlayerByName(s);

		if (!this.world.isClient) {
			value = this.world.getClosestPlayerTo(this, -1.0);
		}

		return value;
	}

	@Inject(method = "tick", at = @At("RETURN"))
	private void btw$tick(CallbackInfo ci) {
		if (!this.world.isClient && !this.removed) {
			this.CheckForLooseFood();
			int chanceOfShitting = 1;
			if (this.IsDarkEnoughToAffectShitting()) {
				chanceOfShitting *= 2;
			}

			if (this.bIsFed) {
				chanceOfShitting *= 4;
			}

			if (this.world.rand.nextInt(96000) < chanceOfShitting) {
				this.Shit();
			}
		}
	}

	public boolean IsDarkEnoughToAffectShitting() {
		int i = MathHelper.floor(this.x);
		int j = MathHelper.floor(this.y);
		int k = MathHelper.floor(this.z);
		int lightValue = this.world.placeBlock(i, j, k);
		return lightValue <= 5;
	}

	public void CheckForLooseFood() {
		List collisionList = this.world
				.getEntities(
						ItemEntity.class, AxixAlignedBoundingBox.createAndAddToList(this.x - 2.5, this.y - 1.0, this.z - 2.5, this.x + 2.5, this.y + 1.0, this.z + 2.5)
				);
		if (!collisionList.isEmpty()) {
			for(int listIndex = 0; listIndex < collisionList.size(); ++listIndex) {
				ItemEntity entityItem = (ItemEntity)collisionList.get(listIndex);
				if (entityItem.pickupDelay == 0) {
					int iTempItemID = entityItem.stack.itemId;
					Item tempItem = Item.byId[iTempItemID];
					if (tempItem instanceof FoodItem && ((FoodItem)tempItem).canWolvesEat()) {
						this.world.playSound(this, "random.pop", 0.25F, ((this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
						entityItem.remove();
						this.addHealth(((FoodItem)Item.RAW_PORKCHOP).getHealAmount());
						this.bIsFed = true;
					}
				}
			}
		}
	}

	public void Shit() {
		float xOffset = -(-MathHelper.sin(this.yaw / 180.0F * 3.141593F) * MathHelper.cos(this.pitch / 180.0F * 3.141593F));
		float zOffset = -(MathHelper.cos(this.yaw / 180.0F * 3.141593F) * MathHelper.cos(this.pitch / 180.0F * 3.141593F));
		float yOffset = 0.25F;
		ItemEntity entityitem = new ItemEntity(
				this.world, this.x + (double)xOffset, this.y + (double)yOffset, this.z + (double)zOffset, new ItemStack(mod_FCBetterThanWolves.fcDung)
		);
		float velocityFactor = 0.05F;
		entityitem.xVelocity = (double)(
				-(-MathHelper.sin(this.yaw / 180.0F * 3.141593F) * MathHelper.cos(this.pitch / 180.0F * 3.141593F)) * 10.0F * velocityFactor
		);
		entityitem.zVelocity = (double)(
				-(MathHelper.cos(this.yaw / 180.0F * 3.141593F) * MathHelper.cos(this.pitch / 180.0F * 3.141593F)) * 10.0F * velocityFactor
		);
		entityitem.yVelocity = (double)((float)this.world.rand.nextGaussian() * velocityFactor + 0.2F);
		entityitem.pickupDelay = 10;
		this.world.spawnEntity(entityitem);
		this.world.playSound(this, "random.explode", 0.2F, 1.25F);
		this.world.playSound(this, "mob.wolf.growl", this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);

		for(int counter = 0; counter < 5; ++counter) {
			double smokeX = this.x + (double)(xOffset * 0.5F) + this.world.rand.nextDouble() * 0.25;
			double smokeY = this.y + this.world.rand.nextDouble() * 0.5 + 0.25;
			double smokeZ = this.z + (double)(zOffset * 0.5F) + this.world.rand.nextDouble() * 0.25;
			this.world.addParticle("smoke", smokeX, smokeY, smokeZ, 0.0, 0.0, 0.0);
		}

		this.bIsFed = false;
	}

	@ModifyExpressionValue(method = "damage", at = @At(value = "INVOKE", target = "Ljava/lang/String;equalsIgnoreCase(Ljava/lang/String;)Z", remap = false))
	private boolean btw$damage(boolean value) {
		return value || !this.world.isClient;
	}

	@Inject(method = "interact", at = @At(value = "RETURN", ordinal = 1))
	private void btw$interact$1(PlayerEntity par1, CallbackInfoReturnable<Boolean> cir) {
		this.bIsFed = true;
	}

	@Inject(method = "interact", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/food/FoodItem;canWolvesEat()Z"))
	private void btw$interact$2(PlayerEntity par1, CallbackInfoReturnable<Boolean> cir, @Local FoodItem itemfood) {
		if (!(itemfood.canWolvesEat() && this.dataTracker.getInt(18) < 20)) {
			if (itemfood.id == mod_FCBetterThanWolves.fcWolfRaw.id || itemfood.id == mod_FCBetterThanWolves.fcWolfCooked.id) {
				this.world.playSound(this, "mob.wolf.growl", this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
				this.setAngry(true);
			}
		}
	}

	@ModifyExpressionValue(method = "interact", at = @At(value = "INVOKE", target = "Ljava/lang/String;equalsIgnoreCase(Ljava/lang/String;)Z", remap = false))
	private boolean btw$interact$3(boolean value) {
		return value || !this.world.isClient;
	}
}
