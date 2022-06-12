package shockahpi;

import net.minecraft.util.io.CompoundTag;

/**
 * Represents any class that can store or load NBT data
 */
public interface INBT {
	String getFnameNBT();
	void saveNBT(CompoundTag nbt);
	void loadNBT(CompoundTag nbt);
}
