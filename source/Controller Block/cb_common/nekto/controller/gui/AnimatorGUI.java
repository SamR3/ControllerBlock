package nekto.controller.gui;

import nekto.controller.container.ContainerAnimator;
import nekto.controller.container.ContainerBase;
import nekto.controller.core.Controller;
import nekto.controller.tile.TileEntityAnimator;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class AnimatorGUI extends GuiContainer {

    private TileEntityAnimator animatorTile;
    private EntityPlayer player;

    public AnimatorGUI(InventoryPlayer par1InventoryPlayer, TileEntityAnimator par2TileEntity)
    {
        super(new ContainerAnimator(par1InventoryPlayer, par2TileEntity));
        this.animatorTile = par2TileEntity;
        this.player = par1InventoryPlayer.player;
    }

    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {        
        String s = "Animator Block";
        this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);
        
        String value = (Float.toString(round(((float)animatorTile.getDelay()) / 1000, 2)) + "s");
        this.fontRenderer.drawString(value, this.xSize / 2 - this.fontRenderer.getStringWidth(value) / 2, 109, 0);
    }

    @Override
    public void updateScreen() 
    {
        
    }
    
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture("/mods/controller/textures/gui/controllergui.png");
        this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, this.xSize, this.ySize);
    }
    
    @Override
    public void initGui() 
    {
            super.initGui();
            
            //id, x, y, width, height, text
            //buttonList.add(new GuiButton(1, guiLeft + 110, guiTop + 30, 50, 20, "Activate"));
            buttonList.add(new GuiButton(1, guiLeft + 109, guiTop + 105, 16, 15, "+"));
            buttonList.add(new GuiButton(2, guiLeft + 51, guiTop + 105, 16, 15, "-"));
            
    }
    
    @Override
    protected void actionPerformed(GuiButton guibutton) 
    {        
        
        switch(guibutton.id) 
        {
            case 1:
                ((TileEntityAnimator)((ContainerBase)player.openContainer).getControl()).setDelay(0.1F);
                break;
            case 2:
                ((TileEntityAnimator)((ContainerBase)player.openContainer).getControl()).setDelay(-0.1F);
                break;
            default:
                Controller.proxy.sendPacket(guibutton.id, player);
                break;

        }
    }
    
    public float round(float value, int places) 
    {
        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        
        float val = (float) tmp / factor;
        
        return val;
    }

}
