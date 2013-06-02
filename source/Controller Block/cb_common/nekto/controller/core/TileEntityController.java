package nekto.controller.core;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.src.ModLoader;
import net.minecraft.tileentity.TileEntity;

public class TileEntityController extends TileEntity {
    
    private int currIndex = 0;
    public int[][] blockList = new int[100][5];
    private boolean state = false;
    
    public void add(int blockID, int x, int y, int z, int metaData) 
    {
        ModLoader.getMinecraftInstance().thePlayer.addChatMessage("Made it here! The info passed is " + " " + blockID + " " + x + " " + y + " " + z + " " + metaData);
        
        this.blockList[this.currIndex][0] = blockID;
        this.blockList[this.currIndex][1] = x;
        this.blockList[this.currIndex][2] = y;
        this.blockList[this.currIndex][3] = z;
        this.blockList[this.currIndex][4] = metaData;
        
        this.currIndex++;
    }
    
    public void activate()
    {
        if(!state)
        {
            for(int i = currIndex; i > 0; i--)
            {
                this.worldObj.setBlockToAir(this.blockList[i][1], this.blockList[i][2], this.blockList[i][3]);
            }
        } else {
            for(int i = currIndex; i > 0; i--)
            {
                this.worldObj.setBlock(this.blockList[i][1], this.blockList[i][2], this.blockList[i][3], this.blockList[i][0]);
            }
        }
        
        state = !state;
    }
    
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        par1NBTTagCompound.setInteger("num", currIndex);
        
        for(int i = currIndex; i > 0; i--)
        {
            if(this.blockList[i] != null)
            {
                par1NBTTagCompound.setIntArray(Integer.toString(i), this.blockList[i]);
            } else {
                break;
            }
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
