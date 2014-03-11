package js25.advancedStuff;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import js25.advancedStuff.gui.GuiHandler;
import js25.advancedStuff.lib.ModInfo;
import js25.advancedStuff.proxies.CommonProxy;
import js25.advancedStuff.util.Log;
import js25.advancedStuff.util.ModCreativeTab;

@Mod(modid=ModInfo.ID, name=ModInfo.NAME, version=ModInfo.VERSION)
public class AdvancedStuff {

        @Instance(value = ModInfo.ID)
        public static AdvancedStuff instance;
        
        @SidedProxy(clientSide="js25.advancedStuff.proxies.ClientProxy", serverSide="js25.advancedStuff.proxies.CommonProxy")
        public static CommonProxy proxy;

        @EventHandler
        public void preInit(FMLPreInitializationEvent event) {
            Log.logger = event.getModLog();
            Config.init(event.getSuggestedConfigurationFile());

            ModCreativeTab.modGeneral = new ModCreativeTab();
            ModRegistry.registerBlocks();
            ModRegistry.registerTileEntities();
            ModRegistry.registerItems();

        	proxy.initSounds();
        	proxy.initRenderers();
        }
        
        @EventHandler
        public void load(FMLInitializationEvent event) {

            //ModBlocks.registerBlockNames();
            //ModItems.registerNames();
        	
            new GuiHandler();


        }
        
        @EventHandler
        public void postInit(FMLPostInitializationEvent event) {

            Crafting.initRecipes();

        }
        
        
       

}






