package io.github.betterthanupdates.apron.fixes.vanilla.infsprites;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.mine_diver.infsprites.proxy.RenderBlocksProxy;
import net.minecraft.class_66;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.client.render.entity.BlockEntityRenderDispatcher;
import net.minecraft.entity.BlockEntity;
import net.minecraft.world.WorldPopulationRegion;
import org.lwjgl.opengl.GL11;

import net.mine_diver.infsprites.api.IRenderHook;
import net.mine_diver.infsprites.proxy.transform.Shadow;
import net.mine_diver.infsprites.render.TextureManager;
import net.mine_diver.infsprites.util.compatibility.ForgePatcher;
import net.minecraft.block.Block;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.World;

public abstract class WorldRendererProxy extends class_66 {
	@Shadow(
			obfuscatedName = "field_225"
	)
	private int field_225;
	@Shadow(
			obfuscatedName = "field_226"
	)
	private static Tessellator field_226;
	@Shadow(
			obfuscatedName = "field_227"
	)
	private boolean field_227;
	@Shadow(
			obfuscatedName = "field_228"
	)
	private List<BlockEntity> field_228;

	@Shadow(
			obfuscatedName = "method_302"
	)
	public abstract void method_302();

	public WorldRendererProxy(World world, List<BlockEntity> list, int i, int j, int k, int l, int i1) {
		super(world, list, i, j, k, l, i1);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void method_296() {
		if(!field_249)
			return;
		chunkUpdates++;
		int i = field_231;
		int j = field_232;
		int k = field_233;
		int l = field_231 + field_234;
		int i1 = field_232 + field_235;
		int j1 = field_233 + field_236;
		for(int k1 = 0; k1 < 2; k1++)
			field_244[k1] = true;
		Chunk.field_953 = false;
		Set<BlockEntity> hashset = new HashSet<BlockEntity>();
		hashset.addAll(field_224);
		field_224.clear();
		int l1 = 1;
		WorldPopulationRegion chunkcache = new WorldPopulationRegion(world, i - l1, j - l1, k - l1, l + l1, i1 + l1, j1 + l1);
		BlockRenderer renderblocks = new RenderBlocksProxy(chunkcache);
		int i2 = 0;
		do {
			if(i2 >= 2)
				break;
			boolean flag = false;
			boolean flag1 = false;
			boolean flag2 = false;
//            Map<Integer, List<BlockCoord>> hashmap = new HashMap<Integer, List<BlockCoord>>();
//            List<Integer> arraylist = new ArrayList<Integer>();
			for(int j2 = j; j2 < i1; j2++) {
				for(int k2 = k; k2 < j1; k2++) {
					for(int l2 = i; l2 < l; l2++) {
						int i3 = chunkcache.getBlockId(l2, j2, k2);
						if(i3 <= 0)
							continue;
						if(!flag2) {
							flag2 = true;
							GL11.glNewList(field_225 + i2, 4864);
							GL11.glBindTexture(3553, TextureManager.getTerrain(0));
							GL11.glPushMatrix();
							method_302();
							float f = 1.000001F;
							GL11.glTranslatef((float)(-field_236) / 2.0F, (float)(-field_235) / 2.0F, (float)(-field_236) / 2.0F);
							GL11.glScalef(f, f, f);
							GL11.glTranslatef((float)field_236 / 2.0F, (float)field_235 / 2.0F, (float)field_236 / 2.0F);
							for (int r = 0, size = IRenderHook.renderHooks.size(); r < size; r++)
								IRenderHook.renderHooks.get(r).beforeRenderPass(i2);
							(ForgePatcher.installed ? Tessellator.INSTANCE : field_226).start();
							(ForgePatcher.installed ? Tessellator.INSTANCE : field_226).setOffset(-field_231, -field_232, -field_233);
						}
						if(i2 == 0 && Block.HAS_BLOCK_ENTITY[i3]) {
							BlockEntity tileentity = chunkcache.getBlockEntity(l2, j2, k2);
							if(BlockEntityRenderDispatcher.INSTANCE.hasCustomRenderer(tileentity))
								field_224.add(tileentity);
						}
						Block block = Block.BY_ID[i3];
						int j3 = block.getRenderPass();

						if(j3 > i2 && ForgePatcher.installed)
							flag = true;
						if(j3 != i2 && !ForgePatcher.installed) {
							flag = true;
							continue;
						}

						boolean pass = true;
						for (int r = 0, size = IRenderHook.renderHooks.size(); r < size; r++)
							if (!IRenderHook.renderHooks.get(r).canRenderInPass(block, i2))
								pass = false;

						if(pass) {
							for (int r = 0, size = IRenderHook.renderHooks.size(); r < size; r++)
								IRenderHook.renderHooks.get(r).beforeBlockRender(block, renderblocks);
							flag1 |= ((RenderBlocksProxy)renderblocks).render(block, l2, j2, k2);
							for (int r = 0, size = IRenderHook.renderHooks.size(); r < size; r++)
								IRenderHook.renderHooks.get(r).afterBlockRender(block, renderblocks);
						}
					}
				}
			}

			if(flag2) {
				for (int r = 0, size = IRenderHook.renderHooks.size(); r < size; r++)
					IRenderHook.renderHooks.get(r).afterRenderPass(i2);
				(ForgePatcher.installed ? Tessellator.INSTANCE : field_226).tessellate();
				GL11.glPopMatrix();
				GL11.glEndList();
				(ForgePatcher.installed ? Tessellator.INSTANCE : field_226).setOffset(0.0D, 0.0D, 0.0D);
			} else
				flag1 = false;
			if(flag1)
				field_244[i2] = false;
			if(!flag)
				break;
			i2++;
		} while (true);
		Set<BlockEntity> hashset1 = new HashSet<BlockEntity>();
		hashset1.addAll(field_224);
		hashset1.removeAll(hashset);
		field_228.addAll(hashset1);
		hashset.removeAll(field_224);
		field_228.removeAll(hashset);
		field_223 = Chunk.field_953;
		field_227 = true;
	}
}
