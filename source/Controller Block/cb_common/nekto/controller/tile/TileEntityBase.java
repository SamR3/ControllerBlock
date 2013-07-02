package nekto.controller.tile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import nekto.controller.item.ItemBase;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public abstract class TileEntityBase<e> extends TileEntity implements IInventory {
	private ItemStack[] items;
	public boolean previousState = false;
	private List<e> baseList;
	private int size;
	private ItemBase linker = null;
	private boolean editing;
	
	public TileEntityBase(int size)
    {
		this.size = size;
    	this.items = new ItemStack[size];
    	this.setBaseList(new ArrayList<e>());
    }

	protected boolean removeFromList(Iterator itr,int[] temp)
	{
		while(itr.hasNext())
        {
        	if(Arrays.equals((int[]) itr.next(),temp))
        	{
        		itr.remove();
        		return true;
        	}
        }
		return false;
	}
	
    protected static void sendMessage(EntityPlayer player, boolean removed, int blockID, int x, int y, int z, int metaData) 
    {
    	String name = Block.blocksList[blockID].getUnlocalizedName().substring(5);
    	if(removed) 
    	{
        	player.sendChatToPlayer("Removed from list " + name + " " + x + " " + y + " " + z + " " + metaData);
        } else 
        {
        	player.sendChatToPlayer("Added to list " + name + " " + x + " " + y + " " + z + " " + metaData);
        }
	}
	
    public void setState(boolean active)
    {
        this.previousState = active;
    }
    
    public boolean isPowered()
    {
    	return this.previousState;
    }
    
    public List<e> getBaseList() 
    {
		return baseList;
	}

	public void setBaseList(List baseList) 
	{
		this.baseList = baseList;
	}
	
	public void setLinker(ItemBase par1Linker)
    {
        this.linker = par1Linker;
    }
    
    public ItemBase getLinker()
    {
        return this.linker;
    }
    
    public boolean isEditing() 
    {
		return this.editing;
	}

	public void setEditing(boolean b) 
	{
		this.editing = b;
	}
	
    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
    	super.writeToNBT(par1NBTTagCompound);
    	par1NBTTagCompound.setInteger("length", getBaseList().size());
        par1NBTTagCompound.setBoolean("active", this.previousState);
        par1NBTTagCompound.setBoolean("edit", this.isEditing());
    }
    
    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
    	super.readFromNBT(par1NBTTagCompound);
        this.previousState = par1NBTTagCompound.getBoolean("active");
        this.setEditing(par1NBTTagCompound.getBoolean("edit"));
    	this.getBaseList().clear();
    }
    
	@Override
	public int getSizeInventory() 
	{
		return this.size;
	}

	@Override
	public ItemStack getStackInSlot(int i) 
	{
		return items[i];
	}

	@Override
	public ItemStack decrStackSize(int i, int j) 
	{
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
	public ItemStack getStackInSlotOnClosing(int i) 
	{
		return null;
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) 
	{
		items[i]=itemstack;
		onInventoryChanged();
	}

	@Override
	public boolean isInvNameLocalized() 
	{
		return false;
	}

	@Override
	public int getInventoryStackLimit() 
	{
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) 
	{
		return  entityplayer.isDead ? false : entityplayer.getDistanceSq(xCoord, yCoord, zCoord) <= 64D;
	}

	@Override
	public void openChest() {}

	@Override
	public void closeChest() {}

	public abstract void add(EntityPlayer player, int blockId, int x, int y, int z, int blockMetadata);
}
