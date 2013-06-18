/*
 *  Author: Sam6982
 */
package nekto.controller.block;

import java.util.Iterator;

import nekto.controller.item.ItemLinker;
import nekto.controller.tile.TileEntityBase;
import nekto.controller.tile.TileEntityController;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockController extends BlockBase {
	
    public BlockController(int id)
    {
        super(id);
        setUnlocalizedName("controllerBlock");
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return new TileEntityController();
    }
    
    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {    
        if(par5EntityPlayer.getCurrentEquippedItem() != null && par5EntityPlayer.getCurrentEquippedItem().getItem() instanceof ItemLinker)
        {
            return false;
        }
        
        /*TileEntityController tile = (TileEntityController)par1World.getBlockTileEntity(par2, par3, par4);

        if (tile != null)
        {
            par5EntityPlayer.openGui(Controller.instance, Controller.proxy.GUI_ID, par1World, par2, par3, par4);
        }*/

        return false;
    }

	@Override
	public void onRedstoneChange(World par1World, int par2, int par3, int par4, int par5, boolean powered, TileEntityBase tile) 
	{
		if(powered)
    	{
    		setActiveBlocks(par1World,tile.getBaseList().iterator());
    	}
        else
        {
        	setUnactiveBlocks(par1World,tile.getBaseList().iterator());
        }
	}

}
