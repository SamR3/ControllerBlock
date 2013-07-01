package nekto.controller.render;

import nekto.controller.core.Controller;
import nekto.controller.render.model.ModelAnimator;
import nekto.controller.tile.TileEntityAnimator;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

public class TileEntityAnimatorRenderer extends TileEntitySpecialRenderer {

  //The model of your block
    private final ModelAnimator model;
    
    public TileEntityAnimatorRenderer() {
            this.model = new ModelAnimator();
    }
    
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float scale) {
        GL11.glPushMatrix();
        
        TileEntityAnimator tileEntityYour = (TileEntityAnimator)tileEntity;
        setLighting(tileEntityYour, tileEntity.worldObj, tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord, Controller.animator);
        
        GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
        bindTextureByName("/mods/roads/textures/blocks/TrafficLightPoleRed.png");
        
        GL11.glPushMatrix();
        
        GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
        this.model.render((Entity)null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        
        GL11.glRotatef(((TileEntityAnimator) tileEntity).getRotation(), 0.0F, 1.0F, 0.0F);
        
        float f2 = MathHelper.sin(((TileEntityAnimator) tileEntity).getHoverHeight() / 10.0F) * 0.04F;

        GL11.glTranslatef(0.0F, f2, 0.0F);
        
        this.model.renderOrb(0.0625F);
        
        GL11.glPopMatrix();
        
        GL11.glPopMatrix();
    }
    
    public void setLighting(TileEntityAnimator tl, World world, int i, int j, int k, Block block) {
        Tessellator tessellator = Tessellator.instance;
        float f = block.getBlockBrightness(world, i, j, k);
        int l = world.getLightBrightnessForSkyBlocks(i, j, k, 0);
        int l1 = l % 65536;
        int l2 = l / 65536;
        tessellator.setColorOpaque_F(f, f, f);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)l1, (float)l2); 
    }    
}
