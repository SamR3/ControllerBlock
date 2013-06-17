/*
 *  Author: Sam6982
 */
package nekto.controller.block;

import java.util.Iterator;

import nekto.controller.item.ItemLinker;
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
    public void breakBlock(World world, int par2, int par3, int par4, int par5, int par6)
    {
        TileEntityController tile = (TileEntityController) world.getBlockTileEntity(par2, par3, par4);
        if(!world.isRemote && tile.previousState && !tile.isEditing())//We only spawn items if it is powered and not in editing mode
        {
        	Iterator itr = tile.getBaseList().iterator();
        	dropItems(world, itr, par2, par3, par4);
        }
        super.breakBlock(world, par2, par3, par4, par5, par6);
    }
    
    @Override
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
    	TileEntityController tile = (TileEntityController) par1World.getBlockTileEntity(par2, par3, par4);
    	boolean flag =par1World.isBlockIndirectlyGettingPowered(par2, par3, par4);
        if( tile.previousState!=flag)
        {
        	Iterator itr = tile.getBaseList().iterator();
        
        	if(flag)
        	{
        		setActiveBlocks(par1World,itr);
        	}
            else
            {
            	setUnactiveBlocks(par1World,itr);
            }
        	tile.setState(flag);
        } 
    }

}
