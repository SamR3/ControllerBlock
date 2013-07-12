package nekto.controller.item;

import java.util.List;

import nekto.controller.ref.GeneralRef;
import nekto.controller.tile.TileEntityBase;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class ItemBase extends Item{

	public TileEntityBase link = null;
    public final static String KEYTAG = "Control";
    public String MESSAGE_0="The "+toString()+" is not connected.";
    public String MESSAGE_1="Linked to "+getControlName()+" at ";
    public String MESSAGE_2="Unlinked from "+getControlName()+".";
	public String MESSAGE_3=getControlName()+" is already linked to another "+toString()+".";
	public String MESSAGE_4="Right click on a "+getControlName()+" block to begin linking.";
	
	public ItemBase(int id)
    {
        super(id);
        setMaxStackSize(1);
        setCreativeTab(CreativeTabs.tabBlock);
    }
	
	@Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer player, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
    {
        if(!par3World.isRemote)
        {
        	TileEntityBase tempTile = null;
        	if(isController(par4, par5, par6, par3World))
        	{   
            	tempTile = (TileEntityBase) par3World.getBlockTileEntity(par4, par5, par6);
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
                		setItemVar(par3World, pos);
                	}
                	else//It had data on a block that doesn't exist anymore
            		{
            			par1ItemStack.getTagCompound().removeTag(KEYTAG);
            			player.sendChatToPlayer(MESSAGE_2);//see ChatMessageComponent static string method
            			return false;
            		}
                }
                if(tempTile != null)
            	{   
                    if(!onControlUsed(tempTile, player, par4, par5, par6, par1ItemStack))
                    	player.sendChatToPlayer(MESSAGE_3);
                	//Another player might be editing, let's avoid any issue and do nothing.
                }
                else if(!par3World.isAirBlock(par4, par5, par6))
                {
                	onBlockSelected(player, par3World.getBlockId(par4, par5, par6), par4, par5, par6, par3World.getBlockMetadata(par4, par5, par6));   
                } 
            }   
            else if(isController(par4, par5, par6, par3World) && ((TileEntityBase) par3World.getBlockTileEntity(par4, par5, par6)).getLinker() == null)           
            {
            	player.sendChatToPlayer(MESSAGE_1 + par4 + ", " + par5 + ", " + par6);
        		this.link = (TileEntityBase) par3World.getBlockTileEntity(par4, par5, par6);
        		this.link.setLinker(this);
            	setEditAndTag(getStartData(par4, par5, par6), par1ItemStack);
            }
            else
            	player.sendChatToPlayer(MESSAGE_0+MESSAGE_4);
        }
        return false;
    }
	
	protected int[] getStartData(int par4, int par5, int par6) 
	{
		return new int[]{par4, par5, par6};
	}
/**
 * Fired if player selected a block which isn't a valid control
 * @param player
 * @param id blockID from {@link World#getBlockId(int, int, int)}
 * @param meta block metadata {@link World#getBlockMetadata(int, int, int)}
 */
	protected void onBlockSelected(EntityPlayer player, int id, int par4, int par5, int par6, int meta) 
	{
    	this.link.setEditing(true);
    	if(player.capabilities.isCreativeMode)
        {
            this.link.add(player, id, par4, par5, par6, meta);
        } 
        else if (id != 7)
        {//Bedrock case removed
            this.link.add(player, id, par4, par5, par6, meta);             
        }
	}
/**
 * Fired if link is null but item has NBTTag data pointing to a valid control
 * @param world
 * @param data from the item {@link NBTTagCompound}
 */
	protected void setItemVar(World world, int...data) 
	{
		this.link = (TileEntityBase)world.getBlockTileEntity(data[0], data[1], data[2]);
	}

	@Override
    public void registerIcons(IconRegister par1IconRegister) 
    {
        this.itemIcon = par1IconRegister.registerIcon(GeneralRef.TEXTURE_PATH + this.getUnlocalizedName().substring(5));
    }
	
	public void resetLinker()
    {
        this.link = null;
    }
	
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
    	if(stack.hasTagCompound() && stack.stackTagCompound.hasKey(KEYTAG))
    	{
	        int[] pos = stack.getTagCompound().getIntArray(KEYTAG);
	        par3List.add(MESSAGE_1 + pos[0]+ ", " + pos[1] + ", " + pos[2]);
        } 
    	else 
    	{
            par3List.add(MESSAGE_4);
        }
    }
	/**
	 * Helper function to set data into the item and its Control in editing mode
	 * @param pos Data to set into the item {@link NBTTagCompound}
	 * @param par1ItemStack The item that will get the data
	 */
	protected void setEditAndTag(int[] pos, ItemStack par1ItemStack) 
	{
    	this.link.setEditing(true);
        NBTTagCompound tag = new NBTTagCompound();
    	tag.setIntArray(KEYTAG, pos);
    	par1ItemStack.setTagCompound(tag);
	}
	/**
	 * Check for a controller on the coordinates
	 * @return True only if there is a tile entity which extends from {@link #getControl()}
	 */
	private boolean isController(int x, int y, int z, World world)
	{
    	return getControl().isInstance(world.getBlockTileEntity(x, y, z));
    }
	
	protected abstract boolean onControlUsed(TileEntityBase tempTile, EntityPlayer player, int par4, int par5, int par6, ItemStack stack);
	protected abstract Class<? extends TileEntityBase> getControl();
	protected abstract String getControlName();
}
