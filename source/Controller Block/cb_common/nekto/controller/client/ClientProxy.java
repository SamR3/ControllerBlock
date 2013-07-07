package nekto.controller.client;

import java.io.ByteArrayOutputStream;

import nekto.controller.network.CommonProxy;
import nekto.controller.ref.GeneralRef;
import nekto.controller.render.TileEntityAnimatorRenderer;
import nekto.controller.tile.TileEntityAnimator;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy {

    public void registerRenderThings() 
    {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAnimator.class, new TileEntityAnimatorRenderer());
    }
    @Override
    protected void addPacketToQueue(Packet250CustomPayload packet, EntityPlayer player) 
	{
        if (player instanceof EntityClientPlayerMP) 
        {
            ((EntityClientPlayerMP) player).sendQueue.addToSendQueue(packet);    	
        }
	}
}
