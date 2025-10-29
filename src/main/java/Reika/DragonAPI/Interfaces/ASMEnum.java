package Reika.DragonAPI.Interfaces;

import org.objectweb.asm.tree.ClassNode;

public interface ASMEnum {
    public String name();

    public void apply(ClassNode data);
}
