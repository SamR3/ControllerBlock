package nekto.controller.block;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import nekto.controller.animator.Mode;
import nekto.controller.core.Controller;
import nekto.controller.item.ItemRemote;
import nekto.controller.tile.TileEntityAnimator;
import nekto.controller.tile.TileEntityBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockAnimator extends BlockBase {
    
    public BlockAnimator(int id)
    {
        super(id);
        setUnlocalizedName("animator");
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return new TileEntityAnimator();
    }
    
    public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l)
    {
       return false;
    }

    public boolean isOpaqueCube()
    {
       return false;
    }
    
    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {    
    	if(par5EntityPlayer.getCurrentEquippedItem() == null || !(par5EntityPlayer.getCurrentEquippedItem().getItem() instanceof ItemRemote))
        {
	        if(!par1World.isBlockIndirectlyGettingPowered(par2, par3, par4))//We don't want to enable any changes when block is powered
	        {
	        	if(!par1World.isRemote && par1World.getBlockTileEntity(par2, par3, par4) instanceof TileEntityAnimator)
	        		par5EntityPlayer.openGui(Controller.instance, Controller.proxy.GUI_ID, par1World, par2, par3, par4);
	        	return true;
	        }
        }
        return false;
    }
    
    @Override
    protected void dropItems(World world, TileEntityBase tile, Iterator itr, int par2, int par3, int par4) 
	{
    	List<int[]> frames = null;
    	int index = 0;
    	while(itr.hasNext())
    	{
    		frames = (List<int[]>) itr.next();
    		if(frames!=null && index!=((TileEntityAnimator)tile).getFrame())
    			super.dropItems(world, tile, frames.iterator(), par2, par3, par4);
    		index++;
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
		//FMLLog.getLogger().info(tile.getDelay()+" ticked "+tile.getFrame());//DEBUG
		boolean flag = par1World.isBlockIndirectlyGettingPowered(par2, par3, par4);
		if(!(tile.isWaiting() && flag))
			if(flag || tile.getFrame()!=0)
	        {
	        	if(tile.getFrame() < tile.getBaseList().size())
	        		previousFrame(par1World ,tile);
	    		nextFrame(par1World, tile);
	        	par1World.scheduleBlockUpdate(par2, par3, par4, this.blockID, this.tickRate(par1World) + tile.getDelay());//Here we loop the ticks
	        	tile.setCount(tile.getCount()+1);
	        }
		if(!flag && tile.getFrame()==0)
    		((TileEntityAnimator)tile).setCount(0);
    }

	private void nextFrame(World par1World, TileEntityAnimator tile) 
	{
		switch(tile.getMode())
		{
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
		Iterator itr = tile.getBaseList().get(tile.getFrame()).listIterator();//build next frame
    	setUnactiveBlocks(par1World,itr);
	}

	private void previousFrame(World par1World, TileEntityAnimator tile) 
	{
		Iterator oldItr = tile.getBaseList().get(tile.getFrame()).listIterator();//erase previous frame
		setActiveBlocks(par1World,oldItr);
	}

	@Override
	public void onRedstoneChange(World par1World, int par2, int par3, int par4, int par5, boolean powered, TileEntityBase tile) 
	{
		if(powered)//Powered but previously not powered
    	{
			if(!((TileEntityAnimator)tile).hasRemoved())
			{
	    		for(int frame = 0; frame < tile.getBaseList().size(); frame++) 			
	    		{
	    			if(((TileEntityAnimator)tile).getFrame()!=frame)
	    			{
		    			Iterator itr = ((TileEntityAnimator)tile).getBaseList().get(frame).listIterator();
		            	setActiveBlocks(par1World,itr);	            	
	    			}
	    		}
	    		((TileEntityAnimator)tile).setRemoved(true);
			}
    	}
    	else//Not powered but previously powered
    	{
    		if(((TileEntityAnimator)tile).getMode()==Mode.ORDER || ((TileEntityAnimator)tile).getMode()==Mode.LOOP)
    			((TileEntityAnimator)tile).setMode(Mode.REVERSE);
    		else if(((TileEntityAnimator)tile).getMode()!=Mode.REVERSE)
    		{
	    		for(int frame = 0;frame < tile.getBaseList().size(); frame++)
	    		{
	    			Iterator itr = ((TileEntityAnimator)tile).getBaseList().get(frame).listIterator();
	            	setUnactiveBlocks(par1World,itr);//Make all the blocks reappear 	
	    		}
	    		((TileEntityAnimator)tile).setRemoved(false);
	    		((TileEntityAnimator)tile).setFrame(0);
    		}
    	}
		if(powered || ((TileEntityAnimator)tile).getFrame() != 0)
			par1World.scheduleBlockUpdate(par2, par3, par4, this.blockID, this.tickRate(par1World)+((TileEntityAnimator)tile).getDelay());
	}
}
