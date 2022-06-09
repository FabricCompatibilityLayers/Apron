package io.github.betterthanupdates.forge.mixin.modloader;

import modloader.ModLoader;
import net.minecraft.block.DispenserBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.block.DispenserBlockEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.SnowballEntity;
import net.minecraft.entity.projectile.ThrownEggEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Random;

@Mixin(DispenserBlock.class)
public class DispenserBlockMixin {

    /**
     * @author Risugami
     * @reason idk
     */
    @Overwrite
    private void dispense(World paramfd, int paramInt1, int paramInt2, int paramInt3, Random paramRandom) {
        int i = paramfd.getBlockMeta(paramInt1, paramInt2, paramInt3);
        int j = 0;
        int k = 0;
        if (i == 3) {
            k = 1;
        } else if (i == 2) {
            k = -1;
        } else if (i == 5) {
            j = 1;
        } else {
            j = -1;
        }

        DispenserBlockEntity localaz = (DispenserBlockEntity)paramfd.getBlockEntity(paramInt1, paramInt2, paramInt3);
        ItemStack localiz = localaz.getItemToDispense();
        double d1 = (double)paramInt1 + (double)j * 0.6 + 0.5;
        double d2 = (double)paramInt2 + 0.5;
        double d3 = (double)paramInt3 + (double)k * 0.6 + 0.5;
        if (localiz == null) {
            paramfd.playWorldEvent(1001, paramInt1, paramInt2, paramInt3, 0);
        } else {
            boolean handled = ModLoader.DispenseEntity(paramfd, d1, d2, d3, j, k, localiz);
            if (!handled) {
                if (localiz.itemId == Item.ARROW.id) {
                    Entity localObject = new ArrowEntity(paramfd, d1, d2, d3);
                    ((ArrowEntity)localObject).method_1291((double)j, 0.1000000014901161, (double)k, 1.1F, 6.0F);
                    ((ArrowEntity)localObject).spawnedByPlayer = true;
                    paramfd.spawnEntity(localObject);
                    paramfd.playWorldEvent(1002, paramInt1, paramInt2, paramInt3, 0);
                } else if (localiz.itemId == Item.EGG.id) {
                    Entity localObject = new ThrownEggEntity(paramfd, d1, d2, d3);
                    ((ThrownEggEntity)localObject).method_1682((double)j, 0.1000000014901161, (double)k, 1.1F, 6.0F);
                    paramfd.spawnEntity(localObject);
                    paramfd.playWorldEvent(1002, paramInt1, paramInt2, paramInt3, 0);
                } else if (localiz.itemId == Item.SNOWBALL.id) {
                    Entity localObject = new SnowballEntity(paramfd, d1, d2, d3);
                    ((SnowballEntity)localObject).method_1656((double)j, 0.1000000014901161, (double)k, 1.1F, 6.0F);
                    paramfd.spawnEntity(localObject);
                    paramfd.playWorldEvent(1002, paramInt1, paramInt2, paramInt3, 0);
                } else {
                    Entity localObject = new ItemEntity(paramfd, d1, d2 - 0.3, d3, localiz);
                    double d4 = paramRandom.nextDouble() * 0.1 + 0.2;
                    localObject.xVelocity = (double)j * d4;
                    localObject.yVelocity = 0.2000000029802322;
                    localObject.zVelocity = (double)k * d4;
                    localObject.xVelocity += paramRandom.nextGaussian() * 0.0075F * 6.0;
                    localObject.yVelocity += paramRandom.nextGaussian() * 0.0075F * 6.0;
                    localObject.zVelocity += paramRandom.nextGaussian() * 0.0075F * 6.0;
                    paramfd.spawnEntity(localObject);
                    paramfd.playWorldEvent(1000, paramInt1, paramInt2, paramInt3, 0);
                }
            }

            paramfd.playWorldEvent(2000, paramInt1, paramInt2, paramInt3, j + 1 + (k + 1) * 3);
        }

    }
}
