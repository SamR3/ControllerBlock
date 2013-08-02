package nekto.controller.gui;

import java.util.List;

import nekto.controller.container.ContainerAnimator;
import nekto.controller.container.ContainerBase;
import nekto.controller.core.Controller;
import nekto.controller.item.ItemBase;
import nekto.controller.network.PacketHandler;
import nekto.controller.tile.TileEntityAnimator;
import nekto.controller.tile.TileEntityBase;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
//import net.minecraft.client.resources.ResourceLocation;

@SideOnly(Side.CLIENT)
public class AnimatorGUI extends GuiContainer {

    private List<GuiButton> frameButtons;
	private boolean remote;

    public AnimatorGUI(InventoryPlayer par1InventoryPlayer, TileEntityAnimator par2TileEntity, boolean isRemote)
    {
        super(new ContainerAnimator(par1InventoryPlayer, par2TileEntity, !isRemote));
        this.remote = isRemote;
        if(remote)
        {
        	xSize = 256;
        	ySize = 596;
        }
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
    	if(!remote)
    	{
	        String s = "Animator Block";
	        this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);
	        int delay = ((ContainerAnimator)this.inventorySlots).getDelay()+2;
	        if(Controller.tickDisplay)
	        	s = (delay) + "ticks";
	        else
	        {
	        	s = Float.toString((delay)*0.05F);
	        	if(s.length()>3)
	        		s = s.substring(0, 4);
	        	s = s+ "s"; 
	        }
	        this.fontRenderer.drawString(s, 131 - this.fontRenderer.getStringWidth(s) / 2, 87, 0);   
    	}
    	else
    	{
    		TileEntityBase control = ((ContainerBase)this.inventorySlots).getControl();
            String s = "Linked to "+control.getName()+" @";
            this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 40, 4210752);
            s = control.xCoord+", "+control.yCoord+", "+control.zCoord;
            this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 50, 4210752);
        }
    	refreshButtonsText();
    }

	@Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(getTexture());
        //this.mc.renderEngine.func_110577_a(new ResourceLocation("controller","/textures/gui/controllergui.png"));
    	this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, this.xSize, this.ySize);
    }
    
    private String getTexture() 
    {
		return "/mods/controller/textures/gui/"+(remote ? "remote":"controller")+"gui.png";
	}

	@Override
    public void initGui() 
    {
        super.initGui();
        //id, x, y, width, height, text
        if(remote)
        {
        	buttonList.add(new GuiButton(0, guiLeft + 85, guiTop + 300, 82, 20, ((ContainerAnimator)this.inventorySlots).getFrame()));
        	buttonList.add(new GuiButton(1, guiLeft + 107, guiTop + 330, 36, 20, ((ContainerAnimator)this.inventorySlots).getCorner()));
        	buttonList.add(new GuiButton(2, guiLeft + 95, guiTop + 360, 60, 20, "Reset Link"));
        	guiTop = 0;
        }
        else
        {
	        buttonList.add(new GuiButton(0, guiLeft + 149, guiTop + 81, 19, 20, "+"));
	        buttonList.add(new GuiButton(1, guiLeft + 96, guiTop + 81, 19, 20, "-"));
	
	        buttonList.add(new GuiButton(2, guiLeft + 10, guiTop + 81, 82, 20, ((ContainerAnimator)this.inventorySlots).getMode()));
	        
	        buttonList.add(new GuiButton(3, guiLeft + 32, guiTop + 19, 60, 20, "Reset Link"));
	        buttonList.add(new GuiButton(4, guiLeft + 96, guiTop + 19, 70, 20, "Force Reset"));
	        
	        buttonList.add(new GuiButton(5, guiLeft + 96, guiTop + 50, 70, 20, ((ContainerAnimator)this.inventorySlots).getMax()));
	        buttonList.add(new GuiButton(6, guiLeft + 10, guiTop + 50, 82, 20, ((ContainerAnimator)this.inventorySlots).getFrame()));
        }
    }
    
    private void refreshButtonsText()
    {
    	if(!remote)
    	{
    		((GuiButton)this.buttonList.get(2)).displayString = ((ContainerAnimator)this.inventorySlots).getMode();
    		((GuiButton)this.buttonList.get(5)).displayString = ((ContainerAnimator)this.inventorySlots).getMax();
    		((GuiButton)this.buttonList.get(6)).displayString = ((ContainerAnimator)this.inventorySlots).getFrame();
    	}
    	else
    	{
    		((GuiButton)this.buttonList.get(0)).displayString = ((ContainerAnimator)this.inventorySlots).getFrame();
    		((GuiButton)this.buttonList.get(1)).displayString = ((ContainerAnimator)this.inventorySlots).getCorner();
    	}
	}

	@Override
    protected void actionPerformed(GuiButton guibutton) 
    {
    	super.actionPerformed(guibutton);
    	TileEntityAnimator animator = (TileEntityAnimator) ((ContainerAnimator)this.inventorySlots).getControl();
        Packet packet = null;
        int[] data = null;
    	switch(guibutton.id)
    	{
    	    case 3: case 4://One of the "Reset" button has been pressed
            	ItemStack stack = this.inventorySlots.getSlot(0).getStack();
            	if(stack != null && stack.hasTagCompound() && stack.getTagCompound().hasKey(ItemBase.KEYTAG))
            	{
            		data = stack.getTagCompound().getIntArray(ItemBase.KEYTAG);
            		break;
            	}
            default:
                break;
        }
    	if(data!=null)
    	{
	    	int[] cData = new int[4+data.length];
			cData[0] = guibutton.id;
			cData[1] = animator.xCoord;
			cData[2] = animator.yCoord;
			cData[3] = animator.zCoord;
			for(int i = 0; i < data.length; i++)
				cData[i + 4] = data[i];
	    	packet = PacketHandler.getGuiPacket(remote, cData);
    	}
    	else
    	{
    		packet = PacketHandler.getGuiPacket(remote, guibutton.id, animator.xCoord, animator.yCoord, animator.zCoord);
    	}
    	if(packet!=null)
    		this.mc.getNetHandler().addToSendQueue(packet);
    	refreshButtonsText();
    }
}
