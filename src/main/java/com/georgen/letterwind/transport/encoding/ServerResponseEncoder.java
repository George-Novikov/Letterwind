package com.georgen.letterwind.transport.encoding;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class ServerResponseEncoder extends MessageToByteEncoder<Integer> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Integer integer, ByteBuf output) throws Exception {
        output.writeInt(integer);
    }
}
