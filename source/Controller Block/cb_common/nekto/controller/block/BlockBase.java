package nekto.controller.block;

import java.util.Iterator;

import nekto.controller.item.ItemBase;
import nekto.controller.item.ItemLinker;
import nekto.controller.ref.GeneralRef;
import nekto.controller.tile.TileEntityBase;
import nekto.controller.tile.TileEntityController;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class BlockBase extends BlockContainer{

	protected Icon textureSide;
	protected Icon textureTop;
	protected BlockBase(int par1) 
	{
		super(par1, Material.rock);
		setCreativeTab(CreativeTabs.tabBlock);
    }

    @SideOnly(Side.CLIENT)
    public Icon getIcon(int par1, int par2)
    {
        return par1 <= 1 ? this.textureTop : this.textureSide;
    }
    
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)//Both controller and animator have temporarily same textures
    {
        this.textureSide = par1IconRegister.registerIcon(GeneralRef.TEXTURE_PATH + "controller_side");
        this.textureTop = par1IconRegister.registerIcon(GeneralRef.TEXTURE_PATH + "controller_top");
    }
    
    @Override
    public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side)
    {
        return (side>=0 && side<=3);
    }
    
    @Override
    public void breakBlock(World world, int par2, int par3, int par4, int par5, int par6)
    {
    	TileEntityBase tile = (TileEntityBase) world.getBlockTileEntity(par2, par3, par4);
        if(!world.isRemote && tile.isPowered() && !tile.isEditing())//We only spawn items if it is powered and not in editing mode
        {
        	Iterator itr = tile.getBaseList().iterator();
        	dropItems(world, tile, itr, par2, par3, par4);
        }
    	ItemBase linker = tile.getLinker();
    	if(linker != null)
        {
            linker.resetLinker();
        }
    	super.breakBlock(world, par2, par3, par4, par5, par6);
    }

	protected void setUnactiveBlocks(World par1World, Iterator itr) 
	{
        while(itr.hasNext())
        {
        	int[] block = (int[])itr.next();
        	if(block != null && block.length > 4 && par1World.getBlockId(block[1], block[2], block[3]) != block[0])
        	{
        		par1World.setBlock(block[1], block[2], block[3], block[0], block[4], 3);
        	}
        }
	}

	protected void setActiveBlocks(World par1World, Iterator itr) 
	{
        while(itr.hasNext())
        {
        	int[] block = (int[])itr.next();
        	if(block != null && block.length > 4)
        	{
        		if(par1World.getBlockId(block[1], block[2], block[3]) != block[0])
        			itr.remove();
        		else
        			par1World.setBlockToAir(block[1], block[2], block[3]);
        	}
        }
	}
	
	protected void dropItems(World world, TileEntityBase tile, Iterator itr, int par2, int par3, int par4) 
	{
		float f = world.rand.nextFloat() * 0.8F + 0.1F;
        float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
        float f2 = world.rand.nextFloat() * 0.8F + 0.1F;
        
		while(itr.hasNext())
		{
			int[] elem = (int[]) itr.next();
			EntityItem item = new EntityItem(world, (double)((float)par2 + f), (double)((float)par3 + f1), (double)((float)par4 + f2), new ItemStack(elem[0],1,elem[4]));
			world.spawnEntityInWorld(item);
		}
	}
	
	@Override
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
		TileEntityBase tile = (TileEntityBase) par1World.getBlockTileEntity(par2, par3, par4);
    	boolean flag =par1World.isBlockIndirectlyGettingPowered(par2, par3, par4);
        if( tile.previousState!=flag)
        { 	
        	if(tile.getBaseList().size()>0)
        		onRedstoneChange(par1World, par2, par3, par4, par5, flag, tile);   	
        	tile.setState(flag);
        }
    }
	protected abstract void onRedstoneChange(World par1World, int par2, int par3,
			int par4, int par5, boolean powered, TileEntityBase tile);
}
