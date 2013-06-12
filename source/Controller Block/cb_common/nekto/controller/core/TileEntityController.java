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

public class TileEntityController extends TileEntity implements IInventory {
    
    public List<int[]> blockList = null;
    public boolean previousState = false;
    private ItemStack[] items;
	private ItemLinker linker = null;
    
    public TileEntityController()
    {
    	this.items = new ItemStack[this.getSizeInventory()];
    	this.blockList = new ArrayList<int[]>();
    }

	public void add(EntityPlayer player, int blockID, int x, int y, int z, int metaData) 
    {	
        boolean removed = false;
        int[] temp = new int[]{
            blockID,x,y,z,metaData
        };
        Iterator itr = blockList.listIterator();       
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
            blockList.add(temp);
            //this.worldObj.setBlockToAir(temp[1], temp[2], temp[3]);
        }
    }
    
    public void setState(boolean active)
    {
        this.previousState = active;
    }
    
    public void setLinker(ItemLinker par1Linker)
    {
        this.linker = par1Linker;
        //par1Linker;
    }
    
    public ItemLinker getLinker()
    {
        return this.linker;
    }
    
    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
    	super.writeToNBT(par1NBTTagCompound);
    	par1NBTTagCompound.setInteger("length", blockList.size());
        par1NBTTagCompound.setBoolean("active", this.previousState);
        for(int index = 0; index < blockList.size(); index++ )
        {
            par1NBTTagCompound.setIntArray(Integer.toString(index), blockList.get(index));
        }
    }
    
    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
    	super.readFromNBT(par1NBTTagCompound);
        int count = par1NBTTagCompound.getInteger("length");
        this.previousState = par1NBTTagCompound.getBoolean("active");

    	this.blockList.clear();
        for(int i = 0; i < count; i++)
        {
            this.blockList.add(par1NBTTagCompound.getIntArray(Integer.toString(i)));
        }
    }

	@Override
	public int getSizeInventory() {
		return 2;//1 slot for each block: controller activated, deactivated
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return items[i];
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		ItemStack aitemstack[] = items;
		if (aitemstack[i] != null) {
			if (aitemstack[i].stackSize <= j) {
				ItemStack itemstack = aitemstack[i];
				aitemstack[i] = null;
				onInventoryChanged();
				return itemstack;
			}
			ItemStack itemstack1 = aitemstack[i].splitStack(j);
			if (aitemstack[i].stackSize == 0)
				aitemstack[i] = null;
			onInventoryChanged();
			return itemstack1;
		}
		else
			return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		return null;//We don't want to drop anything on closing gui
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		items[i]=itemstack;
		onInventoryChanged();
	}

	@Override
	public String getInvName() {
		return "Controller.inventory";
	}

	@Override
	public boolean isInvNameLocalized() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;//Or do we want less ?
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return  entityplayer.isDead ? false : entityplayer.getDistanceSq(xCoord, yCoord, zCoord) <= 64D;
	}

	@Override
	public void openChest() {}

	@Override
	public void closeChest() {}

	@Override
	public boolean isStackValidForSlot(int i, ItemStack itemstack) {
		return i<getSizeInventory() && itemstack.getItem() instanceof ItemBlock;
	}
}
