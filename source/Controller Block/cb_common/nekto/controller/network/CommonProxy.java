package nekto.controller.network;

import nekto.controller.container.ContainerAnimator;
import nekto.controller.ref.GeneralRef;
import nekto.controller.tile.TileEntityAnimator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class CommonProxy implements IGuiHandler{

	public void registerRenderThings() {}
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) 
	{
	    TileEntity tile = world.getBlockTileEntity(x, y, z);
	    if(tile instanceof TileEntityAnimator)
	        if(ID == GeneralRef.GUI_ID )
			{
				return new ContainerAnimator(player.inventory, (TileEntityAnimator) tile, true);
			}
			else if(ID == GeneralRef.REMOTE_GUI_ID)
			{
				return new ContainerAnimator(player.inventory, (TileEntityAnimator) tile, false);
			}
		
		return null;
	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) 
	{
	    return null;
	}
}
