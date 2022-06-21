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
						}

						super.visitMethodInsn(opcode, methodOwner, methodName, methodDescriptor, isInterface);
					}
				};
			}
		};
	}
}
