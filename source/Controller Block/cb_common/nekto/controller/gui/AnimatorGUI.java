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
        
        
        //Just for debugging
        /*String value = "Delay: " + Float.toString(animatorTile.theAnimator.getDelay());
        this.fontRenderer.drawString(value, this.xSize / 2 - this.fontRenderer.getStringWidth(value) / 2, 20, 4210752);*/
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
            buttonList.add(new GuiSliderFixed(2, (guiLeft + (176 / 2)) - 75, guiTop + 105, 150, 20, "Delay", 0.0F));
            buttonList.add(new GuiButton(1, guiLeft + 110, guiTop + 30, 50, 20, "Activate"));
            //buttonList.add(new GuiButton(2, guiLeft + 109, guiTop + 113, 16, 14, "+"));
    }
    
    @Override
    protected void actionPerformed(GuiButton guibutton) 
    {        
        if(guibutton.id == 2)
        {
            float sliderValue = ((GuiSliderFixed) buttonList.get(0)).sliderValue;
            ((TileEntityAnimator)((ContainerBase)player.openContainer).getControl()).theAnimator.setTimer(round(sliderValue, 2));
        } else {
            Controller.proxy.sendPacket(guibutton.id, player);
        }
    }
    
    public float round(float value, int places) 
    {
        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (float) tmp / factor;
    }

}
