package nekto.controller.core;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ContainerAnimator extends ContainerBase {

	public ContainerAnimator(InventoryPlayer inventory, TileEntityAnimator tile){
		this.control = tile;
		//Adding animator slots
		addSlotToContainer(new ControllerSlot(tile, 0, 29, 21));
		//Adding player inventory
		addPlayerInventory(inventory);
	}
}
