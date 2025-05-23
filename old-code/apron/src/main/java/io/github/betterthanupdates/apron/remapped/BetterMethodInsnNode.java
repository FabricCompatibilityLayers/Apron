package io.github.betterthanupdates.apron.remapped;

import io.github.fabriccompatibiltylayers.modremappingapi.api.MappingUtils;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;

import java.util.Map;

public class BetterMethodInsnNode extends MethodInsnNode implements Opcodes {
    public BetterMethodInsnNode(int opcode, String owner, String name, String descriptor) {
        this(opcode, owner, name, descriptor, opcode == Opcodes.INVOKEINTERFACE);
    }

    public BetterMethodInsnNode(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        this(opcode, MappingUtils.mapClass(owner), MappingUtils.mapMethod(owner, name, descriptor), isInterface);
    }

    private BetterMethodInsnNode(int opcode, String owner, MappingUtils.ClassMember member, boolean isInterface) {
        super(opcode, owner, member.name, MappingUtils.mapDescriptor(member.desc), isInterface);
    }

    @Override
    public AbstractInsnNode clone(final Map<LabelNode, LabelNode> clonedLabels) {
        return new BetterMethodInsnNode(opcode, owner, name, desc, itf).cloneAnnotations(this);
    }
}
