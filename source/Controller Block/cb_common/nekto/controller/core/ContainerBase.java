package nekto.controller.core;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerBase extends Container{

	protected TileEntityBase control;
	@Override
	public boolean canInteractWith(EntityPlayer player) 
	{
		return this.getControl().isUseableByPlayer(player);
	}

	public TileEntityBase getControl() 
	{
		return this.control;
	}
	@Override
    public ItemStack transferStackInSlot(EntityPlayer player, int i) 
	{
		ItemStack itemstack = null;
		Slot slot = (Slot) this.inventorySlots.get(i);
		if (slot != null && slot.getHasStack()) {
		    ItemStack itemstack1 = slot.getStack();
		    itemstack = itemstack1.copy();
		    if (i < this.getControl().getSizeInventory()) //From block inventory to player inventory
		    { 
		    	if(!this.mergeItemStack(itemstack1, this.getControl().getSizeInventory(), this.inventorySlots.size(), true))
		    		return null;
		    } 	    
		    else if (!this.mergeItemStack(itemstack1, 2, this.getControl().getSizeInventory(), false))
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

	protected void addPlayerInventory(InventoryPlayer inventory) 
	{
		for(int i = 0; i < 3; i++)
			for(int k = 0; k < 9; k++)
				addSlotToContainer(new Slot(inventory, k + i * 9 + 9, 8 + k * 18, 84 + i * 18));
		for(int j = 0; j < 9; j++)
			addSlotToContainer(new Slot(inventory, j, 8 + j * 18, 142));
	}
}
