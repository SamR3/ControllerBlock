package nekto.controller.core;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.src.ModLoader;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)
public class GuiController extends GuiContainer {

    private TileEntityController controllerTile;
    private EntityPlayer player;

    public GuiController(InventoryPlayer par1InventoryPlayer, TileEntityController par2TileEntity)
    {
        super(new ContainerController(par1InventoryPlayer, par2TileEntity));
        this.controllerTile = par2TileEntity;
        this.player = par1InventoryPlayer.player;
    }

    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        String s = "Controller Block";
        this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);
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
            buttonList.add(new GuiButton(1, guiLeft + 110, guiTop + 30, 50, 20, "Activate"));
    }
    @Override
    protected void actionPerformed(GuiButton guibutton) 
    {        
        Controller.proxy.sendPacket(guibutton.id, player);
    }

}
