package nekto.controller.client;

import nekto.controller.network.CommonProxy;
import nekto.controller.render.TileEntityAnimatorRenderer;
import nekto.controller.tile.TileEntityAnimator;
import cpw.mods.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy {

    public void registerRenderThings() 
    {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAnimator.class, new TileEntityAnimatorRenderer());
    }
    
}
