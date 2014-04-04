package js25.advancedStuff.blocks;

import js25.advancedStuff.items.ItemDebugger;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

import java.util.List;

public class TileEntityMagnet extends TileEntity implements IDebuggable {

    public int timer;

    public TileEntityMagnet() {
        timer = 10;
    }

    @Override
    public void updateEntity() {
        if(worldObj.isRemote) return;
        if(timer > 0) {
            --timer;
            return;
        }
        timer = 100;
        if(BlockMagnet.isPowered(blockMetadata)) {
            AxisAlignedBB bounding = AxisAlignedBB.getBoundingBox(xCoord - 5, yCoord - 5, zCoord - 5, xCoord + 5, yCoord + 5, zCoord + 5);
            List<Entity> entities;
            if(BlockMagnet.isActive(blockMetadata)) {
                entities = worldObj.getEntitiesWithinAABB(Entity.class, bounding);
            } else {
                entities = worldObj.getEntitiesWithinAABB(EntityItem.class, bounding);
            }

            float x = xCoord + 0.5f;
            float y = yCoord + 0.5f;
            float z = zCoord + 0.5f;

            for(Entity e : entities) {
                double d1 = (x - e.posX) / 8;
                double d2 = (y - e.posY) / 8;
                double d3 = (z - e.posZ) / 8;
                double d4 = Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3);
                double d5 = 1.0D - d4;

                if (d5 > 0.0D)
                {
                    d5 *= d5;
                    e.motionX = d1 / d4 * d5 * 0.5D;
                    e.motionY = d2 / d4 * d5 * 0.5D;
                    e.motionZ = d3 / d4 * d5 * 0.5D;
                }
            }

        }


    }

    @Override
    public Object performDebug(ItemDebugger.DebugModes debugMode, Object in) {
        return null;
    }

    @Override
    public boolean isValid(ItemDebugger.DebugModes debugMode) {
       if(debugMode == ItemDebugger.DebugModes.INFO) return true;
       return false;
    }
}
