package nekto.controller.network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import nekto.controller.animator.Mode;
import nekto.controller.container.ContainerAnimator;
import nekto.controller.core.Controller;
import nekto.controller.item.ItemBase;
import nekto.controller.ref.GeneralRef;
import nekto.controller.tile.TileEntityAnimator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class PacketHandler implements IPacketHandler{

	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) 
	{
		//DEBUG:
		//Side side = ((EntityPlayer)player).worldObj.isRemote?Side.CLIENT:Side.SERVER;
		//FMLLog.getLogger().info(side.toString()+" received a "+packet.channel +" packet");
		if(packet.channel.equals(GeneralRef.PACKET_CHANNELS[0]))
		{//Sent by AnimatorGUI to server when a button has been activated
			//or by RemoteKeyHandler when player is holding a remote with a link
			handleGuiChange(packet,(EntityPlayer) player);
		}
		else if(packet.channel.equals(GeneralRef.PACKET_CHANNELS[1]))
		{//Sent by server to client
			handleDescriptionPacket(packet,(EntityPlayer) player);
		}
		else{
			//FMLLog.getLogger().warning("Couldn't handle packet");
		}
		
	}
/**
 * Client method to handle a packet describing the TileEntityAnimator from server
 * @param packet
 * @param player
 */
	private static void handleDescriptionPacket(Packet250CustomPayload packet, EntityPlayer player)
	{
		DataInputStream dat = new DataInputStream(new ByteArrayInputStream(packet.data));
		try{
	        int x = dat.readInt();
	        int y = dat.readInt();
	        int z = dat.readInt();
	        TileEntity te = player.worldObj.getBlockTileEntity(x, y, z);
	        if (te instanceof TileEntityAnimator)
	        {
	            TileEntityAnimator animator = (TileEntityAnimator) te;
	            animator.setEditing(dat.readBoolean());
	            if(!animator.isEditing() && animator.getStackInSlot(0)!=null)
	                resetRemote(animator.getStackInSlot(0));
	            animator.setFrame(dat.readInt());
	            animator.setMaxFrame(dat.readInt());
	            animator.setCount(dat.readInt());
	            animator.resetDelay();
	            animator.setDelay(dat.readInt());
	            animator.setMode(Mode.values()[dat.readShort()]);
	        }
		}catch(IOException i)
		{
			i.printStackTrace();
		}
	}
