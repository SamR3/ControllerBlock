/*
 *  Author: Sam6982
 */
package nekto.controller.tile;

import java.util.Arrays;
import java.util.Iterator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityController extends TileEntityBase<int[]> {
    
    public TileEntityController()
    {
    	super(2);
    }
    
    @Override
	public void add(EntityPlayer player, int blockID, int x, int y, int z, int metaData) 
    {	
        int[] temp = new int[]{blockID,x,y,z,metaData};
        Iterator itr = getBaseList().listIterator();
        
        boolean removed = removeFromList(itr, temp);
        sendMessage(player,removed,blockID,x,y,z,metaData);
        if(!removed)
            getBaseList().add(temp);
    }
    
    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
    	super.writeToNBT(par1NBTTagCompound);
    	for(int index = 0; index < getBaseList().size(); index++ )
        {
            par1NBTTagCompound.setIntArray(Integer.toString(index), (int[]) getBaseList().get(index));
        }
    }
    
    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
    	super.readFromNBT(par1NBTTagCompound);
    	int count = par1NBTTagCompound.getInteger("length");
	    for(int i = 0; i < count; i++)
	    {
	        this.getBaseList().add(par1NBTTagCompound.getIntArray(Integer.toString(i)));
	    }
    }
	@Override
	public String getInvName() 
	{
		return "Controller.inventory";
	}

	@Override
	public boolean isStackValidForSlot(int i, ItemStack itemstack) 
	{
		return i<getSizeInventory() && itemstack.getItem() instanceof ItemBlock;
	}
}
