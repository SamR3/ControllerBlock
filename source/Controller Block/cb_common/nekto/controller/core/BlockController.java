/*
 *  Author: Sam6982
 */
package nekto.controller.core;

import nekto.controller.ref.GeneralRef;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockController extends BlockContainer {

    private Icon textureSide;
    private Icon textureTop;
    
    public BlockController(int id)
    {
        super(id, Material.rock);
        setUnlocalizedName("controllerBlock");
        setCreativeTab(CreativeTabs.tabBlock);
    }

    @SideOnly(Side.CLIENT)
    public Icon getIcon(int par1, int par2)
    {
        return par1 <= 1 ? this.textureTop : this.textureSide;
    }
    
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.textureSide = par1IconRegister.registerIcon(GeneralRef.TEXTURE_PATH + "controller_side");
        this.textureTop = par1IconRegister.registerIcon(GeneralRef.TEXTURE_PATH + "controller_top");
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return new TileEntityController();
    }
    
    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {    
        if(par5EntityPlayer.getCurrentEquippedItem()!=null && par5EntityPlayer.getCurrentEquippedItem().getItem() instanceof ItemLinker)
        {
            return false;
        }
        
        TileEntityController tile = (TileEntityController)par1World.getBlockTileEntity(par2, par3, par4);

        if (tile != null)
        {
            par5EntityPlayer.openGui(Controller.instance, Controller.proxy.GUI_ID, par1World, par2, par3, par4);
            //tile.activate(!(tile.state));
        }

        return false;
    }
    
    @Override
    public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side)
    {
        switch(side) 
        {
            case 0:case 1:case 2:case 3: 
            	return true;
            default: 
            	return false;
  
        }
    }
    
    @Override
    public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6)
    {
        TileEntityController tile = (TileEntityController) par1World.getBlockTileEntity(par2, par3, par4);
        ItemLinker linker = tile.getLinker();
        
        if(linker != null)
        {
            linker.resetLinker();
        }
        
        /*
         * BUGFIX:
         *  Makes sure that the Linker doesn't hang on to the
         *  Controller after it's been destroyed.
         */
        
        if (this.hasTileEntity(par6))
        {
            par1World.removeBlockTileEntity(par2, par3, par4);
        }
    }
    
    @Override
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
        TileEntityController tile = (TileEntityController) par1World.getBlockTileEntity(par2, par3, par4);
        
        if(tile.isPowered)
        {
            tile.activate(true);
        } 
        else
        {
            tile.activate(false);
        }
        
        /*
         * GLITCH: 
         *  When the Controller is not powered, and the selected block is not 
         *  air, any block update near the Controller will cause it to turn all 
         *  of the linked blocks to air. I tried to fix this, but I haven't made 
         *  any headway.
         */
    }
}
