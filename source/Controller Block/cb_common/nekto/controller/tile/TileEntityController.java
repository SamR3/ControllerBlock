/*
 *  Author: Sam6982
 */
package nekto.controller.tile;

import java.util.List;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityController extends TileEntityBase<int[]> {
    
    public TileEntityController()
    {
    	super(2);
    }

	@Override
	protected List getBlockList() 
	{
		return getBaseList();
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

	@Override
	public String getTexture()
	{
		return "models/controller.png";
	}
}
