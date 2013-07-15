package nekto.controller.network;

import nekto.controller.container.ContainerAnimator;
import nekto.controller.tile.TileEntityAnimator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class CommonProxy implements IGuiHandler{

	public static final int GUI_ID = 0;
	
    public void registerRenderThings() {}
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) 
	{
	    TileEntity tile = world.getBlockTileEntity(x, y, z);
	    
		if(ID == GUI_ID && tile instanceof TileEntityAnimator)
		{
			return new ContainerAnimator(player.inventory, (TileEntityAnimator) tile);
		}
		
		return null;
	}
	
	@Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        return null;
    }

	public World getClientWorld() 
	{
		return null;
	}
}
