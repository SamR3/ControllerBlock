package nekto.controller.container;

import nekto.controller.animator.Mode;
import nekto.controller.tile.TileEntityAnimator;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerAnimator extends ContainerBase {

	private int oldDelay,oldMode,oldFrame,oldMax=-1;
	private final static int DELAY_INDEX=0,MODE_INDEX=1,FRAME_INDEX=2,MAX_INDEX=3;
	public ContainerAnimator(InventoryPlayer inventory, TileEntityAnimator tile, boolean hasSlots)
	{
		this.control = tile;
		this.oldDelay = tile.getDelay();
		this.oldMode = tile.getMode().ordinal();
		this.oldFrame = tile.getFrame();
		this.oldMax = tile.getMaxFrame();
		if(hasSlots)
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

	public String getFrame() 
	{
		return "Frame: "+ new Integer(this.oldFrame + 1).toString();
	}
	
	public String getMax()
	{
		return "Max: "+ this.oldMax;
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
    	}
    }

}