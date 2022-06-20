package io.github.betterthanupdates.forge.mixin.client;

import forge.IArmorTextureProvider;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@Environment(EnvType.CLIENT)
@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin extends LivingEntityRenderer {
	@Shadow
	public static String[] armorTypes;

	@Shadow
	private BipedEntityModel field_295;

	@Shadow
	private BipedEntityModel field_296;

	public PlayerRendererMixin(EntityModel arg, float f) {
		super(arg, f);
	}

	/**
	 * @author Forge
	 * @reason
	 */
	@Overwrite
	protected boolean render(PlayerEntity player, int i, float f) {
		ItemStack itemstack = player.inventory.getArmorItem(3 - i);

		if (itemstack != null) {
			Item item = itemstack.getItem();

			if (item instanceof ArmorItem) {
				ArmorItem armor = (ArmorItem) item;

				if (item instanceof IArmorTextureProvider) {
					this.bindTexture(((IArmorTextureProvider) item).getArmorTextureFile());
				} else {
					this.bindTexture("/armor/" + armorTypes[armor.textureFileAppend] + "_" + (i != 2 ? 1 : 2) + ".png");
				}

				BipedEntityModel bipedModel = i != 2 ? this.field_295 : this.field_296;
				bipedModel.head.visible = i == 0;
				bipedModel.hat.visible = i == 0;
				bipedModel.torso.visible = i == 1 || i == 2;
				bipedModel.rightArm.visible = i == 1;
				bipedModel.leftArm.visible = i == 1;
				bipedModel.rightLeg.visible = i == 2 || i == 3;
				bipedModel.leftLeg.visible = i == 2 || i == 3;
				this.setModel(bipedModel);
				return true;
			}
		}

		return false;
	}
}
