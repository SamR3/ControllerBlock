package nekto.controller.core;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ContainerController extends Container{

	private int[] pos=new int[3];
	private TileEntityController control;
	public ContainerController(InventoryPlayer inventory,World world, int x, int y, int z){
		control=(TileEntityController) world.getBlockTileEntity(x, y, z);
		pos[0]=x;
		pos[1]=y;
		pos[2]=z;
		//TODO:Add controller slots
		//Adding player inventory
		for(int i = 0; i < 3; i++)
			for(int k = 0; k < 9; k++)
				addSlotToContainer(new Slot(inventory, k + i * 9 + 9, 8 + k * 18, 84 + i * 18));
		for(int j = 0; j < 9; j++)
			addSlotToContainer(new Slot(inventory, j, 8 + j * 18, 142));
	}
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return this.control.isUseableByPlayer(player);
	}
	@Override
    public ItemStack transferStackInSlot(EntityPlayer player, int i) {
		ItemStack itemstack = null;
		Slot slot = (Slot) this.inventorySlots.get(i);
		if (slot != null && slot.getHasStack()) {
		    ItemStack itemstack1 = slot.getStack();
		    itemstack = itemstack1.copy();
		    if (i < this.control.getSizeInventory()) //From block inventory to player inventory
		    { 
		    	if(!this.mergeItemStack(itemstack1, this.control.getSizeInventory(), this.inventorySlots.size(), true))
		    		return null;
		    } 	    
		    else if (!this.mergeItemStack(itemstack1, 2, this.control.getSizeInventory(), false))
		    	return null;
		    
		    if (itemstack1.stackSize == 0) 
		    {
		    	slot.putStack((ItemStack)null);
		    } 
		    else 
		    {
		    	slot.onSlotChanged();
		    }
		    if (itemstack1.stackSize != itemstack.stackSize)
		    {
		    	slot.onPickupFromSlot(player, itemstack1);
		    } 
		    else 
		    {
		    	return null;
		    }
		}
		return itemstack;
    }
}
