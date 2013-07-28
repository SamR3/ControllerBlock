package nekto.controller.render;

import nekto.controller.core.Controller;
import nekto.controller.ref.GeneralRef;
import nekto.controller.render.model.ModelAnimator2;
import nekto.controller.tile.TileEntityBase;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;
//import net.minecraft.client.resources.ResourceLocation;

public class TileEntityAnimatorRenderer extends TileEntitySpecialRenderer {

    private final ModelAnimator2 model;
    //private static final ResourceLocation texture = new ResourceLocation("");
    public TileEntityAnimatorRenderer() 
    {
        this.model = new ModelAnimator2();
    }
    
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float scale) 
    {
        GL11.glPushMatrix();
        
        setLighting(tileEntity.worldObj, tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord, Controller.animator);
        
        GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
        bindTextureByName(GeneralRef.FULL_TEXTURE_PATH+((TileEntityBase)tileEntity).getTexture());
        //func_110628_a(texture);
        GL11.glPushMatrix();
        
        GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
        this.model.render(0.0625F);
        
        renderOrb((TileEntityBase)tileEntity);
        
        GL11.glPopMatrix();
        
        GL11.glPopMatrix();
    }
    
    private void renderOrb(TileEntityBase tile)
    {
        GL11.glRotatef(tile.getRotation(), 0.0F, 1.0F, 0.0F);
        
        float f2 = MathHelper.sin(tile.getHoverHeight() / 10.0F) * 0.04F;

        GL11.glTranslatef(0.0F, f2, 0.0F);
        
        this.model.renderOrb(0.0625F);
    }
    
    public void setLighting(World world, int i, int j, int k, Block block) 
    {
        Tessellator tessellator = Tessellator.instance;
        float f = block.getBlockBrightness(world, i, j, k);
        int l = world.getLightBrightnessForSkyBlocks(i, j, k, 0);
        int l1 = l % 65536;
        int l2 = l / 65536;
        tessellator.setColorOpaque_F(f, f, f);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)l1, (float)l2); 
    }    
}
