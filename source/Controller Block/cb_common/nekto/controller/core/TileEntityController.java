/*
 *  Author: Sam6982
 */
package nekto.controller.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityController extends TileEntity  implements IInventory{
    
    //private int currIndex = 0;
    public ArrayList<int[]> blockList = null;
    public boolean state = true;
    public boolean isPowered = false;
	private ItemStack[] items;
    
    public TileEntityController(){
    	this.items = new ItemStack[this.getSizeInventory()];
    	this.blockList = new ArrayList<int[]>();
    }
    @Override
    public void updateEntity()
    {
        isPowered = worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord);
    }
    
    public void add(EntityPlayer player, int blockID, int x, int y, int z, int metaData) 
    {	
        boolean removed = false;
        int[] temp = new int[]{
            blockID,x,y,z,metaData
        };
        Iterator itr = blockList.iterator();
        int index=0;
        while(itr.hasNext()){
        	
        	if(Arrays.equals((int[]) itr.next(),temp)){
        		blockList.remove(index);
        		removed=true;
        		break;
        	}
    		index++;
        }
        if(removed) {
        	player.sendChatToPlayer("Removed " + " " + blockID + " " + x + " " + y + " " + z + " " + metaData);
        } else {
        	player.sendChatToPlayer("Added " + " " + blockID + " " + x + " " + y + " " + z + " " + metaData);
            blockList.add(temp);
        }
    }
    
    public void activate(boolean active)
    {
        if(!blockList.isEmpty())
	        if(!active )
	        {
	            for(int[] elem : blockList)
	            {
	            	if(elem!=null && elem.length>4)
	            		this.worldObj.setBlockToAir(elem[1], elem[2], elem[3]);
	            }
	        } else {
	            for(int[] elem : blockList)
	            {
	            	if(elem!=null && elem.length>5)
	            		this.worldObj.setBlock(elem[1], elem[2], elem[3], elem[0], elem[4], 2);
	            }
	        }
        
        state = !active;
    }
    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
    	super.writeToNBT(par1NBTTagCompound);
    	blockList.trimToSize();
        par1NBTTagCompound.setInteger("length", blockList.size());
        par1NBTTagCompound.setBoolean("active", this.state);
        for(int index=0;index<blockList.size();index++ )
        {
            par1NBTTagCompound.setIntArray(Integer.toString(index), blockList.get(index));
        }
    }
    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
    	super.readFromNBT(par1NBTTagCompound);
        int count = par1NBTTagCompound.getInteger("length");
        this.state = par1NBTTagCompound.getBoolean("active");

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
		return  entityplayer.isDead?false:entityplayer.getDistanceSq(xCoord,yCoord,zCoord) <= 64D;
	}

	@Override
	public void openChest() {}

	@Override
	public void closeChest() {}

	@Override
	public boolean isStackValidForSlot(int i, ItemStack itemstack) {
		return true;//TODO:Do we need any limitations ?
	}
    
    /*public void add(int blockID, int x, int y, int z, int metaData) 
    {
        ModLoader.getMinecraftInstance().thePlayer.addChatMessage("Made it here! The info passed is " + " " + blockID + " " + x + " " + y + " " + z + " " + metaData);
        
        this.blockList[this.currIndex][0] = blockID;
        this.blockList[this.currIndex][1] = x;
        this.blockList[this.currIndex][2] = y;
        this.blockList[this.currIndex][3] = z;
        this.blockList[this.currIndex][4] = metaData;
        
        this.currIndex++;
    }
    
    public void activate()
    {
        if(!state)
        {
            for(int i = currIndex; i > 0; i--)
            {
                this.worldObj.setBlockToAir(this.blockList[i][1], this.blockList[i][2], this.blockList[i][3]);
            }
        } else {
            for(int i = currIndex; i > 0; i--)
            {
                this.worldObj.setBlock(this.blockList[i][1], this.blockList[i][2], this.blockList[i][3], this.blockList[i][0]);
            }
        }
        
        state = !state;
    }
    
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        par1NBTTagCompound.setBoolean("active", this.state);
        par1NBTTagCompound.setInteger("num", currIndex);
        
        for(int i = currIndex; i > 0; i--)
        {
            if(this.blockList[i] != null)
            {
                par1NBTTagCompound.setIntArray(Integer.toString(i), this.blockList[i]);
            } else {
                break;
            }
        }
    }
    
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        this.state = par1NBTTagCompound.getBoolean("active");
        this.currIndex = par1NBTTagCompound.getInteger("num");
        
        for(int i = currIndex; i > 0; i--)
        {
            this.blockList[i] = par1NBTTagCompound.getIntArray(Integer.toString(i));
        }
    }*/
}
