package js25.advancedStuff.util;

import net.minecraftforge.oredict.OreDictionary;

public class CraftingHelper {

    public static boolean isOreRegistered(String oreName) {
        return !OreDictionary.getOres(oreName).isEmpty();
    }


}
