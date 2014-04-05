package js25.advancedStuff.blocks;

import js25.advancedStuff.items.ItemDebugger;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;

import java.util.LinkedList;
import java.util.List;

public class TileEntityCamouflage extends TileEntity implements IDebuggable {

    public Block cover;

    public TileEntityCamouflage() {
        //this.cover = ModRegistry.Blocks.CAMOUFLAGE.instance;
        this.cover = Blocks.stained_glass;
    }

    @Override
    public void updateEntity() {

    }

    @Override
    public boolean isValid(ItemDebugger.DebugModes debugMode) {
        if(debugMode == ItemDebugger.DebugModes.INFO) return true;
        return false;
    }

    @Override
    public Object performDebug(ItemDebugger.DebugModes debugMode, Object in) {
        if(debugMode == ItemDebugger.DebugModes.INFO) {
            List data = (LinkedList<String>)in;
            data.add("Cover: " + cover.getClass().getName());
            return data;
        }
        return null;
    }

}
