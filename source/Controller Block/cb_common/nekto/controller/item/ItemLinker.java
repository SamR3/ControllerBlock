/*
 *  Author: Sam6982
 */
package nekto.controller.item;

import nekto.controller.tile.TileEntityBase;
import nekto.controller.tile.TileEntityController;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ItemLinker extends ItemBase {

    public ItemLinker(int id)
    {
        super(id);
        setUnlocalizedName("linker");
    }
    
    @Override
    protected boolean onControlUsed(TileEntityBase tempTile, EntityPlayer player, int par4, int par5, int par6, ItemStack stack)
    {
    	if(tempTile.getLinker() == null)
        {
    		if(this.link!=tempTile)
    		{
	    		this.link.setEditing(false);
	    		this.link.setLinker(null);
	    		this.link = tempTile;
	    		player.sendChatToPlayer(MESSAGE_2);
    		}
        	tempTile.setLinker(this);
        	player.sendChatToPlayer(MESSAGE_1 + par4 + ", " + par5 + ", " + par6);
        	setEditAndTag(new int[]{par4, par5, par6},stack);
        	return true;
        }
        else if(tempTile.getLinker() == this)
        {
        	tempTile.setLinker(null);
        	tempTile.setEditing(false);
        	stack.getTagCompound().removeTag(KEYTAG);
        	this.resetLinker();
        	player.sendChatToPlayer(MESSAGE_2);
        	return true;
        }
    	return false;
    }
    
	@Override
    protected Class<? extends TileEntityBase> getControl()
    {
    	return TileEntityController.class;
    }

	@Override
	protected String getControlName() 
	{
		return "Controller";
	}
	
	@Override
	public String toString()
	{
		return "Linker";
	}
}
