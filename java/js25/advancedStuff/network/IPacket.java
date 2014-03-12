package js25.advancedStuff.network;

import io.netty.buffer.ByteBuf;

public interface IPacket {

    public void readData(ByteBuf data);
    public void writeData(ByteBuf data);

}
