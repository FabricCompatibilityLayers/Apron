package io.github.betterthanupdates.apron;

import net.fabricmc.tinyremapper.TinyRemapper;
import net.fabricmc.tinyremapper.api.TrClass;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ApronPostRemappingVisitor implements TinyRemapper.ApplyVisitorProvider {
	@Override
	public ClassVisitor insertApplyVisitor(TrClass cls, ClassVisitor next) {
		final String className = cls.getName();
		return new ClassVisitor(Opcodes.ASM9, next) {
			@Override
			public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
				return new MethodVisitor(Opcodes.ASM9, super.visitMethod(access, name, descriptor, signature, exceptions)) {
					@Override
					public void visitFieldInsn(int opcode, String fieldOwner, String fieldName, String fieldDescriptor) {
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
							case "betatweaks/Utils":
							case "hmi/Utils":
								if (methodName.equals("getField")) {
									methodOwner = "io/github/betterthanupdates/apron/ReflectionUtils";
								}

								break;
						}

						super.visitMethodInsn(opcode, methodOwner, methodName, methodDescriptor, isInterface);
					}

					@Override
					public void visitLdcInsn(Object value) {
						if (value instanceof String) {
							String stringValue = (String) value;
							switch (className) {
								// Twilight Forest
								case "mod_TwilightForest":
									if (name.equals("<init>") && stringValue.equals("DimensionTwilightForest")) {
										value = "net.minecraft." + stringValue;
									}

									break;

								// How Many Items
								case "hmi/Utils":
									if (stringValue.equals("getSlotAtPosition")) {
										value = "method_986";
									} else if (stringValue.equals("drawGradientRect")) {
										value = "method_1933";
									}

									break;
								case "hmi/GuiOverlay":
									switch (stringValue) {
										case "mouseClicked":
											value = "method_124";
											break;
										case "keyTyped":
											value = "method_117";
											break;
										case "mouseMovedOrUp":
											value = "method_128";
											break;
									}

									break;

								// BetaTweaks
								case "mod_BetaTweaks":
									switch (stringValue) {
										case "getSlotAtPosition":
											value = "method_986";
											break;
										case "ModLoaderMp":
											value = "modloadermp." + stringValue;
											break;
										case "tallGrass":
											value = "field_1845";
											break;
										case "deadBush":
											value = "field_1846";
											break;
										case "blockSteel":
											value = "field_1883";
											break;
										case "blockGold":
											value = "field_1882";
											break;
										case "blockDiamond":
											value = "field_1898";
											break;
										case "blocksEffectiveAgainst":
											value = "field_2712";
											break;
									}

									break;
								case "betatweaks/Utils":
									if (stringValue.equals("net.minecraft.src.")) {
										value = "net.minecraft.";
									} else if (stringValue.equals("modList")) {
										value = "MOD_LIST";
									}

									break;
								case "betatweaks/EntityRendererProxyFOV":
									switch (stringValue) {
										case "func_4135_b":
											value = "method_1845";
											break;
										case "hurtCameraEffect":
											value = "method_1849";
											break;
										case "setupViewBobbing":
											value = "method_1850";
											break;
										case "orientCamera":
											value = "method_1851";
											break;
										case "updateFogColor":
											value = "method_1852";
											break;
									}

									break;
								case "betatweaks/block/BlockTNTPunchable":
									if (stringValue.equals("tnt")) {
										value = "field_995";
									}

									break;
							}
						}

						super.visitLdcInsn(value);
					}
				};
			}
		};
	}
}
