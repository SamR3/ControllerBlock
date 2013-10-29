/*
 *  Author: Sam6982
 */
package nekto.controller.item;

import java.util.List;

import nekto.controller.tile.TileEntityAnimator;
import nekto.controller.tile.TileEntityBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemRemote extends ItemBase {
	private int frame;

	public ItemRemote(int id) {
		super(id);
		setUnlocalizedName("remote");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		super.addInformation(stack, par2EntityPlayer, par3List, par4);
		if (stack.hasTagCompound() && stack.stackTagCompound.hasKey(KEYTAG)) {
			int data = stack.getTagCompound().getIntArray(KEYTAG)[3];
			par3List.add("Editing frame # " + (data + 1));
		}
	}

	@Override
	protected Class<? extends TileEntityBase> getControl() {
		return TileEntityAnimator.class;
	}

	@Override
	protected String getControlName() {
		return "tile.animator.name";
	}

	@Override
	protected int[] getStartData(int par4, int par5, int par6) {
		return new int[] { par4, par5, par6, 0 };
	}

	@Override
	protected void onBlockSelected(EntityPlayer player, World world, int id, int par4, int par5, int par6, int meta) {
		((TileEntityAnimator) this.link).setFrame(this.frame);
		super.onBlockSelected(player, world, id, par4, par5, par6, meta);
	}

	@Override
	protected boolean onControlUsed(TileEntityBase tempTile, EntityPlayer player, int par4, int par5, int par6, ItemStack stack) {
		if (tempTile.getLinker() == null) {
			if (this.link != tempTile) {
				this.link.setEditing(false);
				this.link.setLinker(null);
				this.link = tempTile;
				player.sendChatToPlayer(ChatMessageComponent.createFromText(MESSAGE_2));
			}
			tempTile.setLinker(this);
			this.frame = ((TileEntityAnimator) tempTile).getFrame();
			player.sendChatToPlayer(ChatMessageComponent.createFromText(MESSAGE_1 + par4 + ", " + par5 + ", " + par6));
			setEditAndTag(new int[] { par4, par5, par6, this.frame }, stack);
			return true;
		} else if (tempTile.getLinker() == this) {
			player.sendChatToPlayer(ChatMessageComponent.createFromText("Finished frame # " + (this.frame + 1) + " Continuing with frame # " + (this.frame + 2)));
			this.frame++;
			setEditAndTag(new int[] { par4, par5, par6, this.frame }, stack);
			return true;
		}
		return false;
	}

	@Override
	protected void setEditAndTag(int[] pos, ItemStack par1ItemStack) {
		((TileEntityAnimator) this.link).setFrame(pos[3]);
		super.setEditAndTag(pos, par1ItemStack);
	}

	@Override
	protected void setItemVar(World par3World, int... data) {
		super.setItemVar(par3World, data);
		this.frame = data[3];
	}
}
