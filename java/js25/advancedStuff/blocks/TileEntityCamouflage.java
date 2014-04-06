package js25.advancedStuff.blocks;

import js25.advancedStuff.ModRegistry;
import js25.advancedStuff.items.ItemDebugger;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import java.util.LinkedList;
import java.util.List;

public class TileEntityCamouflage extends TileEntity implements IDebuggable, IServerClientSync {

    //const
    private static final int TICKS_NEW_SYNC = 5;
    private static final int TICKS_NEW_OWNER_CHECK = 5;
    //pers
    public Block cover;
    public TileEntityBuilder owner; //TODO: Interface for MBS owners
    //temp
    private int nextSync;
    private int nextOwnerCheck;
    private Integer savedOwnerX, savedOwnerY, savedOwnerZ;

    public TileEntityCamouflage() {
        this.cover = ModRegistry.Blocks.CAMOUFLAGE.instance;
    }

    @Override
    public void updateEntity() {

        if(owner == null && savedOwnerX != null && savedOwnerY != null && savedOwnerZ != null) {
            if(worldObj.getBlock(savedOwnerX, savedOwnerY, savedOwnerZ).hasTileEntity(0)) {
                TileEntity tile = worldObj.getTileEntity(savedOwnerX, savedOwnerY, savedOwnerZ);
                if(tile instanceof TileEntityBuilder) {
                    owner = (TileEntityBuilder)tile;
                    worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                } else {
                    savedOwnerX = null;
                    savedOwnerY = null;
                    savedOwnerZ = null;
                }
            } else {
                savedOwnerX = null;
                savedOwnerY = null;
                savedOwnerZ = null;
            }
        }

        if(--nextOwnerCheck <= 0) {
            if(owner != null) {
                if(worldObj.getBlock(owner.xCoord, owner.yCoord, owner.zCoord).hasTileEntity(0)) {
                    TileEntity tile = worldObj.getTileEntity(owner.xCoord, owner.yCoord, owner.zCoord);
                    if(!(tile instanceof TileEntityBuilder)) {
                        owner = null;
                        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                    }
                } else {
                    owner = null;
                    worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                }
            }
            nextOwnerCheck = TICKS_NEW_OWNER_CHECK;
        }

        //sync
        if(--nextSync <= 0) {
            if(!worldObj.isRemote) syncClient();
            //if(worldObj.isRemote) worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            nextSync = TICKS_NEW_SYNC;
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
        if(owner != null) {
            worldObj.addBlockEvent(xCoord, yCoord, zCoord, getBlockType(), 1, owner.xCoord);
            worldObj.addBlockEvent(xCoord, yCoord, zCoord, getBlockType(), 2, owner.yCoord);
            worldObj.addBlockEvent(xCoord, yCoord, zCoord, getBlockType(), 3, owner.zCoord);
        }
    }

    @Override
    public boolean receiveClientEvent(int id, int value) {
        switch(id) {
            case 0:
                if(cover != Block.getBlockById(value))
                    worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                cover = Block.getBlockById(value); break;
            case 1: savedOwnerX = value; break;
            case 2: savedOwnerY = value; break;
            case 3: savedOwnerZ = value; break;
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
            savedOwnerX = ownerCompound.getInteger("x");
            savedOwnerY = ownerCompound.getInteger("y");
            savedOwnerZ = ownerCompound.getInteger("z");
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
