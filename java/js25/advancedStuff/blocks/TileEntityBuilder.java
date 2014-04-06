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
    private static final int TICKS_NEW_MBS_UPDATE = 5;
    private static final MultiBlockStructure BASIC_MULTI_BLOCK = new MultiBlockStructure(ModRegistry.Blocks.BUILDER.instance, false, 3, 3, 3, new Block[][] {null, new Block[] {null, null, null, Blocks.iron_block, Blocks.iron_block, Blocks.iron_block, null, ModRegistry.Blocks.BUILDER.instance, null}, null});
    private static final MultiBlockStructure BOILER_MULTI_BLOCK = new MultiBlockStructure(ModRegistry.Blocks.BUILDER.instance, true, 4, 3, 3, new Block[][] {new Block[] {Blocks.iron_block, Blocks.iron_block, Blocks.iron_block, Blocks.iron_block, Blocks.iron_block, Blocks.iron_block, Blocks.iron_block, Blocks.iron_block, Blocks.iron_block}, new Block[] {Blocks.iron_block, Blocks.iron_block, Blocks.iron_block, Blocks.iron_block, Blocks.air, Blocks.iron_block, Blocks.iron_block, ModRegistry.Blocks.BUILDER.instance, Blocks.iron_block}, new Block[] {Blocks.iron_block, Blocks.iron_block, Blocks.iron_block, Blocks.iron_block, Blocks.air, Blocks.iron_block, Blocks.iron_block, Blocks.iron_block, Blocks.iron_block}, new Block[] {Blocks.iron_block, Blocks.iron_block, Blocks.iron_block, Blocks.iron_block, Blocks.iron_block, Blocks.iron_block, Blocks.iron_block, Blocks.iron_block, Blocks.iron_block}});
    private static final MultiBlockStructure FURNACE_MULTI_BLOCK = new MultiBlockStructure(ModRegistry.Blocks.BUILDER.instance, false, 2, 3, 2, new Block[][] {new Block[] {Blocks.iron_block, Blocks.iron_block, Blocks.iron_block, Blocks.iron_block, Blocks.netherrack, Blocks.iron_block}, new Block[] {Blocks.iron_block, ModRegistry.Blocks.BUILDER.instance, Blocks.iron_block, Blocks.iron_block, Blocks.air, Blocks.iron_block}});
    //pers
    public int powerUnitsStored;
    //temp
    public boolean isRunning;
    public boolean isValidStructure;
    private int nextSync;
    private int nextMBSUpdate;

    public TileEntityBuilder() {
        this.powerUnitsStored = 0;
        this.isValidStructure = false;
        this.isRunning = false;
        this.nextSync = 2;
        this.nextMBSUpdate = 2;

    }

    @Override
    public void updateEntity() {

        if(--nextMBSUpdate <= 0) {
            boolean valid = BOILER_MULTI_BLOCK.isValidStructure(worldObj, xCoord, yCoord, zCoord);
            if(valid && !isValidStructure) {
                createdMultiBlockStructure();
                isValidStructure = true;
            } else if(!valid && isValidStructure) {
                destroyedMultiBlockStructure();
                isValidStructure = false;
            }
            nextMBSUpdate = TICKS_NEW_MBS_UPDATE;
        }

        if(isRunning) { //pseudo
            if(powerUnitsStored - UNITS_PER_TICK < 0) {
                isRunning = false;
            } else {
                powerUnitsStored -= UNITS_PER_TICK;

            }
        }

        //sync
        if(!worldObj.isRemote && --nextSync <= 0) {
            syncClient();
            nextSync = TICKS_NEW_SYNC;
        }

    }

    public void updateMultiBlockStructure() {
        if(BOILER_MULTI_BLOCK.isValidStructure(worldObj, xCoord, yCoord, zCoord))
            createdMultiBlockStructure();
    }

    private void createdMultiBlockStructure() {
        List blocks = BOILER_MULTI_BLOCK.getValidStructureBlocks(worldObj, xCoord, yCoord, zCoord);
        for(int i = 0; i < blocks.size(); i += 3) {
            int x = (Integer)blocks.get(i);
            int y = (Integer)blocks.get(i + 1);
            int z = (Integer)blocks.get(i + 2);
            Block b = worldObj.getBlock(x, y, z);
            if(b != null && b != Blocks.air && !(b instanceof BlockBuilder)) {
                worldObj.setBlock(x, y, z, ModRegistry.Blocks.CAMOUFLAGE.instance, worldObj.getBlockMetadata(x, y, z), 6);
                ((TileEntityCamouflage)worldObj.getTileEntity(x, y, z)).cover = (b instanceof BlockCamouflage ? ((TileEntityCamouflage) worldObj.getTileEntity(x, y, z)).cover : b);
                ((TileEntityCamouflage)worldObj.getTileEntity(x, y, z)).owner = this;
            }

            worldObj.markBlockForUpdate(x, y, z);
        }
    }

    private void destroyedMultiBlockStructure() {
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);

        for(int i = 0; i < worldObj.loadedTileEntityList.size(); i++) {
            TileEntity tile = (TileEntity)worldObj.loadedTileEntityList.get(i);
            if(tile instanceof TileEntityBuilder) {
                if(getDistanceFrom(tile.xCoord, tile.yCoord, tile.zCoord) <= 256)
                    ((TileEntityBuilder)tile).updateMultiBlockStructure();
            }
        }
    }

    @Override
    public void syncClient() {
        worldObj.addBlockEvent(xCoord, yCoord, zCoord, getBlockType(), 0, powerUnitsStored);
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
            data.add(isValidStructure ? "Inside a valid structure" : "No valid structure found");
            return data;
        } else if(debugMode == ItemDebugger.DebugModes.INJECT) {
            List data = (LinkedList<String>)in;

            nextSync = 0;
            nextMBSUpdate = 0;
            data.add("Forced update and sync");

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
