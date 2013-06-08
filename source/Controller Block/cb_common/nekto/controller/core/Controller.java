/*
 *  Author: Sam6982
 */
package nekto.controller.core;

import nekto.controller.ref.GeneralRef;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
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
    @SidedProxy(clientSide="nekto.controller.core.ClientProxy", serverSide="nekto.controller.core.CommonProxy")
	public static CommonProxy proxy;
	
	private Configuration config;
	@PreInit
	public void preLoad(FMLPreInitializationEvent event) {
		config= new Configuration(event.getSuggestedConfigurationFile(),true);
		
	}

    @Init
    public void load(FMLInitializationEvent event) {
    	config.load();
    	controllerBlock = new BlockController(config.get("block", "controller block id", 500).getInt());
    	controllerLinker = new ItemLinker(config.get("item", "linker item id", 1000).getInt());
    	if(config.hasChanged())
    		config.save();
        
        GameRegistry.registerBlock(controllerBlock, "controllerBlock");
        GameRegistry.registerItem(controllerLinker, "controllerLinker");    
        
        LanguageRegistry.addName(controllerBlock, "Controller Block");
        LanguageRegistry.addName(controllerLinker, "Linker");
        
        NetworkRegistry.instance().registerGuiHandler(this, proxy);
        GameRegistry.registerTileEntity(TileEntityController.class, "controllerBlockList");
    }
}
