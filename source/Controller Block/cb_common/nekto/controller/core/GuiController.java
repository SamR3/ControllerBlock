package nekto.controller.core;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;
@SideOnly(Side.CLIENT)
public class GuiController extends GuiContainer{

	public GuiController(InventoryPlayer inventory, World world, int x, int y, int z) {
		super(new ContainerController(inventory, world, x, y, z));
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		// TODO Add a gui
		
	}

}
