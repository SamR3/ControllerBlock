package nekto.controller.core;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import nekto.controller.ref.GeneralRef;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
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
	    
		if(ID == GUI_ID && tile instanceof TileEntityController)
		{
			return new ContainerController(player.inventory, (TileEntityController) tile);
		}
		
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) 
	{	       
        TileEntity tile = world.getBlockTileEntity(x, y, z);
        
        if(ID == GUI_ID && tile instanceof TileEntityController)
        {
			return new GuiController(player.inventory, (TileEntityController) tile);
        }
			
	    return null;
	}
	
	public void sendPacket(int i, Entity playerEntity) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(2);
		DataOutputStream outputStream = new DataOutputStream(bos);
		try 
		{
			outputStream.writeShort(i);
		} 
		catch (Exception ex) 
		{
			ex.printStackTrace();
		}
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = GeneralRef.PACKET_CHANNEL;
		packet.data = bos.toByteArray();
		packet.length = bos.size();
		
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) 
        {
            ((EntityClientPlayerMP) playerEntity).sendQueue.addToSendQueue(packet);    	
        }
	}
}
