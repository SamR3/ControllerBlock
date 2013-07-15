package nekto.controller.client;

import nekto.controller.gui.AnimatorGUI;
import nekto.controller.network.CommonProxy;
import nekto.controller.render.TileEntityAnimatorRenderer;
import nekto.controller.tile.TileEntityAnimator;
import nekto.controller.tile.TileEntityBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy {

    public void registerRenderThings() 
    {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBase.class, new TileEntityAnimatorRenderer());
    }
    @Override
    public World getClientWorld() 
	{
    	return FMLClientHandler.instance().getClient().theWorld;
	}
    @Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) 
	{	       
        TileEntity tile = world.getBlockTileEntity(x, y, z);
        
        if(ID == GUI_ID && tile instanceof TileEntityAnimator)
        {
			return new AnimatorGUI(player.inventory, (TileEntityAnimator) tile);
        }
			
	    return null;
	}
}
