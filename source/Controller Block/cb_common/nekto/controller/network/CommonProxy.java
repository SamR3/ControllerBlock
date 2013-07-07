package nekto.controller.network;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import nekto.controller.container.ContainerAnimator;
import nekto.controller.gui.AnimatorGUI;
import nekto.controller.ref.GeneralRef;
import nekto.controller.tile.TileEntityAnimator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class CommonProxy implements IGuiHandler{

	public static final int GUI_ID = 0;
	
    public void registerRenderThings() {}
	
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
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = GeneralRef.PACKET_CHANNEL;
		packet.data = bos.toByteArray();
		packet.length = bos.size();
		addPacketToQueue(packet,player);
	}

	protected void addPacketToQueue(Packet250CustomPayload packet, EntityPlayer player) 
	{
        if (player instanceof EntityPlayerMP)
        {
        	((EntityPlayerMP)player).playerNetServerHandler.sendPacketToPlayer(packet);
        }
	}
}
