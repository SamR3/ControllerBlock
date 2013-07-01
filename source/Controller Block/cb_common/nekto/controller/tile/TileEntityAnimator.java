package nekto.controller.tile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import nekto.controller.animator.Mode;
import nekto.controller.item.ItemRemote;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;

public class TileEntityAnimator extends TileEntityBase<List<int[]>> {
    
    private int frame = 0, delay = 0;
    private Mode currMode = Mode.ORDER;
    private float orbRotation = 0;
    private float hoverHeight = 0;
	
    public TileEntityAnimator()
    {
    	super(1);
    }
    
    @Override
	public void add(EntityPlayer player, int blockID, int x, int y, int z, int metaData) 
    {
        int[] temp = new int[]{blockID,x,y,z,metaData};
        
        while(getBaseList().size() <= frame)
        	getBaseList().add(new ArrayList());
        
        Iterator itr = ((List) getBaseList().get(frame)).listIterator();   
        
        boolean removed = removeFromList(itr, temp);
        sendMessage(player,removed,blockID,x,y,z,metaData);
        if(!removed)
        	getBaseList().get(frame).add(temp);
    }
	

	@Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
    	super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setInteger("frame", this.frame);
        par1NBTTagCompound.setInteger("delay", this.delay);
        par1NBTTagCompound.setShort("mode", (short) this.getMode().ordinal());
        
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
        this.setFrame(par1NBTTagCompound.getInteger("frame"));
        this.delay = par1NBTTagCompound.getInteger("delay");
        this.setMode(Mode.values()[par1NBTTagCompound.getShort("mode")]);
        
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
    public void updateEntity()
    {
        this.hoverHeight += 3;
        this.orbRotation += 3;
        
        if(this.orbRotation > 360)
        {
            this.orbRotation -= 360;
        }
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
	   
    public void setDelay(float f)
    {
    	this.delay += (int)(f * 1000);
    }

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
}
