package js25.advancedStuff;

import js25.advancedStuff.util.Log;
import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class Config {

	private static Configuration config;
	private static List<Integer> changedIDs = new LinkedList<Integer>();
	
	public static boolean logAllData;
	public static boolean powerfulSystem;
    public static boolean usePlateCrafting;
    public static boolean useSteelCrafting;
    public static boolean useIC2Recipes;
    public static boolean userTERecipes;

	public static void init(File file) {
		config = new Configuration(file);
		config.load();

        //custom categories
        String blocks = "Blocks";
        String items = "Items";
        String recipes = "Recipes";

        //general config stuff
        logAllData = config.get(config.CATEGORY_GENERAL, "Enable advanced debugging", true, "Advanced debugging can be used to track down problems. Your console will get spammed!").getBoolean(true);
		Log.add(logAllData ? "Debugging is enabled, loading configuration..." : "Loading configuration...");

        powerfulSystem = config.get(config.CATEGORY_GENERAL, "Allow heavy rendering", true, "Renders in a more detailed way, only meant for powerful computers. Don't complain about lag with this enabled.").getBoolean(true);
        if(powerfulSystem) Log.add("Heavy rendering is enabled, don't complain about lag!");

        //getRecipeSettings
        usePlateCrafting = config.get(recipes, "use plates", true, "Tries to use plates instead of ingots for crafting, will only work with a mod like IC2, Railcraft or GT installed").getBoolean(true);
        useSteelCrafting = config.get(recipes, "use steel", true, "Changes most recipes to require steel instead of iron").getBoolean(true);
        useIC2Recipes = config.get(recipes, "use IC2 recipes", true, "Will change recipes to use components of those mods. Priority: IC2 > TE > BC").getBoolean(true);
        userTERecipes = config.get(recipes, "use Thermal Expansion recipes", true).getBoolean(true);


		if(logAllData) Log.add("Finished to load the config");
		
		config.save();
		
	}
		
	
	
}











