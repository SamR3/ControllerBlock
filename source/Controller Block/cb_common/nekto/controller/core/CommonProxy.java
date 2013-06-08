package nekto.controller.core;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class CommonProxy implements IGuiHandler{

	public static final int GUI_ID=0;
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		if(ID==GUI_ID)
			return new ContainerController(player.inventory,world,x,y,z);
		else
			return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		if(ID==GUI_ID)
			return new GuiController(player.inventory,world,x,y,z);
		else
			return null;
	}

}
