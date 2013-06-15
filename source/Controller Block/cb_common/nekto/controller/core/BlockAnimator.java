/*
 *  Author: Sam6982
 */
package nekto.controller.core;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import nekto.controller.ref.GeneralRef;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockAnimator extends BlockBase {
    
    public BlockAnimator(int id)
    {
        super(id);
        setUnlocalizedName("animatorBlock");
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return new TileEntityAnimator();
    }
    
    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {    
    	TileEntityAnimator tile = (TileEntityAnimator)par1World.getBlockTileEntity(par2, par3, par4);
        if(par5EntityPlayer.getCurrentEquippedItem()!=null && par5EntityPlayer.getCurrentEquippedItem().getItem() instanceof ItemRemote)
        {
        	if(par5EntityPlayer.getCurrentEquippedItem().hasTagCompound() && par5EntityPlayer.isSneaking())
        	{
        		((ItemRemote)par5EntityPlayer.getCurrentEquippedItem().getItem()).resetLinker();
        		par5EntityPlayer.getCurrentEquippedItem().getTagCompound().removeTag(ItemBase.KEYTAG);
        		tile.setEditing(false);
        		tile.setLinker(null);
        	}
            return false;
        }

        return false;
    }
    
    @Override
    public void breakBlock(World world, int par2, int par3, int par4, int par5, int par6)
    {
        TileEntityAnimator tile = (TileEntityAnimator) world.getBlockTileEntity(par2, par3, par4);
        
        for(int frame=0;frame<tile.getBaseList().size();frame++)
        {
	        Iterator itr = tile.getBaseList().get(frame).iterator();	        
	        if(!world.isRemote && tile.previousState && frame!=tile.getFrame() && !tile.isEditing())//We only spawn items if it is powered and not in editing mode
	        {
	        	dropItems(world, itr, par2, par3, par4);
	        }
        }
        super.breakBlock(world, par2, par3, par4, par5, par6);
    }
   
	@Override
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
    	TileEntityAnimator tile = (TileEntityAnimator) par1World.getBlockTileEntity(par2, par3, par4);
    	//FMLLog.getLogger().info("change "+tile.getFrame());
    	boolean flag =par1World.isBlockIndirectlyGettingPowered(par2, par3, par4);
        if( tile.previousState!=flag)
        {
        	if(flag)//Powered but previously not powered
        	{
        		for(int frame=0;frame<tile.getBaseList().size();frame++)   			
        		{
        			Iterator itr = tile.getBaseList().get(frame).iterator();
                	setActiveBlocks(par1World,itr);//Make all the blocks disappear
        		}
        		par1World.scheduleBlockUpdate(par2, par3, par4, this.blockID, this.tickRate(par1World));
        	}
        	else//Not powered but previously powered
        	{
        		for(int frame=0;frame<tile.getBaseList().size();frame++)
        		{
        			Iterator itr = tile.getBaseList().get(frame).iterator();
                	setUnactiveBlocks(par1World,itr);//Make all the blocks reappear
        		}
        	}
        	tile.setState(flag);//Change the flag state
        }
    }

	@Override
	public int tickRate(World par1World)
    {
        return 2;//Here we can set the timer
    }
	
	@Override
	public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
		TileEntityAnimator tile = (TileEntityAnimator) par1World.getBlockTileEntity(par2, par3, par4);
		//FMLLog.getLogger().info("ticked "+tile.getFrame());
		boolean flag =par1World.isBlockIndirectlyGettingPowered(par2, par3, par4);
		if(flag)
        {
        	if(tile.getFrame()<tile.getBaseList().size())
        	{	if(tile.getFrame() > 0 )
	        	{
					Iterator oldItr = tile.getBaseList().get(tile.getFrame()-1).iterator();//erase previous frame
	        		setActiveBlocks(par1World,oldItr);
	        	}
	        	Iterator itr = tile.getBaseList().get(tile.getFrame()).iterator();//build next frame
	        	setUnactiveBlocks(par1World,itr);
	        	tile.setFrame(tile.getFrame()+1);
        	}
        	par1World.scheduleBlockUpdate(par2, par3, par4, this.blockID, this.tickRate(par1World));//Here we loop the ticks
        }
    }
}
