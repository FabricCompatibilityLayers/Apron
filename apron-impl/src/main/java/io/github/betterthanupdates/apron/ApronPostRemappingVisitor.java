package io.github.betterthanupdates.apron;

import fr.catcore.modremapperapi.api.ApplyVisitorProvider;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.TypePath;

public class ApronPostRemappingVisitor implements ApplyVisitorProvider {
	@Override
	public ClassVisitor insertApplyVisitor(String className, ClassVisitor next) {
		return new ClassVisitor(Opcodes.ASM9, next) {
			@Override
			public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
				if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
					switch (superName) {
						case "net/minecraft/class_632":
							superName = "io/github/betterthanupdates/shockahpi/item/ShockAhPIToolItem";
							break;
						case "net/minecraft/class_420":
							superName = "io/github/betterthanupdates/shockahpi/item/ShockAhPIAxeItem";
							break;
						case "net/minecraft/class_116":
							superName = "io/github/betterthanupdates/shockahpi/item/ShockAhPIPickaxeItem";
							break;
						case "net/minecraft/class_501":
							superName = "io/github/betterthanupdates/shockahpi/item/ShockAhPIShovelItem";
							break;
					}
				}

				super.visit(version, access, name, signature, superName, interfaces);
			}

			@Override
			public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
				if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
					switch (descriptor) {
						case "net/minecraft/class_632":
							descriptor = "io/github/betterthanupdates/shockahpi/item/ShockAhPIToolItem";
							break;
						case "net/minecraft/class_420":
							descriptor = "io/github/betterthanupdates/shockahpi/item/ShockAhPIAxeItem";
							break;
						case "net/minecraft/class_116":
							descriptor = "io/github/betterthanupdates/shockahpi/item/ShockAhPIPickaxeItem";
							break;
						case "net/minecraft/class_501":
							descriptor = "io/github/betterthanupdates/shockahpi/item/ShockAhPIShovelItem";
							break;
					}
				}

				return super.visitTypeAnnotation(typeRef, typePath, descriptor, visible);
			}

			@Override
			public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
				return new MethodVisitor(Opcodes.ASM9, super.visitMethod(access, name, descriptor, signature, exceptions)) {
					@Override
					public void visitTypeInsn(int opcode, String type) {
						if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
							switch (type) {
								case "net/minecraft/class_632":
									type = "io/github/betterthanupdates/shockahpi/item/ShockAhPIToolItem";
									break;
								case "net/minecraft/class_420":
									type = "io/github/betterthanupdates/shockahpi/item/ShockAhPIAxeItem";
									break;
								case "net/minecraft/class_116":
									type = "io/github/betterthanupdates/shockahpi/item/ShockAhPIPickaxeItem";
									break;
								case "net/minecraft/class_501":
									type = "io/github/betterthanupdates/shockahpi/item/ShockAhPIShovelItem";
									break;
							}
						}

						super.visitTypeInsn(opcode, type);
					}

					@Override
					public void visitFieldInsn(int opcode, String fieldOwner, String fieldName, String fieldDescriptor) {
						if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
							switch (fieldOwner) {
								case "net/minecraft/class_632":
									fieldOwner = "io/github/betterthanupdates/shockahpi/item/ShockAhPIToolItem";
									break;
								case "net/minecraft/class_420":
									fieldOwner = "io/github/betterthanupdates/shockahpi/item/ShockAhPIAxeItem";
									break;
								case "net/minecraft/class_116":
									fieldOwner = "io/github/betterthanupdates/shockahpi/item/ShockAhPIPickaxeItem";
									break;
								case "net/minecraft/class_501":
									fieldOwner = "io/github/betterthanupdates/shockahpi/item/ShockAhPIShovelItem";
									break;
							}
						}

						switch (fieldOwner) {
							case "net/minecraft/class_67":
								if (fieldName.equals("renderingWorldRenderer")) {
									fieldOwner = "io/github/betterthanupdates/forge/ForgeClientReflection";
									fieldName = "Tessellator$renderingWorldRenderer";
								} else if (fieldName.equals("firstInstance")) {
									fieldOwner = "io/github/betterthanupdates/forge/ForgeClientReflection";
									fieldName = "Tessellator$firstInstance";
								}

								break;
							case "net/minecraft/class_13":
								if (fieldName.equals("cfgGrassFix")) {
									fieldOwner = "io/github/betterthanupdates/forge/ForgeClientReflection";
									fieldName = "BlockRenderer$cfgGrassFix";
								} else if (fieldName.equals("redstoneColors")) {
									fieldOwner = "io/github/betterthanupdates/forge/ForgeClientReflection";
									fieldName = "BlockRenderer$redstoneColors";
								}

								break;
							case "net/minecraft/class_328":
								if (fieldName.equals("disableValidation")) {
									fieldOwner = "io/github/betterthanupdates/forge/ForgeReflection";
									fieldName = "TrapdoorBlock$disableValidation";
								}

								break;
						}

						super.visitFieldInsn(opcode, fieldOwner, fieldName, fieldDescriptor);
					}

					@Override
					public void visitMethodInsn(int opcode, String methodOwner, String methodName, String methodDescriptor, boolean isInterface) {
						if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
							switch (methodOwner) {
								case "net/minecraft/class_632":
									methodOwner = "io/github/betterthanupdates/shockahpi/item/ShockAhPIToolItem";
									break;
								case "net/minecraft/class_420":
									methodOwner = "io/github/betterthanupdates/shockahpi/item/ShockAhPIAxeItem";
									break;
								case "net/minecraft/class_116":
									methodOwner = "io/github/betterthanupdates/shockahpi/item/ShockAhPIPickaxeItem";
									break;
								case "net/minecraft/class_501":
									methodOwner = "io/github/betterthanupdates/shockahpi/item/ShockAhPIShovelItem";
									break;
							}
						}

						switch (methodOwner) {
							case "net/minecraft/class_13":
								if (methodName.equals("setRedstoneColors")) {
									methodOwner = "io/github/betterthanupdates/forge/ForgeClientReflection";
									methodName = "BlockRenderer$setRedstoneColors";
								}

								break;
							case "net/minecraft/class_50":
								if (methodName.equals("getByID")) {
									methodOwner = "shockahpi/DimensionBase";
								}

								break;
						}

						super.visitMethodInsn(opcode, methodOwner, methodName, methodDescriptor, isInterface);
					}

					@Override
					public void visitLdcInsn(Object value) {
						super.visitLdcInsn(value);
					}
				};
			}
		};
	}
}
