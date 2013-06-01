package nekto.controller.core;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityController extends TileEntity {
    
    private int currIndex = 0;
    public int[][] blockList;
    
    public void add(int blockID, int x, int y, int z, int metaData) 
    {
        this.blockList[this.currIndex][0] = blockID;
        this.blockList[this.currIndex][1] = x;
        this.blockList[this.currIndex][2] = y;
        this.blockList[this.currIndex][3] = z;
        this.blockList[this.currIndex][4] = metaData;
        
        this.currIndex++;
    }
    
    public void activate()
    {
        for(int i = currIndex; i > 0; i--)
        {
            this.worldObj.setBlockToAir(this.blockList[i][1], this.blockList[i][2], this.blockList[i][3]);
        }
    }
    
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        par1NBTTagCompound.setInteger("num", currIndex);
        
        for(int i = currIndex; i > 0; i--)
        {
            par1NBTTagCompound.setIntArray(Integer.toString(i), blockList[i]);
        }
    }
    
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        this.currIndex = par1NBTTagCompound.getInteger("num");
        
        for(int i = currIndex; i > 0; i--)
        {
            this.blockList[i] = par1NBTTagCompound.getIntArray(Integer.toString(i));
        }
    }
}
