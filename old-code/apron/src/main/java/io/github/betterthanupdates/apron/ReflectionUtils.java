package io.github.betterthanupdates.apron;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import io.github.fabriccompatibiltylayers.modremappingapi.api.MappingUtils;
import net.minecraft.client.render.Tessellator;

public class ReflectionUtils {
	public static Field getField(Class<?> target, String[] names) {
		for (String name : names) {
			MappingUtils.ClassMember member = MappingUtils.mapField(target, name);

			try {
				return target.getDeclaredField(member.name);
			} catch (NoSuchFieldException e) {}
		}

		return null;
	}

	public static String getRemappedFieldName(String owner, String fieldName) {
		return MappingUtils.mapFieldFromRemappedClass(owner, fieldName, null).name;
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
			MappingUtils.ClassMember member = MappingUtils.mapMethod(target, name, types);

			try {
				Method method = target.getDeclaredMethod(member.name, types);
				method.setAccessible(true);
				return method;
			} catch (NoSuchMethodException ignored) {
			}
		}

		return null;
	}

	public static String getRemappedMethodName(String owner, String methodName, String desc) {
		return MappingUtils.mapMethodFromRemappedClass(owner, methodName, desc).name;
	}

	public static boolean isModLoaded(String name) {
		try {
			Class.forName(MappingUtils.mapClass(name).replace("/", "."), false, ReflectionUtils.class.getClassLoader());
			return true;
		} catch (Exception e) {

		}

		return false;
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
