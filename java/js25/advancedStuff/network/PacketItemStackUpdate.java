package js25.advancedStuff.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;

public class PacketItemStackUpdate implements IPacket {

    public enum Type {
        DEBUGGER_SET_MODE;
    }

    public PacketItemStackUpdate() {}
    public PacketItemStackUpdate(ItemStack itemStack, Type type, Object data) {

    }

    @Override
    public void readBytes(ByteBuf bytes) {

    }

    @Override
    public void writeBytes(ByteBuf bytes) {

    }
}
