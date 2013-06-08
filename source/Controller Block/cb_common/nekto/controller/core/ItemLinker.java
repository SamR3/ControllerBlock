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
import net.minecraft.src.ModLoader;
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
            if(checkForController(par4, par5, par6, par3World))
            {
                TileEntityController tempTile = (TileEntityController) par3World.getBlockTileEntity(par4, par5, par6);
                
                if(this.link == null || this.link != tempTile)
                {
                    player.sendChatToPlayer("Linked to controller at " + tempTile.xCoord + ", " + tempTile.yCoord + ", " + tempTile.zCoord);
                    this.link = tempTile;
                    this.link.setLinker(this);
                }
                
            } else {
                if(this.link == null || !checkForController(this.link.xCoord, this.link.yCoord, this.link.zCoord, par3World))
                {
                   player.sendChatToPlayer("The Linker is not connected. Right click on a controller block to begin linking.");
                } else if (checkForController(this.link.xCoord, this.link.yCoord, this.link.zCoord, par3World) && !par3World.isAirBlock(par4, par5, par6)) {
                    if(player.capabilities.isCreativeMode)
                    {
                        this.link.add(player,par3World.getBlockId(par4, par5, par6), par4, par5, par6, par3World.getBlockMetadata(par4, par5, par6));
                    } 
                    else if (par3World.getBlockId(par4, par5, par6) != 7){//Bedrock case removed
                        this.link.add(player,par3World.getBlockId(par4, par5, par6), par4, par5, par6, par3World.getBlockMetadata(par4, par5, par6));
                    }
                }
            }
        }
        
        return false;
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        if(this.link != null)
        {
            par3List.add("Registered to Controller at " + this.link.xCoord + ", " + this.link.yCoord + ", " + this.link.zCoord);
        } else {
            par3List.add("Right click on any Controller to begin linking!");
        }
    }
    
    private boolean checkForController(int x, int y, int z, World world)
    {
    	return world.getBlockTileEntity(x, y, z) instanceof TileEntityController;
    }
    
    public void resetLinker()
    {
        this.link = null;
    }
}
