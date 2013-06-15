/*
 *  Author: Sam6982
 */
package nekto.controller.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityController extends TileEntityBase<int[]> {
    
    public TileEntityController()
    {
    	super(2);
    }
    
    @Override
	public void add(EntityPlayer player, int frame, int blockID, int par4, int par5, int par6, int metaData) 
    {
		this.add(player, blockID, par4, par5, par6, metaData);
	}
    
    @Override
	public void add(EntityPlayer player, int blockID, int x, int y, int z, int metaData) 
    {	
        boolean removed = false;
        int[] temp = new int[]{
            blockID,x,y,z,metaData
        };
        Iterator itr = getBaseList().listIterator();       
        while(itr.hasNext()){
        	if(Arrays.equals((int[]) itr.next(), temp)){
        		itr.remove();
        		removed = true;
        		break;
        	}
        }
        
        if(removed) {
        	player.sendChatToPlayer("Removed " + " " + blockID + " " + x + " " + y + " " + z + " " + metaData);
        } else {
        	player.sendChatToPlayer("Added " + " " + blockID + " " + x + " " + y + " " + z + " " + metaData);
            getBaseList().add(temp);
            //this.worldObj.setBlockToAir(temp[1], temp[2], temp[3]);
        }
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
	public String getInvName() {
		return "Controller.inventory";
	}

	@Override
	public boolean isStackValidForSlot(int i, ItemStack itemstack) {
		return i<getSizeInventory() && itemstack.getItem() instanceof ItemBlock;
	}
}
