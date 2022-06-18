package io.github.betterthanupdates.forge.mixin;

import forge.IArmorTextureProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin extends LivingEntityRenderer {

    @Shadow public static String[] armorTypes;

    @Shadow private BipedEntityModel field_295;

    @Shadow private BipedEntityModel field_296;

    public PlayerRendererMixin(EntityModel arg, float f) {
        super(arg, f);
    }

    /**
     * @author Forge
     */
    @Overwrite
    protected boolean render(PlayerEntity entityplayer, int i, float f) {
        ItemStack itemstack = entityplayer.inventory.getArmorItem(3 - i);
        if (itemstack != null) {
            Item item = itemstack.getItem();
            if (item instanceof ArmorItem) {
                ArmorItem itemarmor = (ArmorItem)item;
                if (item instanceof IArmorTextureProvider) {
                    this.bindTexture(((IArmorTextureProvider)item).getArmorTextureFile());
                } else {
                    this.bindTexture("/armor/" + armorTypes[itemarmor.textureFileAppend] + "_" + (i != 2 ? 1 : 2) + ".png");
                }

                BipedEntityModel modelbiped = i != 2 ? this.field_295 : this.field_296;
                modelbiped.head.visible = i == 0;
                modelbiped.hat.visible = i == 0;
                modelbiped.torso.visible = i == 1 || i == 2;
                modelbiped.rightArm.visible = i == 1;
                modelbiped.leftArm.visible = i == 1;
                modelbiped.rightLeg.visible = i == 2 || i == 3;
                modelbiped.leftLeg.visible = i == 2 || i == 3;
                this.setModel(modelbiped);
                return true;
            }
        }

        return false;
    }
}
