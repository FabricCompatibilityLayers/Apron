package io.github.betterthanupdates.apron;

import static fr.catcore.modremapperapi.utils.MappingsUtils.getNativeNamespace;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;

import fr.catcore.modremapperapi.ModRemappingAPI;
import fr.catcore.modremapperapi.impl.lib.mappingio.tree.MappingTree;
import fr.catcore.modremapperapi.remapping.RemapUtil;
import fr.catcore.modremapperapi.utils.MappingsUtils;

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

	public static String getRemappedFieldName(String owner, String fieldName) {
		int target = MappingsUtils.getMinecraftMappings().getNamespaceId(MappingsUtils.getTargetNamespace());
		MappingTree.ClassMapping classMapping = MappingsUtils.getMinecraftMappings().getClass(owner.replace(".", "/"), target);
		if (classMapping != null) {
			for(MappingTree.FieldMapping fieldDef : classMapping.getFields()) {
				String fieldSubName = fieldDef.getName(getNativeNamespace());
				if ((!ModRemappingAPI.BABRIC || fieldSubName != null) && Objects.equals(fieldSubName, fieldName)) {
					return fieldDef.getName(MappingsUtils.getTargetNamespace());
				}
			}
		}

		return fieldName;
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

	public static String getRemappedMethodName(String owner, String methodName, String desc) {
		int target = MappingsUtils.getMinecraftMappings().getNamespaceId(MappingsUtils.getTargetNamespace());
		MappingTree.ClassMapping classMapping = MappingsUtils.getMinecraftMappings().getClass(owner.replace(".", "/"), target);
		if (classMapping != null) {
			for(MappingTree.MethodMapping methodDef : classMapping.getMethods()) {
				String methodSubName = methodDef.getName(getNativeNamespace());
				if ((!ModRemappingAPI.BABRIC || methodSubName != null) && Objects.equals(methodSubName, methodName)) {
					String methodDescriptor = methodDef.getDesc(MappingsUtils.getTargetNamespace());
					if (methodDescriptor.startsWith(desc)) {
						return methodDef.getName(MappingsUtils.getTargetNamespace());
					}
				}
			}
		}

		return methodName;
	}

	public static boolean isModLoaded(String name) {
		try {
			Class.forName("net.minecraft." + name, false, ReflectionUtils.class.getClassLoader());
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
