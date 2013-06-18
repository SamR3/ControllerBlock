package nekto.controller.animator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class Frame {
    
    public List<int[]> blockList = null;
    private Iterator itr = blockList.iterator();
    
    public Frame()
    {
        this.blockList = new ArrayList<int[]>();
    }

    public void add(EntityPlayer player, int blockID, int x, int y, int z, int metaData) 
    {   
        boolean removed = false;
        
        int[] temp = new int[]{
            blockID,x,y,z,metaData
        };
        
        Iterator itr = blockList.iterator();
        int index = 0;
        
        while(itr.hasNext()){
            
            if(Arrays.equals((int[]) itr.next(), temp)){
                blockList.remove(index);
                removed = true;
                break;
            }
            index++;
        }
        
        if(removed) {
            player.sendChatToPlayer("Removed " + " " + blockID + " " + x + " " + y + " " + z + " " + metaData);
        } else {
            player.sendChatToPlayer("Added " + " " + blockID + " " + x + " " + y + " " + z + " " + metaData);
            blockList.add(temp);
        }
    }
    
    public void setInactiveBlocks(World world) {
        while(this.itr.hasNext())
        {
            int[] block = (int[])this.itr.next();
            
            if(block != null && block.length > 4 && world.getBlockId(block[1], block[2], block[3]) == 0)
            {
                world.setBlock(block[1], block[2], block[3], block[0], block[4], 3);
            }
        }
    }

    public void setActiveBlocks(World world) {
        while(this.itr.hasNext())
        {
            int[] block = (int[])this.itr.next();
            
            if(block != null && block.length > 4 /*&& this.world.getBlockId(block[1], block[2], block[3]) == 0*/)
            {
                world.setBlockToAir(block[1], block[2], block[3]);
            }
        }
    }
}
