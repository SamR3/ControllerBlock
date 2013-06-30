package nekto.controller.network;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import nekto.controller.container.ContainerAnimator;
import nekto.controller.gui.AnimatorGUI;
import nekto.controller.ref.GeneralRef;
import nekto.controller.tile.TileEntityAnimator;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.relauncher.Side;

public class CommonProxy implements IGuiHandler{

	public static final int GUI_ID = 0;
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) 
	{
	    TileEntity tile = world.getBlockTileEntity(x, y, z);
	    
		if(ID == GUI_ID && tile instanceof TileEntityAnimator)
		{
			return new ContainerAnimator(player.inventory, (TileEntityAnimator) tile);
		}
		
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) 
	{	       
        TileEntity tile = world.getBlockTileEntity(x, y, z);
        
        if(ID == GUI_ID && tile instanceof TileEntityAnimator)
        {
			return new AnimatorGUI(player.inventory, (TileEntityAnimator) tile);
        }
			
	    return null;
	}

	public void sendPacket( EntityPlayer player,int... data) 
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream(4*data.length);
		DataOutputStream outputStream = new DataOutputStream(bos);
		try 
		{
			for(int d:data)
				outputStream.writeInt(d);
		} 
		catch (Exception ex) 
		{
			ex.printStackTrace();
		}
		addPacketToQueue(bos,player);
	}

	private void addPacketToQueue(ByteArrayOutputStream bos, EntityPlayer player) 
	{
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = GeneralRef.PACKET_CHANNEL;
		packet.data = bos.toByteArray();
		packet.length = bos.size();
		
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) 
        {
            ((EntityClientPlayerMP) player).sendQueue.addToSendQueue(packet);    	
        }
	}
}
