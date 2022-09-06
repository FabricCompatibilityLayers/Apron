package io.github.betterthanupdates.shockahpi.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterial;

import io.github.betterthanupdates.shockahpi.item.ShockAhPIAxeItem;
import io.github.betterthanupdates.shockahpi.item.ShockAhPIPickaxeItem;
import io.github.betterthanupdates.shockahpi.item.ShockAhPIShovelItem;

@Mixin(Item.class)
public class ItemMixin {
	@Shadow
	public static Item IRON_SHOVEL;

	@Shadow
	public static Item IRON_PICKAXE;

	@Shadow
	public static Item IRON_AXE;

	@Shadow
	public static Item WOOD_SHOVEL;

	@Shadow
	public static Item WOOD_PICKAXE;

	@Shadow
	public static Item WOOD_AXE;

	@Shadow
	public static Item STONE_SHOVEL;

	@Shadow
	public static Item STONE_PICKAXE;

	@Shadow
	public static Item STONE_AXE;

	@Shadow
	public static Item DIAMOND_SHOVEL;

	@Shadow
	public static Item DIAMOND_PICKAXE;

	@Shadow
	public static Item DIAMOND_AXE;

	@Shadow
	public static Item GOLD_SHOVEL;

	@Shadow
	public static Item GOLD_PICKAXE;

	@Shadow
	public static Item GOLD_AXE;

	@Shadow
	public static Item[] byId;

	@Inject(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/food/FoodItem;<init>(IIZ)V", ordinal = 1))
	private static void sapi$cinit(CallbackInfo ci) {
		byId[256] = null;
		byId[257] = null;
		byId[258] = null;

		byId[269] = null;
		byId[270] = null;
		byId[271] = null;

		byId[273] = null;
		byId[274] = null;
		byId[275] = null;

		byId[277] = null;
		byId[278] = null;
		byId[279] = null;

		byId[284] = null;
		byId[285] = null;
		byId[286] = null;

		IRON_SHOVEL = new ShockAhPIShovelItem(0, ToolMaterial.IRON).setTexturePosition(2, 5).setTranslationKey("shovelIron");
		IRON_PICKAXE = new ShockAhPIPickaxeItem(1, ToolMaterial.IRON).setTexturePosition(2, 6).setTranslationKey("pickaxeIron");
		IRON_AXE = new ShockAhPIAxeItem(2, ToolMaterial.IRON).setTexturePosition(2, 7).setTranslationKey("hatchetIron");

		WOOD_SHOVEL = new ShockAhPIShovelItem(13, ToolMaterial.WOOD).setTexturePosition(0, 5).setTranslationKey("shovelWood");
		WOOD_PICKAXE = new ShockAhPIPickaxeItem(14, ToolMaterial.WOOD).setTexturePosition(0, 6).setTranslationKey("pickaxeWood");
		WOOD_AXE = new ShockAhPIAxeItem(15, ToolMaterial.WOOD).setTexturePosition(0, 7).setTranslationKey("hatchetWood");

		STONE_SHOVEL = new ShockAhPIShovelItem(17, ToolMaterial.STONE).setTexturePosition(1, 5).setTranslationKey("shovelStone");
		STONE_PICKAXE = new ShockAhPIPickaxeItem(18, ToolMaterial.STONE).setTexturePosition(1, 6).setTranslationKey("pickaxeStone");
		STONE_AXE = new ShockAhPIAxeItem(19, ToolMaterial.STONE).setTexturePosition(1, 7).setTranslationKey("hatchetStone");

		DIAMOND_SHOVEL = new ShockAhPIShovelItem(21, ToolMaterial.DIAMOND).setTexturePosition(3, 5).setTranslationKey("shovelDiamond");
		DIAMOND_PICKAXE = new ShockAhPIPickaxeItem(22, ToolMaterial.DIAMOND).setTexturePosition(3, 6).setTranslationKey("pickaxeDiamond");
		DIAMOND_AXE = new ShockAhPIAxeItem(23, ToolMaterial.DIAMOND).setTexturePosition(3, 7).setTranslationKey("hatchetDiamond");

		GOLD_SHOVEL = new ShockAhPIShovelItem(28, ToolMaterial.GOLD).setTexturePosition(4, 5).setTranslationKey("shovelGold");
		GOLD_PICKAXE = new ShockAhPIPickaxeItem(29, ToolMaterial.GOLD).setTexturePosition(4, 6).setTranslationKey("pickaxeGold");
		GOLD_AXE = new ShockAhPIAxeItem(30, ToolMaterial.GOLD).setTexturePosition(4, 7).setTranslationKey("hatchetGold");
	}
}
