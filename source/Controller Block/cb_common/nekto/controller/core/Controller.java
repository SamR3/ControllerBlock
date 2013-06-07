/*
 *  Author: Sam6982
 */
package nekto.controller.core;

import nekto.controller.ref.GeneralRef;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = GeneralRef.MOD_ID, name = GeneralRef.MOD_NAME, version = GeneralRef.VERSION)
@NetworkMod(clientSideRequired = true, serverSideRequired = false /*, channels={GeneralRef.PACKET_CHANNEL}, packetHandler = PacketHandler.class*/)

public class Controller {

    //Blocks
    public static Block controllerBlock;
    
    //Items
    public static Item controllerLinker;
    
    @Instance(GeneralRef.MOD_ID)
    public static Controller instance;
@PreInit
public void preLoad(FMLPreInitializationEvent event) {
config= new Configuration(event.getSuggestedConfigurationFile(),true);
}

    @Init
    public void load(FMLInitializationEvent event) {
            
	controllerBlock = new BlockController(500);
	controllerLinker = new ItemLinker(1000);
        GameRegistry.registerTileEntity(TileEntityController.class, "controller");
        
        GameRegistry.registerBlock(controllerBlock, "controllerBlock");
        GameRegistry.registerItem(controllerLinker, "controllerLinker");    
        
        LanguageRegistry.addName(controllerBlock, "Controller Block");
        LanguageRegistry.addName(controllerLinker, "Linker");
        
        /*NetworkRegistry.instance().registerGuiHandler(this, new GuiHandler());*/
        GameRegistry.registerTileEntity(TileEntityController.class, "controllerBlockList");
    }
}
