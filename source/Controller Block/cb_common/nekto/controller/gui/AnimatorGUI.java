package nekto.controller.gui;

import java.util.List;

import nekto.controller.container.ContainerAnimator;
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

        String value = (Float.toString(round((float)((ContainerAnimator)this.inventorySlots).getDelay() / 10, 2)) + "s");
        this.fontRenderer.drawString(value, this.xSize / 2 - this.fontRenderer.getStringWidth(value) / 2, 109, 0);
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
        buttonList.add(new GuiButton(0, guiLeft + 109, guiTop + 105, 16, 15, "+"));
        buttonList.add(new GuiButton(1, guiLeft + 51, guiTop + 105, 16, 15, "-"));

        buttonList.add(new GuiButton(2, guiLeft + 14, guiTop + 50, 70, 20, "Mode switch"));
        
        buttonList.add(new GuiButton(3, guiLeft + 39, guiTop + 20, 54, 20, "Reset Link"));
        buttonList.add(new GuiButton(4, guiLeft + 95, guiTop + 20, 54, 20, "Full Reset"));
        
        buttonList.add(new GuiButton(5, guiLeft + 96, guiTop + 50, 70, 20, "Max frame"));
    }
    
    @Override
    protected void actionPerformed(GuiButton guibutton) 
    {        
    	switch(guibutton.id) 
    	{
	    	case 5:
	    		//TODO: set the max frame number
	    		//Controller.proxy.sendPacket(player, new int[]{guibutton.id,max});
	    	break;
            case 3: case 4://One of the "Reset" button has been pressed
            	ItemStack stack = this.inventorySlots.getSlot(0).getStack();
            	if(stack!=null && stack.hasTagCompound() && stack.getTagCompound().hasKey(ItemBase.KEYTAG))
            	{
            		int[] data = stack.getTagCompound().getIntArray(ItemBase.KEYTAG);
            		int[] cData = new int[1+data.length];
            		cData[0] = guibutton.id;
            		for(int i=0;i<data.length;i++)
            			cData[i+1]=data[i];
            		
            		Controller.proxy.sendPacket(player, cData);
            		break;
            	}
            	else if(guibutton.id==3)
            	{//This means if there wasn't an item, and full reset button was pressed, it falls back to sending the button id
            		break;
            	}
            default:
                Controller.proxy.sendPacket(player, guibutton.id);
                break;
        }
    	super.actionPerformed(guibutton);
    }
    
    public static float round(float value, int places) 
    {
        long factor = (long) Math.pow(10, places);
        long tmp = Math.round(value * factor);
        return (float) tmp / factor;
    }

}
