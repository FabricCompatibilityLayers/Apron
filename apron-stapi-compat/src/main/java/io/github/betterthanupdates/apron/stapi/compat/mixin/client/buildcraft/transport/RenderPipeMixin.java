package io.github.betterthanupdates.apron.stapi.compat.mixin.client.buildcraft.transport;

import buildcraft.transport.RenderPipe;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.block.Block;
import net.minecraft.client.render.Tessellator;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.client.StationRenderAPI;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.client.texture.SpriteAtlasTexture;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = RenderPipe.class, remap = false)
public class RenderPipeMixin {
	@Redirect(method = "doRenderItem", remap = false,
			at = @At(value = "INVOKE", remap = false,
					target = "Lforge/MinecraftForgeClient;bindTexture(Ljava/lang/String;)V", ordinal = 0))
	private void fixBlockTexture0(String s, @Local(ordinal = 0) Block block) {
		if (block != null) {
			SpriteAtlasTexture atlas = StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE);
			atlas.bindTexture();
		}
	}
	@Redirect(method = "doRenderItem", remap = false,
			at = @At(value = "INVOKE", remap = false,
					target = "Lforge/MinecraftForgeClient;bindTexture(Ljava/lang/String;)V", ordinal = 1))
	private void fixBlockTexture1(String s, @Local(ordinal = 0) Block block) {
		if (block != null) {
			SpriteAtlasTexture atlas = StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE);
			atlas.bindTexture();
		}
	}

	@Redirect(method = "doRenderItem", remap = false,
			at = @At(value = "INVOKE", remap = false,
					target = "Lforge/MinecraftForgeClient;bindTexture(Ljava/lang/String;)V", ordinal = 2))
	private void fixBindItemTexture0(String s, @Local(ordinal = 0)ItemStack stack, @Share("sprite")LocalRef<Sprite> ref) {
		this.apron$fixBindItemTexture(stack, ref);
	}

	@Redirect(method = "doRenderItem", remap = false,
			at = @At(value = "INVOKE", remap = false,
					target = "Lforge/MinecraftForgeClient;bindTexture(Ljava/lang/String;)V", ordinal = 3))
	private void fixBindItemTexture1(String s, @Local(ordinal = 0)ItemStack stack, @Share("sprite")LocalRef<Sprite> ref) {
		this.apron$fixBindItemTexture(stack, ref);
	}

	@Redirect(method = "doRenderItem", remap = false,
			at = @At(value = "INVOKE", remap = false,
					target = "Lforge/MinecraftForgeClient;bindTexture(Ljava/lang/String;)V", ordinal = 4))
	private void fixBindItemTexture2(String s, @Local(ordinal = 0)ItemStack stack, @Share("sprite")LocalRef<Sprite> ref) {
		this.apron$fixBindItemTexture(stack, ref);
	}

	private void apron$fixBindItemTexture(ItemStack stack, LocalRef<Sprite> ref) {
		SpriteAtlasTexture atlas = StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE);
		atlas.bindTexture();

		ref.set(atlas.getSprite(
				stack.getItem().getAtlas().getTexture(
						stack.getItemTexture()
				).getId()
		));
	}

	@WrapOperation(method = "doRenderItem", remap = false,
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Tessellator;vertex(DDDDD)V",
			ordinal = 0, remap = true))
	private void fixItemTexture0(Tessellator instance, double x, double y, double z, double u, double v,
								 Operation<Void> operation, @Share("sprite") LocalRef<Sprite> ref) {
		Sprite sprite = ref.get();
		operation.call(instance, x, y, z, (double)sprite.getMinU(), (double)sprite.getMaxV());
	}

	@WrapOperation(method = "doRenderItem", remap = false,
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Tessellator;vertex(DDDDD)V",
			ordinal = 1))
	private void fixItemTexture1(Tessellator instance, double x, double y, double z, double u, double v,
								 Operation<Void> operation, @Share("sprite")LocalRef<Sprite> ref) {
		Sprite sprite = ref.get();
		operation.call(instance, x, y, z, (double)sprite.getMaxU(), (double)sprite.getMaxV());
	}

	@WrapOperation(method = "doRenderItem", remap = false,
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Tessellator;vertex(DDDDD)V",
			ordinal = 2))
	private void fixItemTexture2(Tessellator instance, double x, double y, double z, double u, double v,
								 Operation<Void> operation, @Share("sprite")LocalRef<Sprite> ref) {
		Sprite sprite = ref.get();
		operation.call(instance, x, y, z, (double)sprite.getMaxU(), (double)sprite.getMinV());
	}

	@WrapOperation(method = "doRenderItem", remap = false,
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Tessellator;vertex(DDDDD)V",
			ordinal = 3))
	private void fixItemTexture3(Tessellator instance, double x, double y, double z, double u, double v,
								 Operation<Void> operation, @Share("sprite")LocalRef<Sprite> ref) {
		Sprite sprite = ref.get();
		operation.call(instance, x, y, z, (double)sprite.getMinU(), (double)sprite.getMinV());
	}
}
