/*
 *  Author: Sam6982
 */
package nekto.controller.core;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

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
        		par1World.scheduleBlockUpdate(par2, par3, par4, this.blockID, this.tickRate(par1World)+tile.getDelay());
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
        return 2;
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
        	{	
        		previousFrame(par1World,tile);
        		nextFrame(par1World,tile);
        	}
        	par1World.scheduleBlockUpdate(par2, par3, par4, this.blockID, this.tickRate(par1World)+tile.getDelay());//Here we loop the ticks
        }
    }

	private void nextFrame(World par1World, TileEntityAnimator tile) {
		switch(tile.getMode()){
		case LOOP:
			if(tile.getFrame()+1 >= tile.getBaseList().size())
				tile.setFrame(0);
			else
				tile.setFrame(tile.getFrame()+1);
			break;
		case RANDOM:
			tile.setFrame(par1World.rand.nextInt(tile.getBaseList().size()));
			break;
		case ORDER:
			if(tile.getFrame()+1 >= tile.getBaseList().size())
			{
				tile.setFrame(tile.getFrame()-1);
				tile.setMode(Mode.REVERSE);
			}
			else
				tile.setFrame(tile.getFrame()+1);
			break;
		case REVERSE:
			if(tile.getFrame() == 0)
			{
				tile.setFrame(1);
				tile.setMode(Mode.ORDER);
			}
			else
				tile.setFrame(tile.getFrame()-1);
			break;
		}
		Iterator itr = tile.getBaseList().get(tile.getFrame()).iterator();//build next frame
    	setUnactiveBlocks(par1World,itr);
	}

	private void previousFrame(World par1World, TileEntityAnimator tile) 
	{
		Iterator oldItr = tile.getBaseList().get(tile.getFrame()).iterator();//erase previous frame
		setActiveBlocks(par1World,oldItr);
	}
}
