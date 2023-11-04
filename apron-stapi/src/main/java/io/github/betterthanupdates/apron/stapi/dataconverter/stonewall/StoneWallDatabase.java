package io.github.betterthanupdates.apron.stapi.dataconverter.stonewall;

import java.util.Properties;

import com.mojang.serialization.Dynamic;
import net.modificationstation.stationapi.api.nbt.NbtOps;
import net.modificationstation.stationapi.api.registry.ModID;

import net.minecraft.util.io.CompoundTag;

import io.github.betterthanupdates.apron.stapi.dataconverter.ModDatabase;

public class StoneWallDatabase extends ModDatabase {
	public StoneWallDatabase() {
		super(ModID.of("mod_StoneWall"), ModID.of("stonewall"));
	}

	@Override
	public void register() {
		Properties config = this.loadConfigFile();

		blockAndItem(config.getProperty("blockStoneWallID", "245"), "stone_wall");
		blockAndItem(config.getProperty("blockMossyCobbleWallID", "246"), "moss_stone_wall");
		blockAndItem(config.getProperty("blockCobblestoneWallID", "244"), "cobblestone_wall");
		blockAndItem(config.getProperty("blockbrickWallID", "248"), "brick_wall");
		blockAndItem(config.getProperty("blockSandstoneWallID", "247"), "sandstone_wall");
	}

	@Override
	public Dynamic<?> blockState(String id, Dynamic<?> dynamic) {
		if (this.hasBlockOld(id)) {
			CompoundTag tag = new CompoundTag();
			tag.put("east", "none");
			tag.put("north", "none");
			tag.put("west", "none");
			tag.put("south", "none");
			tag.put("up", false);
			dynamic = dynamic.set("Properties", new Dynamic<>(NbtOps.INSTANCE, tag));
		}

		return super.blockState(id, dynamic);
	}
}
