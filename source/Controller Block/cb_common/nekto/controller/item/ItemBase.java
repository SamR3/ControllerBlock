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
    public String MESSAGE_0="The "+toString()+" is not connected. Right click on a "+getControlName()+" block to begin linking.";
    public String MESSAGE_1="Linked to "+getControlName()+" at ";
    public String MESSAGE_2="Unlinked from "+getControlName()+".";
	public String MESSAGE_3=getControlName()+" is already linked to another "+toString()+".";
	
	public ItemBase(int id)
    {
        super(id);
        setMaxStackSize(1);
        setCreativeTab(CreativeTabs.tabBlock);
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
	        par3List.add("Registered to "+getControlName()+" at " + pos[0]+ ", " + pos[1] + ", " + pos[2]);
        } 
    	else 
    	{
            par3List.add("Right click on any "+getControlName()+" to begin linking!");
        }
    }
	
	protected void setEditAndTag(int[] pos, ItemStack par1ItemStack) 
	{
    	this.link.setEditing(true);
        NBTTagCompound tag = new NBTTagCompound();
    	tag.setIntArray(KEYTAG, pos);
    	par1ItemStack.setTagCompound(tag);
	}
	
	protected abstract boolean isController(int x, int y, int z, World world);
	protected abstract String getControlName();
}
