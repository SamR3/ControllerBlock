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
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = GeneralRef.MOD_ID, name = GeneralRef.MOD_NAME, version = GeneralRef.VERSION)
@NetworkMod(clientSideRequired = true, channels = { "AnimatorGui", "Animator" }, packetHandler = PacketHandler.class)
public class Controller {
	//Blocks
	public static Block controller, animator;
	//Items
	public static Item linker, remote;
	public static boolean tickDisplay;
	@Instance(GeneralRef.MOD_ID)
	public static Controller instance;
	@SidedProxy(clientSide = GeneralRef.CLIENT_PROXY, serverSide = GeneralRef.COMMON_PROXY)
	public static CommonProxy proxy;

	@EventHandler
	public void load(FMLInitializationEvent event) {
		GameRegistry.addRecipe(new ItemStack(animator), new Object[] { "IPI", "DRE", "TBW", Character.valueOf('I'), Block.oreIron, Character.valueOf('P'), Item.enderPearl, Character.valueOf('D'),
				Item.diamond, Character.valueOf('R'), Block.blockRedstone, Character.valueOf('E'), Item.emerald, Character.valueOf('T'), Block.enchantmentTable, Character.valueOf('B'), Item.book,
				Character.valueOf('W'), Block.workbench });
		GameRegistry.addRecipe(new ItemStack(remote), new Object[] { "D", "I", "I", Character.valueOf('D'), Item.diamond, Character.valueOf('I'), Item.ingotIron });
		NetworkRegistry.instance().registerGuiHandler(this, proxy);
		GameRegistry.registerTileEntity(TileEntityController.class, "controllerBlockList");
		GameRegistry.registerTileEntity(TileEntityAnimator.class, "animatorBlockList");
		proxy.registerRenderThings();
	}

	@EventHandler
	public void preLoad(FMLPreInitializationEvent event) {
		Configuration config = new Configuration(event.getSuggestedConfigurationFile(), true);
		config.load();
		controller = new BlockController(config.getBlock("controller id", 500).getInt());
		linker = new ItemLinker(config.getItem("linker id", 9000).getInt()).setTextureName(GeneralRef.TEXTURE_PATH + "linker");
		animator = new BlockAnimator(config.getBlock("animator id", 501).getInt());
		remote = new ItemRemote(config.getItem("remote id", 9001).getInt()).setTextureName(GeneralRef.TEXTURE_PATH + "remote");
		tickDisplay = config.get("general", "Show delay as ticks", false).getBoolean(false);
		if (config.hasChanged())
			config.save();
		GameRegistry.registerBlock(controller, "controller");
		GameRegistry.registerBlock(animator, "animator");
		GameRegistry.registerItem(linker, "linker");
		GameRegistry.registerItem(remote, "remote");
	}
}
