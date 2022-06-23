package io.github.betterthanupdates.shockahpi.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterial;

import io.github.betterthanupdates.shockahpi.item.SAPIAxeItem;
import io.github.betterthanupdates.shockahpi.item.SAPIPickaxeItem;
import io.github.betterthanupdates.shockahpi.item.SAPIShovelItem;

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

	@Inject(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/stat/Stats;setupItemStats()V"))
	private static void sapi$cinit(CallbackInfo ci) {
		IRON_SHOVEL = new SAPIShovelItem(0, ToolMaterial.IRON).setTexturePosition(2, 5).setTranslationKey("shovelIron");
		IRON_PICKAXE = new SAPIPickaxeItem(1, ToolMaterial.IRON).setTexturePosition(2, 6).setTranslationKey("pickaxeIron");
		IRON_AXE = new SAPIAxeItem(2, ToolMaterial.IRON).setTexturePosition(2, 7).setTranslationKey("hatchetIron");

		WOOD_SHOVEL = new SAPIShovelItem(13, ToolMaterial.WOOD).setTexturePosition(0, 5).setTranslationKey("shovelWood");
		WOOD_PICKAXE = new SAPIPickaxeItem(14, ToolMaterial.WOOD).setTexturePosition(0, 6).setTranslationKey("pickaxeWood");
		WOOD_AXE = new SAPIAxeItem(15, ToolMaterial.WOOD).setTexturePosition(0, 7).setTranslationKey("hatchetWood");

		STONE_SHOVEL = new SAPIShovelItem(17, ToolMaterial.STONE).setTexturePosition(1, 5).setTranslationKey("shovelStone");
		STONE_PICKAXE = new SAPIPickaxeItem(18, ToolMaterial.STONE).setTexturePosition(1, 6).setTranslationKey("pickaxeStone");
		STONE_AXE = new SAPIAxeItem(19, ToolMaterial.STONE).setTexturePosition(1, 7).setTranslationKey("hatchetStone");

		DIAMOND_SHOVEL = new SAPIShovelItem(21, ToolMaterial.DIAMOND).setTexturePosition(3, 5).setTranslationKey("shovelDiamond");
		DIAMOND_PICKAXE = new SAPIPickaxeItem(22, ToolMaterial.DIAMOND).setTexturePosition(3, 6).setTranslationKey("pickaxeDiamond");
		DIAMOND_AXE = new SAPIAxeItem(23, ToolMaterial.DIAMOND).setTexturePosition(3, 7).setTranslationKey("hatchetDiamond");

		GOLD_SHOVEL = new SAPIShovelItem(28, ToolMaterial.GOLD).setTexturePosition(4, 5).setTranslationKey("shovelGold");
		GOLD_PICKAXE = new SAPIPickaxeItem(29, ToolMaterial.GOLD).setTexturePosition(4, 6).setTranslationKey("pickaxeGold");
		GOLD_AXE = new SAPIAxeItem(30, ToolMaterial.GOLD).setTexturePosition(4, 7).setTranslationKey("hatchetGold");
	}
}
