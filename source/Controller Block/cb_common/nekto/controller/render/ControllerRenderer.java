package nekto.controller.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import nekto.controller.block.BlockBase;
import nekto.controller.core.Controller;
import nekto.controller.ref.GeneralRef;
import nekto.controller.render.model.ModelAnimator2;
import nekto.controller.tile.TileEntityBase;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class ControllerRenderer implements ISimpleBlockRenderingHandler{

	@Override
	public void renderInventoryBlock(Block block, int i, int j, RenderBlocks renderblocks)
	{
		if(block instanceof BlockBase)
		{
			block.setBlockBoundsForItemRender();
			TileEntityRenderer.instance.renderTileEntityAt((TileEntityBase)((BlockBase) block).createNewTileEntity(Minecraft.getMinecraft().theWorld), 0.0D, 0.0D, 0.0D, 0.0F);
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		}
	}
	
	@Override
	public boolean renderWorldBlock(IBlockAccess iblockaccess, int i, int j, int k, Block block, int l, RenderBlocks renderblocks)
	{
		return false;
	}

	@Override
	public boolean shouldRender3DInInventory() 
	{
		return true;
	}

	@Override//Unused anyway
	public int getRenderId()
	{
		return BlockBase.renderID;
	}
}
