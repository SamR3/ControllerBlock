package nekto.controller.tile;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nekto.controller.animator.Mode;
import nekto.controller.item.ItemRemote;
import nekto.controller.network.PacketHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet;

public class TileEntityAnimator extends TileEntityBase<List<int[]>> {
    
    private int frame = 0, delay = 0, count = 0, max = -1;
    private Mode currMode = Mode.ORDER;
    private float orbRotation = 0;
    private float hoverHeight = 0;
	private boolean removed;
	
    public TileEntityAnimator()
    {
    	super(1);
    }

	@Override
	protected List getBlockList() 
	{	
		return getBaseList().get(frame);
	}
	
	@Override
	protected String getListName()
    {
		return "frame "+(frame+1);
	}  
	
	@Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
    	super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setInteger("frame", this.frame);
        par1NBTTagCompound.setInteger("delay", this.delay);
        par1NBTTagCompound.setInteger("max", this.max);
        par1NBTTagCompound.setInteger("count", this.count);
        par1NBTTagCompound.setShort("mode", (short) this.getMode().ordinal());
        par1NBTTagCompound.setBoolean("removed", this.removed);
        
        NBTTagList tags = new NBTTagList("frames");
        
        for(int index = 0; index < getBaseList().size(); index++ )
        {
        	NBTTagCompound tag = new NBTTagCompound(Integer.toString(index));
			for(int block = 0; block < getBaseList().get(index).size(); block++)
			{
        		tag.setIntArray( Integer.toString(block), ((int[]) getBaseList().get(index).get(block)));
			}
        	tags.appendTag(tag);
        }
        par1NBTTagCompound.setTag("frames", tags);
    }
    
    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
    	super.readFromNBT(par1NBTTagCompound);
        int count = par1NBTTagCompound.getInteger("length");
        this.frame = par1NBTTagCompound.getInteger("frame");
        this.delay = par1NBTTagCompound.getInteger("delay");
        this.max = par1NBTTagCompound.getInteger("max");
        this.count = par1NBTTagCompound.getInteger("count");
        this.setMode(Mode.values()[par1NBTTagCompound.getShort("mode")]);
        this.removed = par1NBTTagCompound.getBoolean("removed");
        
        for(int i = 0; i < count; i++)
        {
            NBTTagCompound tag = ((NBTTagCompound) par1NBTTagCompound.getTagList("frames").tagAt(i));
            List list = new ArrayList();
            Iterator itr = tag.getTags().iterator();
            while(itr.hasNext())
            	list.add(((NBTTagIntArray)itr.next()).intArray);
            this.getBaseList().add(list);
        }
    }
    
    @Override
    public Packet getDescriptionPacket()
    {
        return PacketHandler.getPacket(this);
    }
    
    @Override
    public void updateEntity()
    {
        this.hoverHeight += 3;
        this.orbRotation += 3;
        
        if(this.orbRotation > 360)
        {
            this.orbRotation -= 360;
        }
        //Side side = FMLCommonHandler.instance().getEffectiveSide();
        //FMLLog.getLogger().info((side==Side.CLIENT?"client has ":"server has ")+this.count+" frames done max is "+this.max);//DEBUG
    }
    
    public float getRotation()
    {
        return this.orbRotation;
    }
    
    public float getHoverHeight()
    {
        return this.hoverHeight;
    }

	@Override
	public String getInvName() 
	{
		return "Animator.inventory";
	}

	@Override
	public boolean isStackValidForSlot(int i, ItemStack itemstack) 
	{
		return (i == 0 && itemstack.getItem() instanceof ItemRemote);
	}
	
    public void setDelay(int i)
    {
    	this.delay += i;
    }
/**
 * 
 * @return the number of ticks (minus 2) between scheduled updates  
 * in {@link nekto.controller.block.BlockAnimator} ie, between frames
 */
    public int getDelay() 
    {
        return this.delay;
    }
    
    public void resetDelay()
    {
    	this.delay = 0;
    }
    
    public void setFrame(int i)
    {
    	while(getBaseList().size() <= i)
        	getBaseList().add(new ArrayList());
         this.frame = i;
    }

	public int getFrame() 
	{
		return this.frame;
	}
	
    public void setMode(Mode par1Mode)
    {
    	this.currMode = par1Mode;
    }
    
    public Mode getMode()
    {
    	return this.currMode;
    }
    
    public boolean isWaiting()
    {
    	return (this.max >= 0 && this.count >= this.max);
    }
    
    public boolean hasRemoved()
    {
    	return this.removed;
    }
    
    public void setRemoved(boolean rem)
    {
    	this.removed = rem;
    }
    
    public void setMaxFrame(int number)
    {
		this.max = number;
    }
    
    public int getMaxFrame()
    {
    	return this.max;
    }
    
    public int getCount()
    {
    	return this.count;
    }
    
    public void setCount(int ct)
    {
    	this.count = ct;
    }
}
