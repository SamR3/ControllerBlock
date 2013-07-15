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
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class PacketHandler implements IPacketHandler{

	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) 
	{
		if(packet.channel == GeneralRef.PACKET_CHANNELS[0])
		{//Sent by AnimatorGUI to server when a button has been activated
			handleGuiChange(packet,(EntityPlayer) player);
		}
		else if(packet.channel == GeneralRef.PACKET_CHANNELS[1])
		{//Sent by server to client
			handleDescriptionPacket(packet,(EntityPlayer) player);
		}
		//DEBUG:
		/*Side side = FMLCommonHandler.instance().getEffectiveSide();
		FMLLog.getLogger().info(side.toString()+" recieved a "+packet.channel +" packet");*/
	}
/**
 * Client method to handle a packet describing the TileEntityAnimator from server
 * @param packet
 * @param player
 */
	private static void handleDescriptionPacket(Packet250CustomPayload packet, EntityPlayer player)
	{
		ByteArrayDataInput dat = ByteStreams.newDataInput(packet.data);
        int x = dat.readInt();
        int y = dat.readInt();
        int z = dat.readInt();
        World world = Controller.proxy.getClientWorld();
        TileEntity te = world.getBlockTileEntity(x, y, z);
        if (te instanceof TileEntityAnimator)
        {
            TileEntityAnimator animator = (TileEntityAnimator) te;
            boolean editing = dat.readBoolean();
            animator.setEditing(editing);
            if(!editing && animator.getStackInSlot(0)!=null)
                resetRemote(animator.getStackInSlot(0));
            animator.setFrame(dat.readInt());
            animator.setMaxFrame(dat.readInt());
            animator.setCount(dat.readInt());
            animator.setDelay(dat.readInt());
            animator.setMode(Mode.values()[dat.readShort()]);
        }
	}
/**
 * Server method to handle a client action in AnimatorGUI
 * @param packet
 * @param player
 */
	private static void handleGuiChange(Packet250CustomPayload packet, EntityPlayer player) 
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
			handleData((TileEntityAnimator) ((ContainerAnimator)player.openContainer).getControl(),player,data);
			player.openContainer.detectAndSendChanges();
		}
	}

	public static void handleData(TileEntityAnimator animator, EntityPlayer player, int... data)
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
        	if(data.length > 1)//We got data from the item in slot
        	{
                TileEntity ent = player.worldObj.getBlockTileEntity(data[1], data[2], data[3]);
                if(ent != null && ent instanceof TileEntityAnimator)
                {//We change the animator to the one pointed by the item
                    animator = (TileEntityAnimator) ent;
                    animator.setEditing(false);
                    animator.setLinker(null);
                    if(data[0]==4)//This is a full reset
                    {
                    	resetAnimator(animator);
                    }
                    //Get the item and reset it
                    resetRemote(player.openContainer.getSlot(0).getStack());
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
        case 5://Increment Max number of frames that will run
        	animator.setMaxFrame(animator.getMaxFrame() + 1);
			break;
        case 6://Increment first frame to display
    		animator.setFrame(animator.getFrame() + 1);
        	break;
		}
		((EntityPlayerMP)player).playerNetServerHandler.sendPacketToPlayer(getPacket(animator));
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
        ItemBase remote = (ItemBase)stack.getItem();
        remote.resetLinker();
        stack.getTagCompound().removeTag(ItemBase.KEYTAG);
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
        }
        Packet250CustomPayload pkt = new Packet250CustomPayload();
        pkt.channel = GeneralRef.PACKET_CHANNELS[1];
        pkt.data = bos.toByteArray();
        pkt.length = bos.size();
        pkt.isChunkDataPacket = true;
        return pkt;
	}
}
