package js25.advancedStuff;

import cpw.mods.fml.common.registry.GameRegistry;
import js25.advancedStuff.blocks.BlockBuilder;
import js25.advancedStuff.blocks.BlockMagnet;
import js25.advancedStuff.blocks.TileEntityBuilder;
import js25.advancedStuff.blocks.TileEntityMagnet;
import js25.advancedStuff.items.ItemDebugger;
import js25.advancedStuff.util.ModCreativeTab;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;

public class ModRegistry {

    private static final Object UNDEFINED = null;
    private static final float F_UNDEFINED = -1;

    public static enum Blocks {
        BUILDER(new BlockBuilder(Material.iron), "builder", 5.0F, 10.0F, Block.soundTypeMetal),
        MAGNET(new BlockMagnet(Material.iron), "magnet", 5.0F, 10.0F, Block.soundTypeMetal);

        public Block instance;
        public String uniqueName, unlocalizedName;
        public CreativeTabs creativeTab;
        public float hardness, resistance;
        public Block.SoundType stepSound;

        private Blocks(Block instance, String name, float hardness, float resistance, Block.SoundType stepSound) {
            this.instance = instance;
            this.uniqueName = name;
            this.unlocalizedName = name;
            this.creativeTab = ModCreativeTab.modGeneral;
            this.hardness = hardness;
            this.resistance = resistance;
            this.stepSound = stepSound;
        }

    }

    public static enum Items {
        DEBUGGER(new ItemDebugger(), "debugger", 1);

        public Item instance;
        public String uniqueName, unlocalizedName;
        public CreativeTabs creativeTab;
        int maxStackSize;

        private Items(Item instance, String name, int maxStackSize) {
            this.instance = instance;
            this.uniqueName = name;
            this.unlocalizedName = name;
            this.creativeTab = ModCreativeTab.modGeneral;
            this.maxStackSize = maxStackSize;
        }
    }

    public static enum TileEntities {
        MAGNET(TileEntityMagnet.class, "tileEntityMagnet"),
        BUILDER(TileEntityBuilder.class, "tileEntityBuilder");

        public Class<? extends TileEntity> tileClass;
        public String identifier;

        private TileEntities(Class<? extends TileEntity> tileClass, String identifier) {
            this.tileClass = tileClass;
            this.identifier = identifier;
        }

    }

    public static void registerBlocks() {
        for(Blocks cur : Blocks.values()) {
            GameRegistry.registerBlock(cur.instance, cur.uniqueName);
            if(cur.unlocalizedName != UNDEFINED) cur.instance.setBlockName(cur.unlocalizedName);
            if(cur.creativeTab != UNDEFINED) cur.instance.setCreativeTab(cur.creativeTab);
            if(cur.hardness != F_UNDEFINED) cur.instance.setHardness(cur.hardness);
            if(cur.resistance != F_UNDEFINED) cur.instance.setResistance(cur.resistance);
            if(cur.stepSound != UNDEFINED) cur.instance.setStepSound(cur.stepSound);
        }
    }

    public static void registerItems() {
        for(Items cur : Items.values()) {
            GameRegistry.registerItem(cur.instance, cur.uniqueName);
            if(cur.unlocalizedName != UNDEFINED) cur.instance.setUnlocalizedName(cur.unlocalizedName);
            if(cur.creativeTab != UNDEFINED) cur.instance.setCreativeTab(cur.creativeTab);
            if(cur.maxStackSize != UNDEFINED) cur.instance.setMaxStackSize(cur.maxStackSize);
        }
    }

    public static void registerTileEntities() {
        for(TileEntities cur : TileEntities.values())
            GameRegistry.registerTileEntity(cur.tileClass, cur.identifier);
    }

}
