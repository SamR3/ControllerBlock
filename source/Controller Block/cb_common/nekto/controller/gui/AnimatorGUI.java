package nekto.controller.gui;

import java.util.List;

import nekto.controller.container.ContainerAnimator;
import nekto.controller.container.ContainerBase;
import nekto.controller.core.Controller;
import nekto.controller.item.ItemBase;
import nekto.controller.tile.TileEntityAnimator;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class AnimatorGUI extends GuiContainer {

    private EntityPlayer player;
    private List<GuiButton> frameButtons;

    public AnimatorGUI(InventoryPlayer par1InventoryPlayer, TileEntityAnimator par2TileEntity)
    {
        super(new ContainerAnimator(par1InventoryPlayer, par2TileEntity));
        this.player = par1InventoryPlayer.player;
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {        
        String s = "Animator Block";
        this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);

        String value = (Float.toString(round((float)getDelay() / 1000, 2)) + "s");
        this.fontRenderer.drawString(value, this.xSize / 2 - this.fontRenderer.getStringWidth(value) / 2, 109, 0);
    }
    
    private int getDelay() {
		return ((TileEntityAnimator)((ContainerBase)this.inventorySlots).getControl()).getDelay();
	}

	@Override
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

            buttonList.add(new GuiButton(3, guiLeft + 14, guiTop + 50, 70, 20, "Sequence"));
            buttonList.add(new GuiButton(4, guiLeft + 92, guiTop + 50, 70, 20, "Reverse"));
            buttonList.add(new GuiButton(5, guiLeft + 14, guiTop + 75, 70, 20, "Loop"));
            buttonList.add(new GuiButton(6, guiLeft + 92, guiTop + 75, 70, 20, "Random"));
            
            buttonList.add(new GuiButton(7, guiLeft + 39, guiTop + 20, 50, 20, "Reset"));                  
    }
    
    @Override
    protected void actionPerformed(GuiButton guibutton) 
    {        
    	switch(guibutton.id) 
    	{
            case 7:
            	ItemStack stack = this.inventorySlots.getSlot(0).getStack();
            	if(stack!=null && stack.hasTagCompound() && stack.getTagCompound().hasKey(ItemBase.KEYTAG))
            	{
            		int[] data = stack.getTagCompound().getIntArray(ItemBase.KEYTAG);
            		int[] cData = new int[1+data.length];
            		cData[0] = guibutton.id;
            		for(int i=0;i<data.length;i++)
            			cData[i+1]=data[i];
            		
            		Controller.proxy.sendPacket(player, cData);
            	}
            	return;
            default:
                Controller.proxy.sendPacket( player, guibutton.id);
                return;
        }
    }
    
    public static float round(float value, int places) 
    {
        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        
        float val = (float) tmp / factor;
        
        return val;
    }

}
