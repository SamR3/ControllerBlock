/*
 *  Author: Sam6982
 */
package nekto.controller.core;

import nekto.controller.block.BlockAnimator;
import nekto.controller.block.BlockController;
import nekto.controller.item.ItemLinker;
import nekto.controller.item.ItemRemote;
import nekto.controller.network.CommonProxy;
import nekto.controller.network.PacketHandler;
import nekto.controller.ref.GeneralRef;
import nekto.controller.tile.TileEntityAnimator;
import nekto.controller.tile.TileEntityController;
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
@NetworkMod(clientSideRequired = true, serverSideRequired = false , channels={"Gui","Animator"}, packetHandler = PacketHandler.class)

public class Controller {

    //Blocks
    public static Block controllerBlock;
    public static Block animator;
    
    //Items
    public static Item controllerLinker;
    public static Item remote;
    
	public static boolean tickDisplay;
    
    @Instance(GeneralRef.MOD_ID)
    public static Controller instance;
    @SidedProxy(clientSide = GeneralRef.CLIENT_PROXY, serverSide = GeneralRef.COMMON_PROXY)
	public static CommonProxy proxy;
	
	private Configuration config;
	@PreInit
	public void preLoad(FMLPreInitializationEvent event) 
	{
		config= new Configuration(event.getSuggestedConfigurationFile(),true);
	}

    @Init
    public void load(FMLInitializationEvent event) 
    {
    	config.load();
    	controllerBlock = new BlockController(config.get("block", "controller id", 500).getInt());
    	controllerLinker = new ItemLinker(config.get("item", "linker id", 1000).getInt());
    	animator = new BlockAnimator(config.get("block", "animator id", 501).getInt());
    	remote = new ItemRemote(config.get("item", "remote id", 1001).getInt());
    	tickDisplay = config.get("general", "Show delay as ticks", false).getBoolean(false);
    	if(config.hasChanged())
    		config.save();
        
        GameRegistry.registerBlock(controllerBlock, "controllerBlock");
        GameRegistry.registerBlock(animator, "animatorBlock");
        GameRegistry.registerItem(controllerLinker, "controllerLinker");
        GameRegistry.registerItem(remote, "remote");
        
        LanguageRegistry.addName(controllerBlock, "Controller Block");
        LanguageRegistry.addName(animator, "Animator");
        LanguageRegistry.addName(controllerLinker, "Linker");
        LanguageRegistry.addName(remote, "Remote");
        
        NetworkRegistry.instance().registerGuiHandler(this, proxy);
        GameRegistry.registerTileEntity(TileEntityController.class, "controllerBlockList");
        GameRegistry.registerTileEntity(TileEntityAnimator.class, "animatorBlockList");
        
        proxy.registerRenderThings();
    }
}
