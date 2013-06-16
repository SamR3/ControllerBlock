package nekto.controller.core;

import net.minecraft.entity.player.InventoryPlayer;

public class ContainerAnimator extends ContainerBase {

	public ContainerAnimator(InventoryPlayer inventory, TileEntityAnimator tile)
	{
		this.control = tile;
		//Adding animator slots
		addSlotToContainer(new ControllerSlot(tile, 0, 29, 21));
		//Adding player inventory
		addPlayerInventory(inventory);
	}
}
