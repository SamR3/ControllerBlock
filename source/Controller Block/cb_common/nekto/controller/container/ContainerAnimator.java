package nekto.controller.container;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nekto.controller.tile.TileEntityAnimator;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;

public class ContainerAnimator extends ContainerBase {

	private int oldDelay;
	private final static int DELAY_INDEX=0;
	public ContainerAnimator(InventoryPlayer inventory, TileEntityAnimator tile)
	{
		this.control = tile;
		//Adding animator slots
		addSlotToContainer(new ControllerSlot(tile, 0, 19, 21));
		//Adding player inventory
		addPlayerInventory(inventory);
	}

    public int getDelay() 
    {
		return oldDelay;
	}
    
    /**
     * Looks for changes made in the container, sends them to every listener.
     */
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
	        }
        }
    }
    
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2) 
    {
    	if(par1==DELAY_INDEX)
    		this.oldDelay = par2;
    }
}
