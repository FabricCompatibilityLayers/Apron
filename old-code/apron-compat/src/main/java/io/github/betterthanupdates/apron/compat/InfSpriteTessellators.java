package io.github.betterthanupdates.apron.compat;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;

import net.mine_diver.infsprites.render.TextureManager;
import net.mine_diver.infsprites.util.Util;
import net.mine_diver.infsprites.util.compatibility.ForgePatcher;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.render.Tessellator;

import io.github.betterthanupdates.apron.ReflectionUtils;
import io.github.betterthanupdates.forge.ForgeClientReflection;

public class InfSpriteTessellators {
	private static final MethodHandles.Lookup TESSELLATOR_LOOKUP = Util.IMPL_LOOKUP.in(Tessellator.class);
	private static Supplier<Tessellator>[] tessellators = new Supplier[]{() -> Tessellator.INSTANCE};
	private static final Set<Integer> ACTIVE_WORLD_TESSELLATORS = new HashSet();
	private static final Supplier<Tessellator> TESSELLATOR_FACTORY;
	private static final Supplier<Tessellator> VANILLA_TESSELLATOR;
	private static final Consumer<Tessellator> TESSELLATOR_BINDER;
	private static final MethodHandle X_OFFSET_GETTER_HANDLE;
	private static final MethodHandle Y_OFFSET_GETTER_HANDLE;
	private static final MethodHandle Z_OFFSET_GETTER_HANDLE;
	private static final ToDoubleFunction<Tessellator> X_OFFSET_GETTER;
	private static final ToDoubleFunction<Tessellator> Y_OFFSET_GETTER;
	private static final ToDoubleFunction<Tessellator> Z_OFFSET_GETTER;

	public InfSpriteTessellators() {
	}

	public static Supplier<Tessellator> get(int terrainId) {
		if (terrainId >= tessellators.length) {
			tessellators = (Supplier[]) Arrays.copyOf(tessellators, terrainId + 1);
		}

		Supplier<Tessellator> tessellator = tessellators[terrainId];
		if (tessellator == null) {
			Tessellator newTessellator = (Tessellator)TESSELLATOR_FACTORY.get();
			tessellator = tessellators[terrainId] = () -> newTessellator;
		}

		return tessellator;
	}

	public static void bindTessellator(Tessellator tessellator) {
		if (tessellator != Tessellator.INSTANCE) {
			TESSELLATOR_BINDER.accept(tessellator);
		}
	}

	public static void unbindTessellator() {
		TESSELLATOR_BINDER.accept(VANILLA_TESSELLATOR.get());
	}

	public static Tessellator getWorldTessellator(int terrainId) {
		if (terrainId == 0) {
			return Tessellator.INSTANCE;
		} else {
			Tessellator tessellator = (Tessellator)get(terrainId).get();
			if (ACTIVE_WORLD_TESSELLATORS.add(terrainId)) {
				tessellator.startQuads();
				Tessellator vanillaTessellator = (Tessellator)VANILLA_TESSELLATOR.get();
				tessellator.translate(
						X_OFFSET_GETTER.applyAsDouble(vanillaTessellator),
						Y_OFFSET_GETTER.applyAsDouble(vanillaTessellator),
						Z_OFFSET_GETTER.applyAsDouble(vanillaTessellator)
				);
			}

			return tessellator;
		}
	}

	public static void drawActiveWorldTessellators() {
		ACTIVE_WORLD_TESSELLATORS.forEach(terrainId -> {
			GL11.glBindTexture(3553, TextureManager.getTerrain(terrainId));
			((Tessellator)tessellators[terrainId].get()).draw();
		});
		ACTIVE_WORLD_TESSELLATORS.clear();
		GL11.glBindTexture(3553, TextureManager.getTerrain(0));
	}

	static {
		if (ForgePatcher.installed) {
			TESSELLATOR_FACTORY = ReflectionUtils::create;
			VANILLA_TESSELLATOR = () -> ForgeClientReflection.Tessellator$firstInstance;
			TESSELLATOR_BINDER = ReflectionUtils::binder;
			X_OFFSET_GETTER_HANDLE = null;
			Y_OFFSET_GETTER_HANDLE = null;
			Z_OFFSET_GETTER_HANDLE = null;
			X_OFFSET_GETTER = ReflectionUtils::getXOffset;
			Y_OFFSET_GETTER = ReflectionUtils::getYOffset;
			Z_OFFSET_GETTER = ReflectionUtils::getZOffset;
		} else {
			throw new RuntimeException("Oh No!");
		}
	}
}
