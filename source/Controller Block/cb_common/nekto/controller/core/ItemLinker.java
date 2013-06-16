/*
 *  Author: Sam6982
 */
package nekto.controller.core;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemLinker extends ItemBase {

    public ItemLinker(int id)
    {
        super(id);
        setUnlocalizedName("controllerLinker");
    }
    
    @Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer player, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
    {
    	if(!par3World.isRemote)
        {
        	TileEntityController tempTile = null;
        	if(isController(par4, par5, par6, par3World))
        	{   
            	tempTile = (TileEntityController) par3World.getBlockTileEntity(par4, par5, par6);
        	}
            if(par1ItemStack.hasTagCompound() && par1ItemStack.stackTagCompound.hasKey(KEYTAG))
            {
            	int[] pos = null;
            	if(this.link == null)
                {
                	pos = par1ItemStack.getTagCompound().getIntArray(KEYTAG);
                	//Try to find the old controller block to set its linker
                	if(isController(pos[0], pos[1], pos[2], par3World))
                	{
                		this.link = ((TileEntityController)par3World.getBlockTileEntity(pos[0], pos[1], pos[2]));
                	}
                	else//It had data on a block that doesn't exist anymore
            		{
            			par1ItemStack.getTagCompound().removeTag(KEYTAG);
            			player.sendChatToPlayer(MESSAGE_2);
            			return false;
            		}
                }
                if(tempTile != null)
            	{   
                    if(tempTile.getLinker() == null && this.link == tempTile)
                    {
                    	tempTile.setLinker(this);
                    	player.sendChatToPlayer(MESSAGE_1 + par4 + ", " + par5 + ", " + par6);
                    }
                    else if(tempTile.getLinker() == this)
                    {
                    	tempTile.setLinker(null);
                    	tempTile.setEditing(false);
                    	par1ItemStack.getTagCompound().removeTag(KEYTAG);
                    	this.resetLinker();
                    	player.sendChatToPlayer(MESSAGE_2);
            			return false;
                    }
                    else
                    {
                    	player.sendChatToPlayer(MESSAGE_3);
                    	//Another player might be editing, let's avoid any issue and do nothing.
                    	return false;
                    }
                    setEditAndTag(new int[]{par4, par5, par6},par1ItemStack);
                    
                }
                else if(!par3World.isAirBlock(par4, par5, par6))
                {
                	this.link.setEditing(true);
                    if(player.capabilities.isCreativeMode)
                    {
                        this.link.add(player, par3World.getBlockId(par4, par5, par6), par4, par5, par6, par3World.getBlockMetadata(par4, par5, par6));
                    } 
                    else if (par3World.getBlockId(par4, par5, par6) != 7)
                    {//Bedrock case removed
                        this.link.add(player, par3World.getBlockId(par4, par5, par6), par4, par5, par6, par3World.getBlockMetadata(par4, par5, par6));                
                    }
                } 
            }   
            else if(isController(par4, par5, par6, par3World) && ((TileEntityController) par3World.getBlockTileEntity(par4, par5, par6)).getLinker() == null)           
            {
            	player.sendChatToPlayer(MESSAGE_1 + par4 + ", " + par5 + ", " + par6);
        		this.link = (TileEntityController) par3World.getBlockTileEntity(par4, par5, par6);
        		this.link.setLinker(this);
        		setEditAndTag(new int[]{par4, par5, par6},par1ItemStack);
            }
            else
            	player.sendChatToPlayer(MESSAGE_0);
        }
        return false;
    }

	@Override
    protected boolean isController(int x, int y, int z, World world)
    {
    	return world.getBlockTileEntity(x, y, z) instanceof TileEntityController;
    }
}
