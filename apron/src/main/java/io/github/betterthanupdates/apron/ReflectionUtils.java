package io.github.betterthanupdates.apron;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import fr.catcore.modremapperapi.remapping.RemapUtil;

import net.minecraft.client.render.Tessellator;

public class ReflectionUtils {
	public static Field getField(Class<?> target, String[] names) {
		for (Field field : target.getDeclaredFields()) {
			for (String name : names) {
				name = RemapUtil.getRemappedFieldName(target, name);

				if (field.getName().equals(name)) {
					field.setAccessible(true);
					return field;
				}
			}
		}

		return null;
	}

	public static Object getField(Class<?> clazz, Object obj, String name) {
		while (clazz != null) {
			try {
				Field field = getField(clazz, new String[]{name});
				field.setAccessible(true);
				return field.get(obj);
			} catch (Exception var4) {
				clazz = clazz.getSuperclass();
			}
		}

		return null;
	}

	public static Method getMethod(Class<?> target, String[] names, Class<?>[] types) {
		for (String name : names) {
			name = RemapUtil.getRemappedMethodName(target, name, types);

			try {
				Method method = target.getDeclaredMethod(name, types);
				method.setAccessible(true);
				return method;
			} catch (NoSuchMethodException ignored) {
			}
		}

		return null;
	}

	public static Tessellator create() {
		return new Tessellator(2097152);
	}

	public static void binder(Tessellator t) {
		Tessellator.INSTANCE = t;
	}

	public static double getXOffset(Tessellator t) {
		return t.xOffset;
	}

	public static double getYOffset(Tessellator t) {
		return t.yOffset;
	}

	public static double getZOffset(Tessellator t) {
		return t.zOffset;
	}
}
