package js25.advancedStuff.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;

//TODO write some proper code!
public class PacketContainerUpdate implements IPacket {

    int flag;
    TileEntity tileEntity;
    InventoryPlayer invPlayer;

    public PacketContainerUpdate() {}
    public PacketContainerUpdate(Object origin, int ID, byte[] data) {
        if(origin instanceof TileEntity) {
            tileEntity = (TileEntity)origin;
            flag = 0;
        } else if(origin instanceof InventoryPlayer) {
            invPlayer = (InventoryPlayer)origin;
            flag = 1;
        } else {
            throw new NullPointerException("No valid position");
        }


    }

    @Override
    public void readData(ByteBuf data) {

    }

    @Override
    public void writeData(ByteBuf data) {

    }
}
