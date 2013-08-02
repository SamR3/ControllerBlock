package nekto.controller.container;

import nekto.controller.animator.Mode;
import nekto.controller.item.ItemBase;
import nekto.controller.tile.TileEntityAnimator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerAnimator extends ContainerBase {

	private int oldDelay,oldMode,oldFrame,oldMax=-1;
	private boolean isRemote,corner;
	private ItemBase remote;
	private final static int DELAY_INDEX=0,MODE_INDEX=1,FRAME_INDEX=2,MAX_INDEX=3,CORNER_INDEX=4;
	public ContainerAnimator(InventoryPlayer inventory, TileEntityAnimator tile, boolean hasSlots)
	{
		this.control = tile;
		this.oldDelay = tile.getDelay();
		
		this.oldMode = tile.getMode().ordinal();
		this.oldFrame = tile.getFrame();
		this.oldMax = tile.getMaxFrame();
		this.isRemote = !hasSlots;
		if(isRemote)
		{
			if(inventory.player.getHeldItem().getItem() instanceof ItemBase)
			{
				remote = (ItemBase)inventory.player.getHeldItem().getItem();
				this.corner = remote.isInCornerMode();
			}
		}
		else
		{
			//Adding animator slots
			addSlotToContainer(new ControllerSlot(tile, 0, 11, 21));
			//Adding player inventory
			addPlayerInventory(inventory);
		}
	}

    public int getDelay() 
    {
		return this.oldDelay;
	}

    public String getMode()
    {
    	return Mode.values()[this.oldMode].toString();
    }
    
    public String getCorner()
    {
    	return this.corner?"Corner":"Single";
    }

	public String getFrame() 
	{
		return "Frame: "+ new Integer(this.oldFrame + 1).toString();
	}
	
	public String getMax()
	{
		return "Max: "+ this.oldMax;
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player) 
	{
		return this.isRemote || super.canInteractWith(player);
	}
	
    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();
        for (int i = 0; i < this.crafters.size(); ++i)
        {
            ICrafting icrafting = (ICrafting)this.crafters.get(i);

	        if(this.oldDelay != ((TileEntityAnimator)this.control).getDelay())
	        {
	        	icrafting.sendProgressBarUpdate(this, DELAY_INDEX, ((TileEntityAnimator)this.control).getDelay());
	        	this.oldDelay = ((TileEntityAnimator)this.control).getDelay();
	        }
	        if(this.oldMode != ((TileEntityAnimator)this.control).getMode().ordinal())
	        {
	        	icrafting.sendProgressBarUpdate(this, MODE_INDEX, ((TileEntityAnimator)this.control).getMode().ordinal());
	        	this.oldMode = ((TileEntityAnimator)this.control).getMode().ordinal();
	        }
	        if(this.oldFrame != ((TileEntityAnimator)this.control).getFrame())
	        {
	        	icrafting.sendProgressBarUpdate(this, FRAME_INDEX, ((TileEntityAnimator)this.control).getFrame());
	        	this.oldFrame = ((TileEntityAnimator)this.control).getFrame();
	        }
	        if(this.oldMax != ((TileEntityAnimator)this.control).getMaxFrame())
	        {
	        	icrafting.sendProgressBarUpdate(this, MAX_INDEX, ((TileEntityAnimator)this.control).getMaxFrame());
	        	this.oldMax = ((TileEntityAnimator)this.control).getMaxFrame();
	        }
	        if(this.isRemote && this.corner != this.remote.isInCornerMode())
	        {
	        	icrafting.sendProgressBarUpdate(this, CORNER_INDEX, this.remote.isInCornerMode()?1:0);
    			this.corner = this.remote.isInCornerMode();
	        }
        }
    }
    
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2) 
    {
    	switch(par1){
    	case DELAY_INDEX:
    		((TileEntityAnimator)this.control).resetDelay();
    		((TileEntityAnimator)this.control).setDelay(par2);
    		this.oldDelay = par2;
    		break;
    	case MODE_INDEX:
    		((TileEntityAnimator)this.control).setMode(Mode.values()[par2]);
    		this.oldMode = par2;
    		break;
    	case FRAME_INDEX:
    		((TileEntityAnimator)this.control).setFrame(par2);
    		this.oldFrame = par2;
    		break;
    	case MAX_INDEX:
    		((TileEntityAnimator)this.control).setMaxFrame(par2);
    		this.oldMax = par2;
    		break;
    	case CORNER_INDEX:
    		this.remote.setCornerMode(par2==1);
    		this.corner = par2==1;
    		break;
    	}
    }

}
