package js25.advancedStuff.network;

import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLIndexedMessageToMessageCodec;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.EnumMap;

public class NetworkHandler extends FMLIndexedMessageToMessageCodec<IPacket> {

    public static EnumMap<Side, FMLEmbeddedChannel> channels;

    public NetworkHandler() {
        //addDiscriminator(0, PacketItemStackUpdate.class);
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, IPacket packet, ByteBuf data) throws Exception {
        packet.writeData(data);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf data, IPacket packet) {
        packet.readData(data);
    }
}
