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
			for(int id=0;id<data.length;id++)
				data[id] = inStream.readInt();
		} 
		catch (IOException e) 
		{
            e.printStackTrace();
            return;
		}
		if(player.openContainer instanceof ContainerAnimator)
		{
			TileEntityAnimator animator= (TileEntityAnimator) ((ContainerAnimator)player.openContainer).getControl();
			
			switch(data[0])
			{
			case 1://"+" button has been pressed
				animator.setDelay(0.01F);
				return;
			case 2://"-" button has been pressed
				animator.setDelay(-0.01F);
				return;
			case 3://"Reset" button has been pressed
				TileEntity ent = player.worldObj.getBlockTileEntity(data[1], data[2], data[3]);
	    		if(ent!=null && ent instanceof TileEntityAnimator)
	    		{
	    			animator = (TileEntityAnimator) ent;
	    			animator.setEditing(false);
	    			animator.setFrame(0);
	    			animator.setMode(Mode.ORDER);
	    			animator.resetDelay();
	    			animator.setLinker(null);
	    			ItemStack stack = player.openContainer.getSlot(0).getStack();
	    			ItemBase remote = (ItemBase)stack.getItem();
	    			remote.resetLinker();
	    			stack.getTagCompound().removeTag(ItemBase.KEYTAG);
	    		}
	    		return;
			}
		}
	}

}
