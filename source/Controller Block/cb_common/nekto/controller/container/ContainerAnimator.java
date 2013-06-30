package nekto.controller.container;

import nekto.controller.tile.TileEntityAnimator;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerAnimator extends ContainerBase {

	public ContainerAnimator(InventoryPlayer inventory, TileEntityAnimator tile)
	{
		this.control = tile;
		//Adding animator slots
		
		/*GLITCH:
		 * 
		 *  Causes Game Crash
		 * 
		 * */
		//addSlotToContainer(new ControllerSlot(tile, 1, 19, 21));
		
		
		//Adding player inventory
		addPlayerInventory(inventory);
	}
}