/**
 * Server method to handle a client action in AnimatorGUI or RemoteKeyHandler
 * @param packet
 * @param player
 */
	private static void handleGuiChange(Packet250CustomPayload packet, EntityPlayer player) 
	{
		DataInputStream inStream = new DataInputStream(new ByteArrayInputStream(packet.data));
		int[] data = new int[(packet.data.length-1)/4];
		boolean remote;
		try 
		{
			remote = inStream.readBoolean();
			for(int id = 0; id < data.length; id++)
				data[id] = inStream.readInt();
		} 
		catch (IOException e) 
		{
            e.printStackTrace();
            return;
		}
		TileEntity tile = player.worldObj.getBlockTileEntity(data[1], data[2], data[3]);
		if(tile instanceof TileEntityAnimator)
		{
			if(data[0]>=0)//From AnimatorGUI
			{
				if(!remote)
				{
					handleBlockData(player, (TileEntityAnimator) tile, data);
				}
				else
				{
					handleRemoteData(player, (TileEntityAnimator) tile, data);
				}
	            if(player.openContainer instanceof ContainerAnimator && ((ContainerAnimator)player.openContainer).getControl() == tile)
	    			player.openContainer.detectAndSendChanges();
				player.worldObj.markBlockForUpdate(data[1], data[2], data[3]);
			}
			else//From RemoteKeyHandler
			{
				player.openGui(Controller.instance, GeneralRef.REMOTE_GUI_ID, player.worldObj,data[1],data[2],data[3]);
			}
		}
	}

	private static void handleRemoteData(EntityPlayer player, TileEntityAnimator animator, int... data)
	{
		switch(data[0])
		{
		case 0://Frame selection
			animator.setFrame(animator.getFrame() + 1);
			break;
		case 1://Multiple block selection switch
			ItemStack stack = player.getCurrentEquippedItem();
			if(stack!=null && stack.getItem() instanceof ItemBase)
			{
				ItemBase item = (ItemBase)stack.getItem();
				item.setCornerMode(!item.isInCornerMode());
			}
			break;
		case 2://Reset button
			animator.setEditing(false);
            animator.setLinker(null);
            resetAnimator(animator);
            resetRemote(player.getCurrentEquippedItem());
			break;
		default:
        	break;
		}
	}
	
	private static void handleBlockData(EntityPlayer player, TileEntityAnimator animator, int... data)
	{
		switch(data[0])
		{
		case 0://"+" button has been pressed
			animator.setDelay(1);
			break;
		case 1://"-" button has been pressed
			if(animator.getDelay()>-1)
			{//Lower delay won't work and might crash
				animator.setDelay(-1);
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
                animator.setEditing(false);
                animator.setLinker(null);
                if(data[0]==4)//This is a full reset
                {
                	if(data.length > 6)
                		if(player.worldObj.getBlockTileEntity(data[4], data[5], data[6]) instanceof TileEntityAnimator)
                		{
                			animator = (TileEntityAnimator) player.worldObj.getBlockTileEntity(data[4], data[5], data[6]);
                		}
                	resetAnimator(animator);
                }
                if(data.length > 6)//Get the item and reset it
                	resetRemote(animator.getStackInSlot(0));
                break;
        case 5://Increment Max number of frames that will run
        	animator.setMaxFrame(animator.getMaxFrame() + 1);
			break;
        case 6://Increment first frame to display
    		animator.setFrame(animator.getFrame() + 1);
        	break;
        default:
        	break;
		}
		
	}

	public static void resetAnimator(TileEntityAnimator animator) 
	{
		animator.setFrame(0);                
        animator.setMode(Mode.ORDER);
        animator.resetDelay();
        animator.setMaxFrame(-1);
        animator.setCount(0);
	}
	
	public static void resetRemote(ItemStack stack)
	{
		if(stack.getItem() instanceof ItemBase)
		{
	        ItemBase remote = (ItemBase)stack.getItem();
	        remote.resetLinker();
	        if(stack.hasTagCompound())
	        {
	        	stack.getTagCompound().removeTag(ItemBase.KEYTAG);
	        }
		}
	}

	public static Packet getPacket(TileEntityAnimator animator) 
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream(31);
        DataOutputStream dos = new DataOutputStream(bos);     
        try
        {
            dos.writeInt(animator.xCoord);
            dos.writeInt(animator.yCoord);
            dos.writeInt(animator.zCoord);
            dos.writeBoolean(animator.isEditing());
            dos.writeInt(animator.getFrame());
            dos.writeInt(animator.getMaxFrame());
            dos.writeInt(animator.getCount());
            dos.writeInt(animator.getDelay());
            dos.writeShort(animator.getMode().ordinal());
        }
        catch (IOException e)
        {
        	e.printStackTrace();
        }
        Packet250CustomPayload pkt = new Packet250CustomPayload();
        pkt.channel = GeneralRef.PACKET_CHANNELS[1];
        pkt.data = bos.toByteArray();
        pkt.length = bos.size();
        pkt.isChunkDataPacket = true;
        return pkt;
	}
	
	public static Packet getGuiPacket(boolean remote, int... data)
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream(1+4*data.length);
		DataOutputStream outputStream = new DataOutputStream(bos);
		try 
		{
			outputStream.writeBoolean(remote);
			for(int d:data)
				outputStream.writeInt(d);
		} 
		catch (IOException ex) 
		{
			ex.printStackTrace();
		}
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = GeneralRef.PACKET_CHANNELS[0];
		packet.data = bos.toByteArray();
		packet.length = bos.size();
		return packet;
	}
}
