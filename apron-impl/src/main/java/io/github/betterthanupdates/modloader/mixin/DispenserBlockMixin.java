package io.github.betterthanupdates.modloader.mixin;

import java.util.Random;

import modloader.ModLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.minecraft.block.DispenserBlock;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.block.DispenserBlockEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.SnowballEntity;
import net.minecraft.entity.projectile.ThrownEggEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@Mixin(DispenserBlock.class)
public abstract class DispenserBlockMixin {
	/**
	 * @author Risugami
	 * @reason Implements {@link ModLoader#DispenseEntity(World, double, double, double, int, int, ItemStack)
	 * ModLoader's dispense entity callback}
	 */
	// TODO(halotroop2288): Rewrite as an {@link Inject} Mixin.
	@Overwrite
	private void dispense(World world, int x, int y, int z, Random random) {
		int meta = world.getBlockMeta(x, y, z);
		int j = 0;
		int k = 0;

		if (meta == 3) {
			k = 1;
		} else if (meta == 2) {
			k = -1;
		} else if (meta == 5) {
			j = 1;
		} else {
			j = -1;
		}

		DispenserBlockEntity dispenser = (DispenserBlockEntity) world.getBlockEntity(x, y, z);
		ItemStack itemStack = dispenser.getItemToDispense();
		double posX = (double) x + (double) j * 0.6 + 0.5;
		double posY = (double) y + 0.5;
		double posZ = (double) z + (double) k * 0.6 + 0.5;

		if (itemStack == null) {
			world.playWorldEvent(1001, x, y, z, 0);
		} else {
			boolean handled = ModLoader.DispenseEntity(world, posX, posY, posZ, j, k, itemStack);

			if (!handled) {
				if (itemStack.itemId == Item.ARROW.id) {
					ArrowEntity arrow = new ArrowEntity(world, posX, posY, posZ);
					arrow.method_1291(j, 0.1f, k, 1.1f, 6.0f);
					arrow.spawnedByPlayer = true;
					world.spawnEntity(arrow);
					world.playWorldEvent(1002, x, y, z, 0);
				} else if (itemStack.itemId == Item.EGG.id) {
					ThrownEggEntity thrownEgg = new ThrownEggEntity(world, posX, posY, posZ);
					thrownEgg.method_1682(j, 0.1f, k, 1.1f, 6.0f);
					world.spawnEntity(thrownEgg);
					world.playWorldEvent(1002, x, y, z, 0);
				} else if (itemStack.itemId == Item.SNOWBALL.id) {
					SnowballEntity snowball = new SnowballEntity(world, posX, posY, posZ);
					snowball.method_1656(j, 0.1f, k, 1.1f, 6.0f);
					world.spawnEntity(snowball);
					world.playWorldEvent(1002, x, y, z, 0);
				} else {
					ItemEntity itemEntity = new ItemEntity(world, posX, posY - 0.3d, posZ, itemStack);
					double d4 = random.nextDouble() * 0.1d + 0.2d;
					itemEntity.xVelocity = (double) j * d4;
					itemEntity.yVelocity = 0.2d;
					itemEntity.zVelocity = (double) k * d4;
					itemEntity.xVelocity += random.nextGaussian() * 0.0075f * 6.0f;
					itemEntity.yVelocity += random.nextGaussian() * 0.0075f * 6.0f;
					itemEntity.zVelocity += random.nextGaussian() * 0.0075f * 6.0f;
					world.spawnEntity(itemEntity);
					world.playWorldEvent(1000, x, y, z, 0);
				}
			}

			world.playWorldEvent(2000, x, y, z, j + 1 + (k + 1) * 3);
		}
	}
}
