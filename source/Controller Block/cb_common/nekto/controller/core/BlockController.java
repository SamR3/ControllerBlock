/*
 *  Author: Sam6982
 */
package nekto.controller.core;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import nekto.controller.ref.GeneralRef;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityCommandBlock;
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
        }

        return false;
    }
    
    @Override
    public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side)
    {
        return (side>=0 && side<=3);
    }
    
    @Override
    public void breakBlock(World world, int par2, int par3, int par4, int par5, int par6)
    {
        TileEntityController tile = (TileEntityController) world.getBlockTileEntity(par2, par3, par4);
        Iterator itr = tile.blockList.iterator();
        if(!world.isRemote && !tile.previousState)
	        while(itr.hasNext())
	        {
	    		int[] elem = (int[]) itr.next();
	    		EntityItem item = new EntityItem(world, par2, par3, par4, new ItemStack(elem[0],1,elem[4]));
	    		world.spawnEntityInWorld(item);
	        }
        ItemLinker linker = tile.getLinker();
        
        if(linker != null)
        {
            linker.resetLinker();
        }
        
        super.breakBlock(world, par2, par3, par4, par5, par6);
    }
    
    @Override
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
    	TileEntityController tile = (TileEntityController) par1World.getBlockTileEntity(par2, par3, par4);
    	boolean flag =par1World.isBlockIndirectlyGettingPowered(par2, par3, par4);
        if( tile.previousState!=flag)
        {
        	Iterator itr = tile.blockList.iterator();
        
        	if(flag)
        	{
        		setActiveBlocks(par1World,itr);
        	}
            else
            {
            	setUnactiveBlocks(par1World,itr);
            }
        	tile.setState(flag);
        } 
    }

	private void setUnactiveBlocks(World par1World, Iterator itr) {
        while(itr.hasNext())
        {
        	int[] block = (int[])itr.next();
        	if(block != null && block.length > 4 && par1World.getBlockId(block[1], block[2], block[3]) == 0)
        	{
        		par1World.setBlock(block[1], block[2], block[3], block[0], block[4], 3);
        	}
        }
	}

	private void setActiveBlocks(World par1World, Iterator itr) {
        while(itr.hasNext())
        {
        	int[] block = (int[])itr.next();
        	if(block != null && block.length > 4 /*&& par1World.getBlockId(block[1], block[2], block[3]) == 0*/)
        	{
        		par1World.setBlockToAir(block[1], block[2], block[3]);
        	}
        }
	}
}
