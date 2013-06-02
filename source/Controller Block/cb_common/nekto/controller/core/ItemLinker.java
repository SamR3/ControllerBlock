package nekto.controller.core;

import nekto.controller.ref.GeneralRef;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

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
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
    {
        if(!par3World.isRemote)
        {
            TileEntity tempLink = par3World.getBlockTileEntity(par4, par5, par6);
            
            if(tempLink instanceof TileEntityController)
            {
                this.link = (TileEntityController) tempLink;    
                ModLoader.getMinecraftInstance().thePlayer.addChatMessage("Linker connected to the Controller Block at " + par4 + ", " + par5 + ", " + par6);
                return true;
            } else if (this.link != null && !par3World.isAirBlock(par4, par5, par6)) {
                ModLoader.getMinecraftInstance().thePlayer.addChatMessage("Adding the block ID " + par3World.getBlockId(par4, par5, par6) +" at " + par4 + ", " + par5 + ", " + par6 + " to the Controller Block at " + this.link.xCoord + ", " + this.link.yCoord + ", " + this.link.zCoord);
                this.link.add(par3World.getBlockId(par4, par5, par6), par4, par5, par6, par3World.getBlockMetadata(par4, par5, par6));
            } else {
                ModLoader.getMinecraftInstance().thePlayer.addChatMessage("The Linker is not connected. Right click on a controller block to begin linking.");
            }
        }
        
        return false;
    }
}
