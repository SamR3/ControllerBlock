package nekto.controller.core;

import net.minecraft.entity.player.InventoryPlayer;

public class ContainerController extends ContainerBase {
	
	public ContainerController(InventoryPlayer inventory, TileEntityController tile)
	{
		this.control = tile;
		//Adding controller slots
		addSlotToContainer(new ControllerSlot(tile, 0, 29, 21));
		addSlotToContainer(new ControllerSlot(tile, 1, 29, 52));
		//Adding player inventory
		addPlayerInventory(inventory);
	}
}
