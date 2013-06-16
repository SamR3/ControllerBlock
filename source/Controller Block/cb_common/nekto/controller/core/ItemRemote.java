/*
 *  Author: Sam6982
 */
package nekto.controller.core;

import java.util.List;

import nekto.controller.ref.GeneralRef;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemRemote extends ItemBase {

    private int frame;
    
    public ItemRemote(int id)
    {
        super(id);
        setUnlocalizedName("remote");
    }
    
    @Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer player, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
    {
        if(!par3World.isRemote)
        {
        	TileEntityAnimator tempTile = null;
        	if(isController(par4, par5, par6, par3World))
        	{   
            	tempTile = (TileEntityAnimator) par3World.getBlockTileEntity(par4, par5, par6);
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
                		this.link = ((TileEntityAnimator)par3World.getBlockTileEntity(pos[0], pos[1], pos[2]));
                		this.frame = pos[3];
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
                    }
                    if(this != tempTile.getLinker())
                    {
                    	player.sendChatToPlayer(MESSAGE_3);
                    	//Another player might be editing, let's avoid any issue and do nothing.
                    	return false;
                    }
                    
                    player.sendChatToPlayer("Finished frame # "+ this.frame +" Continuing with frame # "+ (this.frame + 1));
                    this.frame++;
                    setEditAndTag( new int[]{par4, par5, par6, this.frame},par1ItemStack);
                }
                else if(!par3World.isAirBlock(par4, par5, par6))
                {
                	this.link.setEditing(true);
                	((TileEntityAnimator) this.link).setFrame(this.frame);
                    if(player.capabilities.isCreativeMode)
                    {
                        this.link.add(player, this.frame, par3World.getBlockId(par4, par5, par6), par4, par5, par6, par3World.getBlockMetadata(par4, par5, par6));
                    } 
                    else if (par3World.getBlockId(par4, par5, par6) != 7)
                    {//Bedrock case removed
                        this.link.add(player, this.frame , par3World.getBlockId(par4, par5, par6), par4, par5, par6, par3World.getBlockMetadata(par4, par5, par6));                
                    }
                } 
            }   
            else if(isController(par4, par5, par6, par3World) && ((TileEntityAnimator) par3World.getBlockTileEntity(par4, par5, par6)).getLinker() == null)           
            {
            	player.sendChatToPlayer(MESSAGE_1 + par4 + ", " + par5 + ", " + par6);
        		this.link = (TileEntityAnimator) par3World.getBlockTileEntity(par4, par5, par6);
        		this.link.setLinker(this);
            	setEditAndTag( new int[]{par4, par5, par6, 0},par1ItemStack);
            }
            else
            	player.sendChatToPlayer(MESSAGE_0);
        }
        return false;
    }
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
    	super.addInformation(stack, par2EntityPlayer, par3List, par4);
    	if(stack.hasTagCompound() && stack.stackTagCompound.hasKey(KEYTAG))
    	{
	        int data = stack.getTagCompound().getIntArray(KEYTAG)[3];
	        par3List.add("Editing frame # " + data);
        }
    }
    @Override
    protected boolean isController(int x, int y, int z, World world)
    {
    	return world.getBlockTileEntity(x, y, z) instanceof TileEntityAnimator;
    }
    @Override
    protected void setEditAndTag(int[] pos, ItemStack par1ItemStack) 
	{
    	((TileEntityAnimator) this.link).setFrame(pos[3]);
    	super.setEditAndTag(pos, par1ItemStack);
	}
}
