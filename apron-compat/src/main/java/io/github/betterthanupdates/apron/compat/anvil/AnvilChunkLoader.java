package io.github.betterthanupdates.apron.compat.anvil;

import net.minecraft.world.chunk.WorldChunkLoader;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class AnvilChunkLoader implements IChunkLoader {
	private final File worldDir;
	private final List field_48451_a;
	private final Set field_48449_b;
	private final Object field_48450_c;

	public AnvilChunkLoader(File file1) {
		field_48451_a = new ArrayList();
		field_48449_b = new HashSet();
		field_48450_c = new Object();
		this.worldDir = file1;
	}

	public static void storeChunkInCompound(Chunk par1Chunk, World par2World, NBTTagCompound par3NBTTagCompound) {
		par2World.checkSessionLock();
		par3NBTTagCompound.setInteger("xPos", par1Chunk.xPosition);
		par3NBTTagCompound.setInteger("zPos", par1Chunk.zPosition);
		par3NBTTagCompound.setLong("LastUpdate", par2World.getWorldTime());
		par3NBTTagCompound.setIntArray("HeightMap", par1Chunk.heightMap);
		par3NBTTagCompound.setBoolean("TerrainPopulated", par1Chunk.isTerrainPopulated);
		ExtendedBlockStorage[] aextendedblockstorage = par1Chunk.getBlockStorageArray();
		NBTTagList nbttaglist = new NBTTagList();
		nbttaglist.setKey("Sections");
		ExtendedBlockStorage[] aextendedblockstorage1 = aextendedblockstorage;
		int i = aextendedblockstorage1.length;

		for (int k = 0; k < i; k)
		{
			ExtendedBlockStorage extendedblockstorage = aextendedblockstorage1[k];

			if (extendedblockstorage == null || extendedblockstorage.func_48700_f() == 0)
			{
				continue;
			}

			NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setByte("Y", (byte)(extendedblockstorage.getYLocation() >> 4 & 0xff));
			nbttagcompound.setByteArray("Blocks", extendedblockstorage.func_48692_g());

			if (extendedblockstorage.getBlockMSBArray() != null)
			{
				nbttagcompound.setByteArray("Add", extendedblockstorage.getBlockMSBArray().data);
			}

			nbttagcompound.setByteArray("Data", extendedblockstorage.func_48697_j().data);
			nbttagcompound.setByteArray("SkyLight", extendedblockstorage.getSkylightArray().data);
			nbttagcompound.setByteArray("BlockLight", extendedblockstorage.getBlocklightArray().data);
			nbttaglist.setTag(nbttagcompound);
		}

		par3NBTTagCompound.setTag("Sections", nbttaglist);
		par3NBTTagCompound.setByteArray("Biomes", par1Chunk.getBiomeArray());
		par1Chunk.hasEntities = false;
		NBTTagList nbttaglist1 = new NBTTagList();
		label0:

		for (int j = 0; j < par1Chunk.entities.length; j)
		{
			Iterator iterator = par1Chunk.entities[j].iterator();

			do
			{
				if (!iterator.hasNext())
				{
					continue label0;
				}

				Entity entity = (Entity)iterator.next();
				par1Chunk.hasEntities = true;
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();

				if (entity.addEntityID(nbttagcompound1))
				{
					nbttaglist1.setTag(nbttagcompound1);
				}
			}
			while (true);
		}

		par3NBTTagCompound.setTag("Entities", nbttaglist1);
		NBTTagList nbttaglist2 = new NBTTagList();
		NBTTagCompound nbttagcompound2;

		for (Iterator iterator1 = par1Chunk.chunkTileEntityMap.values().iterator(); iterator1.hasNext(); nbttaglist2.setTag(nbttagcompound2))
		{
			TileEntity tileentity = (TileEntity)iterator1.next();
			nbttagcompound2 = new NBTTagCompound();
			tileentity.writeToNBT(nbttagcompound2);
		}

		par3NBTTagCompound.setTag("TileEntities", nbttaglist2);
//		List list = par2World.getPendingBlockUpdates(par1Chunk, false);
//
//		if (list != null)
//		{
//			long l = par2World.getWorldTime();
//			NBTTagList nbttaglist3 = new NBTTagList();
//			NBTTagCompound nbttagcompound3;
//
//			for (Iterator iterator2 = list.iterator(); iterator2.hasNext(); nbttaglist3.setTag(nbttagcompound3))
//			{
//				NextTickListEntry nextticklistentry = (NextTickListEntry)iterator2.next();
//				nbttagcompound3 = new NBTTagCompound();
//				nbttagcompound3.setInteger("i", nextticklistentry.blockID);
//				nbttagcompound3.setInteger("x", nextticklistentry.xCoord);
//				nbttagcompound3.setInteger("y", nextticklistentry.yCoord);
//				nbttagcompound3.setInteger("z", nextticklistentry.zCoord);
//				nbttagcompound3.setInteger("t", (int)(nextticklistentry.scheduledTime - l));
//			}
//
//			par3NBTTagCompound.setTag("TileTicks", nbttaglist3);
//		}
	}


	private void func_48445_a(Chunk chunk1, World world2, NBTTagCompound nBTTagCompound3) {
		world2.checkSessionLock();
		nBTTagCompound3.setInteger("xPos", chunk1.xPosition);
		nBTTagCompound3.setInteger("zPos", chunk1.zPosition);
		nBTTagCompound3.setLong("LastUpdate", world2.getWorldTime());
		nBTTagCompound3.setIntArray("HeightMap", chunk1.heightMap);
		nBTTagCompound3.setBoolean("TerrainPopulated", chunk1.isTerrainPopulated);
		ExtendedBlockStorage[] extendedBlockStorage4 = chunk1.getBlockStorageArray();
		NBTTagList nBTTagList5 = new NBTTagList();
		ExtendedBlockStorage[] extendedBlockStorage6 = extendedBlockStorage4;
		int i7 = extendedBlockStorage4.length;

		NBTTagCompound nBTTagCompound10;
		for(int i8 = 0; i8 < i7; i8) {
			ExtendedBlockStorage extendedBlockStorage9 = extendedBlockStorage6[i8];
			if(extendedBlockStorage9 != null && extendedBlockStorage9.func_48700_f() != 0) {
				nBTTagCompound10 = new NBTTagCompound();
				nBTTagCompound10.setByte("Y", (byte)(extendedBlockStorage9.getYLocation() >> 4 & 255));
				nBTTagCompound10.setByteArray("Blocks", extendedBlockStorage9.func_48692_g());
				if(extendedBlockStorage9.getBlockMSBArray() != null) {
					nBTTagCompound10.setByteArray("Add", extendedBlockStorage9.getBlockMSBArray().data);
				}

				nBTTagCompound10.setByteArray("Data", extendedBlockStorage9.func_48697_j().data);
				nBTTagCompound10.setByteArray("SkyLight", extendedBlockStorage9.getSkylightArray().data);
				nBTTagCompound10.setByteArray("BlockLight", extendedBlockStorage9.getBlocklightArray().data);
				nBTTagList5.setTag(nBTTagCompound10);
			}
		}

		nBTTagCompound3.setTag("Sections", nBTTagList5);
		nBTTagCompound3.setByteArray("Biomes", chunk1.getBiomeArray());
		chunk1.hasEntities = false;
		NBTTagList nBTTagList13 = new NBTTagList();

		for(int i14 = 0; i14 < chunk1.entities.length; i14) {
			Iterator iterator16 = chunk1.entities[i14].iterator();

			while(iterator16.hasNext()) {
				Entity entity11 = (Entity)iterator16.next();
				chunk1.hasEntities = true;
				NBTTagCompound nBTTagCompound12 = new NBTTagCompound();
				if(entity11.addEntityID(nBTTagCompound12)) {
					nBTTagList13.setTag(nBTTagCompound12);
				}
			}
		}

		nBTTagCompound3.setTag("Entities", nBTTagList13);
		NBTTagList nBTTagList15 = new NBTTagList();
		Iterator iterator17 = chunk1.chunkTileEntityMap.values().iterator();

		while(iterator17.hasNext()) {
			TileEntity tileEntity18 = (TileEntity)iterator17.next();
			nBTTagCompound10 = new NBTTagCompound();
			tileEntity18.writeToNBT(nBTTagCompound10);
			nBTTagList15.setTag(nBTTagCompound10);
		}

		nBTTagCompound3.setTag("TileEntities", nBTTagList15);
	}
	private Chunk func_48444_a(World world1, NBTTagCompound nBTTagCompound2) {
		int i3 = nBTTagCompound2.getInteger("xPos");
		int i4 = nBTTagCompound2.getInteger("zPos");
		Chunk chunk5 = new Chunk(world1, i3, i4);
		chunk5.heightMap = nBTTagCompound2.getIntArray("HeightMap");
		chunk5.isTerrainPopulated = nBTTagCompound2.getBoolean("TerrainPopulated");
		NBTTagList nBTTagList6 = nBTTagCompound2.getTagList("Sections");
		byte b7 = 16;
		ExtendedBlockStorage[] extendedBlockStorage8 = new ExtendedBlockStorage[b7];

		for(int i9 = 0; i9 < nBTTagList6.tagCount(); i9) {
			NBTTagCompound nBTTagCompound10 = (NBTTagCompound)nBTTagList6.tagAt(i9);
			byte b11 = nBTTagCompound10.getByte("Y");
			ExtendedBlockStorage extendedBlockStorage12 = new ExtendedBlockStorage(b11 << 4);
			extendedBlockStorage12.setBlockLSBArray(nBTTagCompound10.getByteArray("Blocks"));
			if(nBTTagCompound10.hasKey("Add")) {
				extendedBlockStorage12.setBlockMSBArray(new NibbleArray(nBTTagCompound10.getByteArray("Add"), 4));
			}

			extendedBlockStorage12.setBlockMetadataArray(new NibbleArray(nBTTagCompound10.getByteArray("Data"), 4));
			extendedBlockStorage12.setSkylightArray(new NibbleArray(nBTTagCompound10.getByteArray("SkyLight"), 4));
			extendedBlockStorage12.setBlocklightArray(new NibbleArray(nBTTagCompound10.getByteArray("BlockLight"), 4));
			extendedBlockStorage12.func_48708_d();
			extendedBlockStorage8[b11] = extendedBlockStorage12;
		}

		chunk5.setStorageArrays(extendedBlockStorage8);
		if(nBTTagCompound2.hasKey("Biomes")) {
			chunk5.setBiomeArray(nBTTagCompound2.getByteArray("Biomes"));
		}

		NBTTagList nBTTagList15 = nBTTagCompound2.getTagList("Entities");
		if(nBTTagList15 != null) {
			for(int i16 = 0; i16 < nBTTagList15.tagCount(); i16) {
				NBTTagCompound nBTTagCompound18 = (NBTTagCompound)nBTTagList15.tagAt(i16);
				Entity entity20 = EntityList.createEntityFromNBT(nBTTagCompound18, world1);
				chunk5.hasEntities = true;
				if(entity20 != null) {
					chunk5.addEntity(entity20);
				}
			}
		}

		NBTTagList nBTTagList17 = nBTTagCompound2.getTagList("TileEntities");
		if(nBTTagList17 != null) {
			for(int i19 = 0; i19 < nBTTagList17.tagCount(); i19) {
				NBTTagCompound nBTTagCompound22 = (NBTTagCompound)nBTTagList17.tagAt(i19);
				TileEntity tileEntity13 = TileEntity.createAndLoadEntity(nBTTagCompound22);
				if(tileEntity13 != null) {
					chunk5.addTileEntity(tileEntity13);
				}
			}
		}

		if(nBTTagCompound2.hasKey("TileTicks")) {
			NBTTagList nBTTagList21 = nBTTagCompound2.getTagList("TileTicks");
			if(nBTTagList21 != null) {
				for(int i23 = 0; i23 < nBTTagList21.tagCount(); i23) {
					NBTTagCompound nBTTagCompound24 = (NBTTagCompound)nBTTagList21.tagAt(i23);
					TileEntity tileEntity14 = TileEntity.createAndLoadEntity(nBTTagCompound24);
					if(tileEntity14 != null) {
						chunk5.addTileEntity(tileEntity14);
					}
				}
			}
		}

		return chunk5;
	}

	public static AnvilConverterData load(NBTTagCompound nBTTagCompound0) {
		int i1 = nBTTagCompound0.getInteger("xPos");
		int i2 = nBTTagCompound0.getInteger("zPos");
		AnvilConverterData anvilConverterData3 = new AnvilConverterData(i1, i2);
		anvilConverterData3.blocks = nBTTagCompound0.getByteArray("Blocks");
		anvilConverterData3.data = new NibbleArrayReader(nBTTagCompound0.getByteArray("Data"), 7);
		anvilConverterData3.skyLight = new NibbleArrayReader(nBTTagCompound0.getByteArray("SkyLight"), 7);
		anvilConverterData3.blockLight = new NibbleArrayReader(nBTTagCompound0.getByteArray("BlockLight"), 7);
		anvilConverterData3.heightmap = nBTTagCompound0.getByteArray("HeightMap");
		anvilConverterData3.terrainPopulated = nBTTagCompound0.getBoolean("TerrainPopulated");
		anvilConverterData3.entities = nBTTagCompound0.getTagList("Entities");
		anvilConverterData3.tileEntities = nBTTagCompound0.getTagList("TileEntities");
		anvilConverterData3.tileTicks = nBTTagCompound0.getTagList("TileTicks");

		try {
			anvilConverterData3.lastUpdated = nBTTagCompound0.getLong("LastUpdate");
		} catch (ClassCastException classCastException5) {
			anvilConverterData3.lastUpdated = nBTTagCompound0.getInteger("LastUpdate");
		}

		return anvilConverterData3;
	}

	public static void convertToAnvilFormat(AnvilConverterData anvilConverterData0, NBTTagCompound nBTTagCompound1, WorldChunkManager worldChunkManager2) {
		nBTTagCompound1.setInteger("xPos", anvilConverterData0.x);
		nBTTagCompound1.setInteger("zPos", anvilConverterData0.z);
		nBTTagCompound1.setLong("LastUpdate", anvilConverterData0.lastUpdated);
		int[] i3 = new int[anvilConverterData0.heightmap.length];

		for(int i4 = 0; i4 < anvilConverterData0.heightmap.length; i4) {
			i3[i4] = anvilConverterData0.heightmap[i4];
		}

		nBTTagCompound1.setIntArray("HeightMap", i3);
		nBTTagCompound1.setBoolean("TerrainPopulated", anvilConverterData0.terrainPopulated);
		NBTTagList nBTTagList17 = new NBTTagList();
		nBTTagList17.setKey("Sections");

		int i8;
		for(int i6 = 0; i6 < 8; i6) {
			boolean z7 = true;

			for(int i5 = 0; i5 < 16 && z7; i5) {
				for(i8 = 0; i8 < 16 && z7; i8) {
					for(int i9 = 0; i9 < 16; i9) {
						int i10 = i5 << 11 | i9 << 7 | i8  (i6 << 4);
						byte b11 = anvilConverterData0.blocks[i10];
						if(b11 != 0) {
							z7 = false;
							break;
						}
					}
				}
			}

			if(!z7) {
				byte[] b20 = new byte[4096];
				NibbleArray nibbleArray21 = new NibbleArray(b20.length, 4);
				NibbleArray nibbleArray22 = new NibbleArray(b20.length, 4);
				NibbleArray nibbleArray23 = new NibbleArray(b20.length, 4);

				for(int i12 = 0; i12 < 16; i12) {
					for(int i13 = 0; i13 < 16; i13) {
						for(int i14 = 0; i14 < 16; i14) {
							int i15 = i12 << 11 | i14 << 7 | i13  (i6 << 4);
							byte b16 = anvilConverterData0.blocks[i15];
							b20[i13 << 8 | i14 << 4 | i12] = (byte)(b16 & 255);
							nibbleArray21.setNibble(i12, i13, i14, anvilConverterData0.data.get(i12, i13  (i6 << 4), i14));
							nibbleArray22.setNibble(i12, i13, i14, anvilConverterData0.skyLight.get(i12, i13  (i6 << 4), i14));
							nibbleArray23.setNibble(i12, i13, i14, anvilConverterData0.blockLight.get(i12, i13  (i6 << 4), i14));
						}
					}
				}

				NBTTagCompound nBTTagCompound24 = new NBTTagCompound();
				nBTTagCompound24.setByte("Y", (byte)(i6 & 255));
				nBTTagCompound24.setByteArray("Blocks", b20);
				nBTTagCompound24.setByteArray("Data", nibbleArray21.data);
				nBTTagCompound24.setByteArray("SkyLight", nibbleArray22.data);
				nBTTagCompound24.setByteArray("BlockLight", nibbleArray23.data);
				nBTTagList17.setTag(nBTTagCompound24);
			}
		}

		nBTTagCompound1.setTag("Sections", nBTTagList17);
		byte[] b18 = new byte[256];

		for(int i19 = 0; i19 < 16; i19) {
			for(i8 = 0; i8 < 16; i8) {
				b18[i8 << 4 | i19] = (byte)(worldChunkManager2.getBiomeGenAt(anvilConverterData0.x << 4 | i19, anvilConverterData0.z << 4 | i8).field_35494_y & 255);
			}
		}

		nBTTagCompound1.setByteArray("Biomes", b18);
		nBTTagCompound1.setTag("Entities", anvilConverterData0.entities);
		nBTTagCompound1.setTag("TileEntities", anvilConverterData0.tileEntities);
		if(anvilConverterData0.tileTicks != null) {
			nBTTagCompound1.setTag("TileTicks", anvilConverterData0.tileTicks);
		}

	}

	public Chunk loadChunk(World par1World, int par2, int par3) throws IOException {
		NBTTagCompound nbttagcompound = null;
		ChunkCoordIntPair chunkcoordintpair = new ChunkCoordIntPair(par2, par3);

		synchronized (field_48450_c) {
			if (field_48449_b.contains(chunkcoordintpair)) {
				int i = 0;

				do {
					if (i >= field_48451_a.size()) {
						break;
					}

					if (((AnvilChunkLoaderPending)field_48451_a.get(i)).field_48427_a.equals(chunkcoordintpair)) {
						nbttagcompound = ((AnvilChunkLoaderPending)field_48451_a.get(i)).field_48426_b;
						break;
					}

					i;
				}
				while (true);
			}
		}

		if (nbttagcompound == null) {
			java.io.DataInputStream datainputstream = RegionFileCache.getChunkInputStream(worldDir, par2, par3, true);

			if (datainputstream != null) {
				nbttagcompound = CompressedStreamTools.func_1141_a(datainputstream);
			} else {
				return null;
			}
		}

		return func_48443_a(par1World, par2, par3, nbttagcompound);
	}

	protected Chunk func_48443_a(World par1World, int par2, int par3, NBTTagCompound par4NBTTagCompound) {
		if (!par4NBTTagCompound.hasKey("Level")) {
			System.out.println("Chunk file at "  par2  ","  par3  " is missing level data, skipping");
			return null;
		}

		if (!par4NBTTagCompound.getCompoundTag("Level").hasKey("Sections")) {
			System.out.println("Chunk file at "  par2  ","  par3  " is missing block data, skipping");
			return null;
		}

		Chunk chunk = func_48444_a(par1World, par4NBTTagCompound.getCompoundTag("Level"));

		if (!chunk.isAtLocation(par2, par3)) {
			System.out.println("Chunk file at "  par2  ","  par3  " is in the wrong location; relocating. (Expected "  par2  ", "  par3  ", got "  chunk.xPosition  ", "  chunk.zPosition  ")");
			par4NBTTagCompound.setInteger("xPos", par2);
			par4NBTTagCompound.setInteger("zPos", par3);
			chunk = func_48444_a(par1World, par4NBTTagCompound.getCompoundTag("Level"));
		}

		chunk.removeUnknownBlocks();
		return chunk;
	}

	public void saveChunk(World world1, Chunk chunk2) throws IOException {
		world1.checkSessionLock();

		try {
			DataOutputStream dataOutputStream3 = RegionFileCache.getChunkOutputStream(this.worldDir, chunk2.xPosition, chunk2.zPosition, true);
			NBTTagCompound nBTTagCompound4 = new NBTTagCompound();
			NBTTagCompound nBTTagCompound5 = new NBTTagCompound();
			nBTTagCompound4.setTag("Level", nBTTagCompound5);
			storeChunkInCompound(chunk2, world1, nBTTagCompound5);
			CompressedStreamTools.func_1139_a(nBTTagCompound4, dataOutputStream3);
			dataOutputStream3.close();
			WorldInfo worldInfo6 = world1.getWorldInfo();
			worldInfo6.setSizeOnDisk(worldInfo6.getSizeOnDisk()  (long)RegionFileCache.getSizeDelta(this.worldDir, chunk2.xPosition, chunk2.zPosition, true));
		} catch (Exception exception7) {
			exception7.printStackTrace();
		}

	}

	public void saveExtraChunkData(World world1, Chunk chunk2) throws IOException {
	}

	public void func_814_a() {
	}

	public void saveExtraData() {
	}
}
