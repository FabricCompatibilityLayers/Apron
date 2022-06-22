package io.github.betterthanupdates.forge.mixin;

import java.util.Random;

import forge.ForgeHooks;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import reforged.ICustomDrop;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import io.github.betterthanupdates.forge.block.ForgeBlock;

/**
 * Default implementation of the new methods provided by Minecraft Forge.
 */
@Mixin(Block.class)
public abstract class BlockMixin implements ForgeBlock {
	@Shadow
	protected float hardness;

	@Shadow
	@Final
	public int id;

	@Shadow
	@Final
	public Material material;

	@Shadow
	public abstract boolean isFullCube();

	@Shadow
	public abstract int getDropCount(Random random);

	@Shadow
	protected abstract int droppedMeta(int i);

	@Shadow
	public abstract int getDropId(int i, Random random);

	// Forge fields
	@Unique
	private ItemStack currentItemStack;

	@Override
	public int quantityDropped(int i, Random random) {
		return this.getDropCount(random);
	}

	@Override
	public int quantityDropped(int i, Random random, ItemStack stack) {
		return stack != null && stack.getItem() instanceof ICustomDrop
				? ((ICustomDrop)stack.getItem()).getQuantityDropped((Block) (Object) this, i, random, stack)
				: this.quantityDropped(i, random);
	}

	@Override
	public int damageDropped(int i, ItemStack stack) {
		return stack != null && stack.getItem() instanceof ICustomDrop ? ((ICustomDrop)stack.getItem()).getDamageDropped((Block) (Object) this, i, stack) : this.droppedMeta(i);
	}

	@Override
	public int idDropped(int i, Random random, ItemStack stack) {
		return stack != null && stack.getItem() instanceof ICustomDrop
				? ((ICustomDrop)stack.getItem()).getIdDropped((Block) (Object) this, i, random, stack)
				: this.getDropId(i, random);
	}

	/**
	 * @author Eloraam
	 * @reason Minecraft Forge extension of this method
	 */
	@Inject(method = "getHardness(Lnet/minecraft/entity/player/PlayerEntity;)F", at = @At("RETURN"), cancellable = true)
	public void getHardness(PlayerEntity player, CallbackInfoReturnable<Float> cir) {
		cir.setReturnValue(this.blockStrength(player, 0));
	}

	int cachedL;

	@Inject(method = "beforeDestroyedByExplosion", at = @At("HEAD"))
	private void reforged$beforeDestroyedByExplosion(World i, int j, int k, int l, int f, float par6, CallbackInfo ci) {
		this.cachedL = f;
	}

	@Redirect(method = "beforeDestroyedByExplosion", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getDropCount(Ljava/util/Random;)I"))
	private int reforged$beforeDestroyedByExplosion(Block instance, Random random) {
		return ((ForgeBlock) instance).quantityDropped(this.cachedL, random, this.currentItemStack);
	}

	@Redirect(method = "beforeDestroyedByExplosion", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getDropId(ILjava/util/Random;)I"))
	private int reforged$beforeDestroyedByExplosion(Block instance, int l, Random random) {
		return ((ForgeBlock) instance).idDropped(l, random, this.currentItemStack);
	}

	@Redirect(method = "beforeDestroyedByExplosion", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;droppedMeta(I)I"))
	private int reforged$beforeDestroyedByExplosion(Block instance, int i) {
		return ((ForgeBlock) instance).damageDropped(i, this.currentItemStack);
	}

	@Inject(method = "afterBreak", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;drop(Lnet/minecraft/world/World;IIII)V"))
	private void reforged$getCurrentItemStack(World arg2, PlayerEntity i, int j, int k, int l, int par6, CallbackInfo ci) {
		this.currentItemStack = i.getHeldItem();
	}

	@Inject(method = "afterBreak", at = @At("RETURN"))
	private void reforged$afterBreak(World arg2, PlayerEntity i, int j, int k, int l, int par6, CallbackInfo ci) {
		this.currentItemStack = null;
	}

	@Override
	public boolean isLadder() {
		return false;
	}

	@Override
	public boolean isBlockNormalCube(World world, int i, int j, int k) {
		return this.material.hasNoSuffocation() && this.isFullCube();
	}

	@Override
	public boolean isBlockSolidOnSide(World world, int i, int j, int k, int side) {
		return this.isBlockNormalCube(world, i, j, k);
	}

	@Override
	public boolean isBlockReplaceable(World world, int i, int j, int k) {
		return false;
	}

	@Override
	public boolean isBlockBurning(World world, int i, int j, int k) {
		return false;
	}

	@Override
	public boolean isAirBlock(World world, int i, int j, int k) {
		return false;
	}

	@Override
	public float getHardness(int md) {
		return this.hardness;
	}

	@Override
	public float blockStrength(World world, PlayerEntity player, int i, int j, int k) {
		int md = world.getBlockMeta(i, j, k);
		return this.blockStrength(player, md);
	}

	@Override
	public float blockStrength(PlayerEntity player, int md) {
		return ForgeHooks.blockStrength((Block) (Object) this, player, md);
	}

	@Override
	public boolean canHarvestBlock(PlayerEntity player, int md) {
		return ForgeHooks.canHarvestBlock((Block) (Object) this, player, md);
	}
}
