package js25.advancedStuff;

import cpw.mods.fml.common.registry.GameRegistry;
import js25.advancedStuff.util.CraftingHelper;
import js25.advancedStuff.util.ModManager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class Crafting {

    public static void initRecipes() {

        Object recommendedIron;
        if(Config.useSteelCrafting && CraftingHelper.isOreRegistered("ingotSteel")) {
            recommendedIron = Config.usePlateCrafting && CraftingHelper.isOreRegistered("plateSteel") ? "plateSteel" : "ingotSteel";
        } else if(Config.usePlateCrafting && CraftingHelper.isOreRegistered("plateIron")) {
            recommendedIron = "plateIron";
        } else {
            recommendedIron = Items.iron_ingot;
        }

        {
        Object c = ModManager.mods.IC2.isLoaded() && Config.useIC2Recipes ? null /*ic2.api.item.Items.getItem("advancedCircuit")*/ : new ItemStack(Blocks.redstone_block);
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModRegistry.Blocks.BUILDER.instance),
            "aba", "aca", "aaa",
            'a', recommendedIron, 'b', new ItemStack(Blocks.heavy_weighted_pressure_plate), 'c', c
        )); }


    }

}
