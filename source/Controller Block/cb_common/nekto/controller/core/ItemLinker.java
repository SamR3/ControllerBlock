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

public class ItemLinker extends Item {

    public TileEntityController link = null;
    
    public ItemLinker(int id)
    {
        super(id);
        setMaxStackSize(1);
        setCreativeTab(CreativeTabs.tabBlock);
        setUnlocalizedName("controllerLinker");
    }

    @Override
    public void registerIcons(IconRegister par1IconRegister) 
    {
        this.itemIcon = par1IconRegister.registerIcon(GeneralRef.TEXTURE_PATH + this.getUnlocalizedName().substring(5));
    }
    
    @Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer player, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
    {
        if(!par3World.isRemote)
        {
            if(isController(par4, par5, par6, par3World))
            {
                TileEntityController tempTile = (TileEntityController) par3World.getBlockTileEntity(par4, par5, par6);
                if(par1ItemStack.hasTagCompound() && par1ItemStack.stackTagCompound.hasKey("ControlPos"))
            	{
                    player.sendChatToPlayer("Unlinked from Controller.");              
                    this.link.setLinker(null);//The controller is freed from the reference to the linker.
                    this.resetLinker();//The linker is freed from the reference to the controller.                
                    par1ItemStack.getTagCompound().removeTag("ControlPos");//Removed the data saved
                }
                else if(tempTile.getLinker() == null)
                {
                    player.sendChatToPlayer("Linked to Controller at " + par4 + ", " + par5 + ", " + par6);
                    this.link = tempTile;
                    this.link.setLinker(this);
                	NBTTagCompound tag = new NBTTagCompound();
                	tag.setIntArray("ControlPos", new int[]{par4, par5, par6});
                	par1ItemStack.setTagCompound(tag);                 	
                }
                else if(this !=tempTile.getLinker())
                {
                	player.sendChatToPlayer("Controller is already linked to another Linker.");
                	//Another player might be editing, let's avoid any issue and do nothing.
                }
            } else {
                if ( par1ItemStack.hasTagCompound() && par1ItemStack.stackTagCompound.hasKey("ControlPos")) 
                {
                	if(link == null)
                	{
                		int[] pos = par1ItemStack.getTagCompound().getIntArray("ControlPos");
                		this.link= (TileEntityController) par3World.getBlockTileEntity(pos[0], pos[1], pos[2]);
                	}
                	if(!par3World.isAirBlock(par4, par5, par6))
	                    if(player.capabilities.isCreativeMode)
	                    {
	                        this.link.add(player,par3World.getBlockId(par4, par5, par6), par4, par5, par6, par3World.getBlockMetadata(par4, par5, par6));
	                    } 
	                    else if (par3World.getBlockId(par4, par5, par6) != 7)
	                    {//Bedrock case removed
	                        this.link.add(player,par3World.getBlockId(par4, par5, par6), par4, par5, par6, par3World.getBlockMetadata(par4, par5, par6));                
	                    }
                }
                else
                	player.sendChatToPlayer("The Linker is not connected. Right click on a controller block to begin linking.");
            }
        }
        return false;
    }
    
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
    	if(stack.hasTagCompound() && stack.stackTagCompound.hasKey("ControlPos"))
    	{
	        int[] pos = stack.getTagCompound().getIntArray("ControlPos");
	        par3List.add("Registered to Controller at " + pos[0]+ ", " + pos[1] + ", " + pos[2]);
        } 
    	else 
    	{
            par3List.add("Right click on any Controller to begin linking!");
        }
    }
    
    private static boolean isController(int x, int y, int z, World world)
    {
    	return world.getBlockTileEntity(x, y, z) instanceof TileEntityController;
    }
    
    public void resetLinker()
    {
        this.link = null;
    }
}
