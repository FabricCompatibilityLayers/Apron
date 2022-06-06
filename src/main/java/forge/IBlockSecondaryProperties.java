package forge;

import net.minecraft.world.World;

public interface IBlockSecondaryProperties {
    boolean isBlockNormalCube(final World level, final int i, final int j, final int k);
    
    boolean isBlockReplaceable(final World level, final int i, final int j, final int k);
    
    boolean isBlockBurning(final World level, final int i, final int j, final int k);
    
    boolean isAirBlock(final World level, final int i, final int j, final int k);
}
