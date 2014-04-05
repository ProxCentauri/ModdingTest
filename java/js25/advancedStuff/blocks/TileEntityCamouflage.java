package js25.advancedStuff.blocks;

import js25.advancedStuff.ModRegistry;
import js25.advancedStuff.items.ItemDebugger;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import java.util.LinkedList;
import java.util.List;

public class TileEntityCamouflage extends TileEntity implements IDebuggable, IServerClientSync {

    private int nextSync;
    private static final int TICKS_NEW_SYNC = 5;
    public Block cover;
    public TileEntityBuilder owner; //TODO: Interface for MBS owners
    private Integer ownerX, ownerY, ownerZ;

    public TileEntityCamouflage() {
        this.cover = ModRegistry.Blocks.CAMOUFLAGE.instance;
    }

    @Override
    public void updateEntity() {
        if(--nextSync <= 0) {
            if(!worldObj.isRemote) syncClient();
            if(worldObj.isRemote) worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            nextSync = TICKS_NEW_SYNC;
        }

        if(owner == null && ownerX != null && ownerY != null && ownerZ != null) {
            owner = (TileEntityBuilder)worldObj.getTileEntity(ownerX, ownerY, ownerZ);
        }

        if(owner != null && (ownerX == null || ownerY == null || ownerZ == null)) {
            ownerX = owner.xCoord;
            ownerY = owner.yCoord;
            ownerZ = owner.zCoord;
        }
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
            data.add("Cover: " + Block.blockRegistry.getNameForObject(cover));
            if(owner != null) {
                data.add(String.format("Owner: [%d, %d, %d]", owner.xCoord, owner.yCoord, owner.zCoord));
            } else {
                data.add("Owner not found");
            }
            return data;
        }
        return null;
    }


    @Override
    public void syncClient() {
        worldObj.addBlockEvent(xCoord, yCoord, zCoord, getBlockType(), 0, Block.blockRegistry.getIDForObject(cover));
        if(ownerX != null && ownerY != null && ownerZ != null) {
            worldObj.addBlockEvent(xCoord, yCoord, zCoord, getBlockType(), 1, ownerX);
            worldObj.addBlockEvent(xCoord, yCoord, zCoord, getBlockType(), 2, ownerY);
            worldObj.addBlockEvent(xCoord, yCoord, zCoord, getBlockType(), 3, ownerZ);
        }
    }

    @Override
    public boolean receiveClientEvent(int id, int value) {
        switch(id) {
            case 0:
                if(cover != Block.getBlockById(value))
                    worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                cover = Block.getBlockById(value); break;
            case 1: ownerX = value; break;
            case 2: ownerY = value; break;
            case 3: ownerZ = value; break;
            default: return false;
        }
        return true;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        cover = (Block)Block.getBlockFromName(compound.getString("Cover"));
        if(cover == null) cover = getBlockType();
        try {
            NBTTagCompound ownerCompound = compound.getCompoundTag("Owner");
            ownerX = ownerCompound.getInteger("x");
            ownerY = ownerCompound.getInteger("y");
            ownerZ = ownerCompound.getInteger("z");
        } catch(Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setString("Cover", Block.blockRegistry.getNameForObject(cover));
        if(owner != null) {
            NBTTagCompound ownerCompound = new NBTTagCompound();
            ownerCompound.setInteger("x", owner.xCoord);
            ownerCompound.setInteger("y", owner.yCoord);
            ownerCompound.setInteger("z", owner.zCoord);
            compound.setTag("Owner", ownerCompound);
        }
    }

}
