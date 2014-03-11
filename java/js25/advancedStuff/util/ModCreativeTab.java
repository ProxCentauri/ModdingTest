package js25.advancedStuff.util;

import js25.advancedStuff.ModRegistry;
import js25.advancedStuff.lib.ModInfo;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ModCreativeTab extends CreativeTabs {

    public static CreativeTabs modGeneral;

    public ModCreativeTab() {
        super("tab" + ModInfo.ID);
    }

    public Item getTabIconItem() {
        return new ItemStack(ModRegistry.Blocks.BUILDER.instance).getItem();
    }

    @Override
    public String getTabLabel() {
        return "mainTab";
    }

    @Override
    public String getTranslatedTabLabel() {
        return ModInfo.NAME;
    }
}
