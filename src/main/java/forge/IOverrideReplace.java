package forge;

import net.minecraft.world.World;

public interface IOverrideReplace {
    boolean canReplaceBlock(final World level, final int i, final int j, final int k, final int l);
    
    boolean getReplacedSuccess();
}
