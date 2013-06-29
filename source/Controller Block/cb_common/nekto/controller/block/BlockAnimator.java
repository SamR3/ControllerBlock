package nekto.controller.block;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import cpw.mods.fml.common.FMLLog;

import nekto.controller.animator.Mode;
import nekto.controller.core.Controller;
import nekto.controller.item.ItemBase;
import nekto.controller.item.ItemRemote;
import nekto.controller.tile.TileEntityAnimator;
import nekto.controller.tile.TileEntityBase;
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
    	
        if(par5EntityPlayer.getCurrentEquippedItem() != null && par5EntityPlayer.getCurrentEquippedItem().getItem() instanceof ItemRemote)
        {
            return false;
        }

        par5EntityPlayer.openGui(Controller.instance, Controller.proxy.GUI_ID, par1World, par2, par3, par4);
        
        return true;
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
		FMLLog.getLogger().info(tile.getDelay()+"ticked "+tile.getFrame());
		boolean flag = par1World.isBlockIndirectlyGettingPowered(par2, par3, par4);
		if(flag)
        {
        	if(tile.getFrame() < tile.getBaseList().size())
        		previousFrame(par1World ,tile);
    		nextFrame(par1World, tile);
        	par1World.scheduleBlockUpdate(par2, par3, par4, this.blockID, this.tickRate(par1World) + tile.getDelay());//Here we loop the ticks
        }
    }

	private void nextFrame(World par1World, TileEntityAnimator tile) 
	{
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

	@Override
	public void onRedstoneChange(World par1World, int par2, int par3, int par4, int par5, boolean powered, TileEntityBase tile) 
	{
		if(powered)//Powered but previously not powered
    	{
    		for(int frame = 0; frame < tile.getBaseList().size(); frame++)   			
    		{
    			Iterator itr = ((TileEntityAnimator)tile).getBaseList().get(frame).iterator();
            	setActiveBlocks(par1World,itr);//Make all the blocks disappear
    		}
    		par1World.scheduleBlockUpdate(par2, par3, par4, this.blockID, this.tickRate(par1World)+((TileEntityAnimator)tile).getDelay());
    	}
    	else//Not powered but previously powered
    	{
    		for(int frame = 0;frame < tile.getBaseList().size(); frame++)
    		{
    			Iterator itr = ((TileEntityAnimator)tile).getBaseList().get(frame).iterator();
            	setUnactiveBlocks(par1World,itr);//Make all the blocks reappear
    		}
    	}
	}
}
