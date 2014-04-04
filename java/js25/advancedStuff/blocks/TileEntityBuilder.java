package js25.advancedStuff.blocks;

import js25.advancedStuff.ModRegistry;
import js25.advancedStuff.items.ItemDebugger;
import js25.advancedStuff.util.MultiBlockStructure;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import java.util.LinkedList;
import java.util.List;

public class TileEntityBuilder extends TileEntity implements IServerClientSync, IDebuggable {

    //const
    private static final int UNITS_PER_TICK = 1;
    private static final int TICKS_NEW_SYNC = 5;
    private static final MultiBlockStructure BASIC_MULTI_BLOCK = new MultiBlockStructure(ModRegistry.Blocks.BUILDER.instance, 3, 3, 3, new Block[][] {null, new Block[] {null, null, null, Blocks.iron_block, Blocks.iron_block, Blocks.iron_block, null, ModRegistry.Blocks.BUILDER.instance, null}, null});
    private static final MultiBlockStructure BOILER_MULTI_BLOCK = new MultiBlockStructure(ModRegistry.Blocks.BUILDER.instance, 4, 3, 3, new Block[][] {new Block[] {Blocks.iron_block, Blocks.iron_block, Blocks.iron_block, Blocks.iron_block, Blocks.iron_block, Blocks.iron_block, Blocks.iron_block, Blocks.iron_block, Blocks.iron_block}, new Block[] {Blocks.iron_block, Blocks.iron_block, Blocks.iron_block, Blocks.iron_block, Blocks.air, Blocks.iron_block, Blocks.iron_block, ModRegistry.Blocks.BUILDER.instance, Blocks.iron_block}, new Block[] {Blocks.iron_block, Blocks.iron_block, Blocks.iron_block, Blocks.iron_block, Blocks.air, Blocks.iron_block, Blocks.iron_block, Blocks.iron_block, Blocks.iron_block}, new Block[] {Blocks.iron_block, Blocks.iron_block, Blocks.iron_block, Blocks.iron_block, Blocks.iron_block, Blocks.iron_block, Blocks.iron_block, Blocks.iron_block, Blocks.iron_block}});
    private static final MultiBlockStructure FURNACE_MULTI_BLOCK = new MultiBlockStructure(ModRegistry.Blocks.BUILDER.instance, 2, 3, 2, new Block[][] {new Block[] {Blocks.iron_block, Blocks.iron_block, Blocks.iron_block, Blocks.iron_block, Blocks.netherrack, Blocks.iron_block}, new Block[] {Blocks.iron_block, ModRegistry.Blocks.BUILDER.instance, Blocks.iron_block, Blocks.iron_block, Blocks.air, Blocks.iron_block}});
    //pers
    public int powerUnitsStored;
    //temp
    public boolean isRunning;
    private int nextSync;

    public TileEntityBuilder() {
        this.powerUnitsStored = 0;
        this.isRunning = false;
        this.nextSync = 2;

    }

    @Override
    public void updateEntity() {
        if(!worldObj.isRemote) {
            if(nextSync == 0) {
                syncClient();
                nextSync = TICKS_NEW_SYNC;
            } else {
                --nextSync;
            }
        }

        if(isRunning) {
            if(powerUnitsStored - UNITS_PER_TICK < 0) {
                isRunning = false;
            } else {
                powerUnitsStored -= UNITS_PER_TICK;

            }
        }


    }

    @Override
    public void syncClient() {
        worldObj.addBlockEvent(xCoord, yCoord, zCoord, worldObj.getBlock(xCoord, yCoord, zCoord), 0, powerUnitsStored);
    }

    @Override
    public boolean receiveClientEvent(int id, int value) {
        switch(id) {
            case 0: powerUnitsStored = value; break;
            default: return false;
        }
        return true;
    }

    @Override
    public boolean isValid(ItemDebugger.DebugModes debugMode) {
        if(debugMode == ItemDebugger.DebugModes.INFO) return true;
        if(debugMode == ItemDebugger.DebugModes.INJECT) return true;

        return false;
    }

    @Override
    public Object performDebug(ItemDebugger.DebugModes debugMode, Object in) {
        if(debugMode == ItemDebugger.DebugModes.INFO) {
            List data = (LinkedList<String>)in;
            data.add(String.format("Power: %d, machine is %s", powerUnitsStored, isRunning ? "active" : "inactive"));
            return data;
        } else if(debugMode == ItemDebugger.DebugModes.INJECT) {
            List data = (LinkedList<String>)in;

            data.add(BOILER_MULTI_BLOCK.isValidStructure(worldObj, xCoord, yCoord, zCoord) ? "True" : "False");

            return data;
        }

        return null;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        powerUnitsStored = compound.getInteger("power");
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        compound.setInteger("power", powerUnitsStored);
    }


}
