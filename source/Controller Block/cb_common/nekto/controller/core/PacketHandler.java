package nekto.controller.core;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import nekto.controller.ref.GeneralRef;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
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
		short data;
		try 
		{
           data = inStream.readShort();
		} 
		catch (IOException e) 
		{
            e.printStackTrace();
            return;
		}
		if(data == 1)//First button in GuiController has been pressed
		{
			((TileEntityController) ((ContainerController)player.openContainer).getControl()).setState(true);
		}
	}

}
