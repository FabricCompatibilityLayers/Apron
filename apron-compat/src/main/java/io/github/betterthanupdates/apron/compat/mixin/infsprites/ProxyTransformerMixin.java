package io.github.betterthanupdates.apron.compat.mixin.infsprites;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.stream.Collectors;

import net.mine_diver.infsprites.api.Identifier;
import net.mine_diver.infsprites.api.Patchers;
import net.mine_diver.infsprites.proxy.transform.ProxyTransformer;
import net.mine_diver.infsprites.util.Util;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import overrideapi.proxy.asm.ClassReader;
import overrideapi.proxy.asm.ClassWriter;
import overrideapi.proxy.asm.tree.AbstractInsnNode;
import overrideapi.proxy.asm.tree.AnnotationNode;
import overrideapi.proxy.asm.tree.ClassNode;
import overrideapi.proxy.asm.tree.FieldInsnNode;
import overrideapi.proxy.asm.tree.FieldNode;
import overrideapi.proxy.asm.tree.MethodInsnNode;
import overrideapi.proxy.asm.tree.MethodNode;

import io.github.betterthanupdates.apron.ReflectionUtils;

@Mixin(ProxyTransformer.class)
public abstract class ProxyTransformerMixin {
	@Shadow(remap = false)
	private static byte[] readClassBytes(Class<?> classObject) {
		return new byte[0];
	}

	@Shadow(remap = false)
	private static void log(String message) {
	}

	@Shadow(remap = false)
	@Final
	private static String SHADOW;

	@Shadow(remap = false)
	private static String toTarget(Class<?> owner, FieldNode field) {
		return null;
	}

	@Shadow(remap = false)
	private static String toTargetCustomName(Class<?> owner, FieldNode field, String fieldName) {
		return null;
	}

	@Shadow(remap = false)
	private static String toTarget(Class<?> owner, MethodNode method) {
		return null;
	}

	@Shadow(remap = false)
	private static String toTargetCustomName(Class<?> owner, MethodNode method, String methodName) {
		return null;
	}

	@Shadow(remap = false)
	private static String toTargetInsn(FieldInsnNode fieldInsn) {
		return null;
	}

	@Shadow(remap = false)
	private static String toTargetInsn(MethodInsnNode methodInsn) {
		return null;
	}

