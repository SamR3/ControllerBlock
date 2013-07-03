package nekto.controller.network;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import nekto.controller.animator.Mode;
import nekto.controller.container.ContainerAnimator;
import nekto.controller.item.ItemBase;
import nekto.controller.ref.GeneralRef;
import nekto.controller.tile.TileEntityAnimator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class PacketHandler implements IPacketHandler{

	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) 
	{
		if(packet.channel == GeneralRef.PACKET_CHANNEL)
		{
			this.handle(packet,(EntityPlayer) player);
		}
	}

	private void handle(Packet250CustomPayload packet, EntityPlayer player) 
	{
		DataInputStream inStream = new DataInputStream(new ByteArrayInputStream(packet.data));
		int[] data = new int[packet.data.length/4];
		try 
		{
			for(int id = 0; id < data.length; id++)
				data[id] = inStream.readInt();
		} 
		catch (IOException e) 
		{
            e.printStackTrace();
            return;
		}
		if(player.openContainer instanceof ContainerAnimator)
		{
			TileEntityAnimator animator = (TileEntityAnimator) ((ContainerAnimator)player.openContainer).getControl();
			
			switch(data[0])
			{
			case 0://"+" button has been pressed
				animator.setDelay(0.01F);
				break;
			case 1://"-" button has been pressed
				if(animator.getDelay()>-1)
				{//Lower delay won't work and might crash
					animator.setDelay(-0.01F);
				}
				break;
			case 2://"Switch button has been pressed, going LOOP->ORDER->REVERSE->RANDOM->LOOP
				int mod = animator.getMode().ordinal();
				if(mod + 1 < Mode.values().length)
					animator.setMode(Mode.values()[mod + 1]);
				else
					animator.setMode(Mode.LOOP);
                break;
            case 3:
            case 4://One of the "Reset" button has been pressed
            	if(data.length > 1)//We got data from the item in slot
            	{
	                TileEntity ent = player.worldObj.getBlockTileEntity(data[1], data[2], data[3]);
	                if(ent != null && ent instanceof TileEntityAnimator)
	                {//We change the animator to the one pointed by the item
	                    animator = (TileEntityAnimator) ent;
	                    animator.setEditing(false);
	                    animator.setLinker(null);
	                    if(data[0]==5)//This is a full reset
	                    {
	                    	resetAnimator(animator);
	                    }
	                    //Get the item and reset it
	                    ItemStack stack = player.openContainer.getSlot(0).getStack();
	                    ItemBase remote = (ItemBase)stack.getItem();
	                    remote.resetLinker();
	                    stack.getTagCompound().removeTag(ItemBase.KEYTAG);
	                }
	                break;
            	}
            	else//No item, so it falls back to full reset
            	{
            		animator.setEditing(false);
                    animator.setLinker(null);
                    resetAnimator(animator);
            	}
            	break;
            case 5://Max setter
            	animator.setMaxFrame(data[1]);
				break;
            case 6:
            	animator.setFrame(animator.getFrame() + 1);
            	break;
			}
			player.openContainer.detectAndSendChanges();
		}
	}

	private void resetAnimator(TileEntityAnimator animator) 
	{
		animator.setFrame(0);                
        animator.setMode(Mode.ORDER);
        animator.resetDelay();
        animator.setMaxFrame(-1);
        animator.resetCount();
	}
}
