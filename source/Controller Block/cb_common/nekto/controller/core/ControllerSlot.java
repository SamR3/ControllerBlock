package nekto.controller.core;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ControllerSlot extends Slot{

	private TileEntityBase control;
	public ControllerSlot(IInventory par1iInventory, int par2, int par3,
			int par4) {
		super(par1iInventory, par2, par3, par4);
		this.control = (TileEntityBase) par1iInventory;
	}
	@Override
	public boolean isItemValid(ItemStack stack)
    {
        return this.control.isStackValidForSlot(slotNumber, stack);
    }
}