	/**
	 * @author Cat Core
	 * @reason remap annotation fields on the fly
	 */
	@Overwrite(remap = false)
	private static <S, T extends S> byte[] transform(Class<T> proxyClass, Class<S> targetClass) {
		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(readClassBytes(proxyClass));
		classReader.accept(classNode, 0);
		Map<String, String> shadowFieldRemap = new HashMap();
		Map<String, String> shadowMethodRemap = new HashMap();
		log(proxyClass.getName() + " -> " + targetClass.getName());
		log("Building shadow field remap map...");
		((List)((List)classNode.fields).stream().filter(field -> ((List)((FieldNode)field).invisibleAnnotations) != null).collect(Collectors.toList()))
				.forEach(field -> ((List)((FieldNode)field).invisibleAnnotations).stream().filter(ann -> SHADOW.equals(((AnnotationNode)ann).desc)).findFirst().ifPresent(ann -> {
					FieldNode feild = (FieldNode) field;
					AnnotationNode nan = (AnnotationNode) ann;
					classNode.fields.remove(feild);
					String fieldName = feild.name;
					String rawPatcher = "";
					if (nan.values != null) {
						for(int i = 0; i < nan.values.size(); i += 2) {
							String var9 = (String)nan.values.get(i);
							switch(var9) {
							case "obfuscatedName":
								if (!Util.workspace) {
									fieldName = ReflectionUtils.getRemappedFieldName(targetClass.getName(), (String)nan.values.get(i + 1));
								}
								break;
							case "requiredPatcher":
								rawPatcher = (String)nan.values.get(i + 1);
							}
						}
					}

					if (!rawPatcher.isEmpty() && !Patchers.PATCHED_VIEW.contains(Identifier.of(rawPatcher))) {
						log(" - Skipping field \"" + feild.name + "\" because \"" + rawPatcher + "\" patcher isn't initialized");
					} else {
						shadowFieldRemap.put(toTarget(proxyClass, feild), toTargetCustomName(targetClass, feild, fieldName));
						log(" - " + proxyClass.getSimpleName() + ";" + feild.name + " -> " + targetClass.getSimpleName() + ";" + fieldName);
					}
				}));
		log("Built!");
		log("Building shadow method remap map...");
		((List)((List)classNode.methods).stream().filter(method -> ((List)((MethodNode)method).invisibleAnnotations) != null).collect(Collectors.toList()))
				.forEach(method -> ((List)((MethodNode)method).invisibleAnnotations).stream().filter(ann -> SHADOW.equals(((AnnotationNode)ann).desc)).findFirst().ifPresent(ann -> {
					MethodNode metodh = (MethodNode) method;
					AnnotationNode nan = (AnnotationNode) ann;
					classNode.methods.remove(metodh);
					String methodName = metodh.name;
					String rawPatcher = "";
					if (nan.values != null) {
						for(int i = 0; i < nan.values.size(); i += 2) {
							String var9 = (String)nan.values.get(i);
							switch(var9) {
							case "obfuscatedName":
								if (!Util.workspace) {
									methodName = ReflectionUtils.getRemappedMethodName(targetClass.getName(), (String)nan.values.get(i + 1), metodh.desc);
								}
								break;
							case "requiredPatcher":
								rawPatcher = (String)nan.values.get(i + 1);
							}
						}
					}

					if (!rawPatcher.isEmpty() && !Patchers.PATCHED_VIEW.contains(Identifier.of(rawPatcher))) {
						log(" - Skipping method \"" + metodh.name + "\" because \"" + rawPatcher + "\" patcher isn't initialized");
					} else {
						shadowMethodRemap.put(toTarget(proxyClass, metodh), toTargetCustomName(targetClass, metodh, methodName));
						log(" - " + proxyClass.getSimpleName() + ";" + metodh.name + " -> " + targetClass.getSimpleName() + ";" + methodName);
					}
				}));
		log("Built!");
		log("Remapping methods...");
		((List)classNode.methods).forEach(method -> {
			MethodNode metodh = (MethodNode) method;
			log(" - " + metodh.name + "...");
			((ListIterator)metodh.instructions.iterator()).forEachRemaining(insn -> {
				AbstractInsnNode inns = (AbstractInsnNode) insn;
				switch(inns.getType()) {
				case 4:
					FieldInsnNode fieldInsn = (FieldInsnNode)inns;
					String fieldTarget = toTargetInsn(fieldInsn);
					if (shadowFieldRemap.containsKey(fieldTarget)) {
						String remapped = (String)shadowFieldRemap.get(fieldTarget);
						int ownerEndIndex = remapped.indexOf(59);
						int nameEndIndex = remapped.indexOf(58);
						fieldInsn.owner = remapped.substring(1, ownerEndIndex);
						fieldInsn.name = remapped.substring(ownerEndIndex + 1, nameEndIndex);
						fieldInsn.desc = remapped.substring(nameEndIndex + 1);
					}
					break;
				case 5:
					MethodInsnNode methodInsn = (MethodInsnNode)inns;
					String methodTarget = toTargetInsn(methodInsn);
					if (shadowMethodRemap.containsKey(methodTarget)) {
						String remapped = (String)shadowMethodRemap.get(methodTarget);
						int ownerEndIndex = remapped.indexOf(59);
						int nameEndIndex = remapped.indexOf(40);
						methodInsn.owner = remapped.substring(1, ownerEndIndex);
						methodInsn.name = remapped.substring(ownerEndIndex + 1, nameEndIndex);
						methodInsn.desc = remapped.substring(nameEndIndex);
					}
				}
			});
		});
		log("Remapped!");
		log("Finalizing proxy class...");
		if (Modifier.isAbstract(classNode.access)) {
			classNode.access &= -1025;
		}

		ClassWriter classWriter = new ClassWriter(3);
		classNode.accept(classWriter);
		byte[] classBytes = classWriter.toByteArray();
		log("Finalized!");
		return classBytes;
	}
}
