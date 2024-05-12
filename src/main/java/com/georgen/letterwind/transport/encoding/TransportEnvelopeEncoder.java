package com.georgen.letterwind.transport.encoding;

import com.georgen.letterwind.model.transport.TransportEnvelope;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.charset.Charset;

public class TransportEnvelopeEncoder extends MessageToByteEncoder<TransportEnvelope> {
    private static final Charset UTF_8 = Charset.forName("UTF-8");

    @Override
    protected void encode(
            ChannelHandlerContext channelHandlerContext,
            TransportEnvelope envelope,
            ByteBuf output
    ) throws Exception {
        output.writeInt(envelope.getTopicName().length());
        output.writeCharSequence(envelope.getTopicName(), UTF_8);

        output.writeInt(envelope.getMessageTypeName().length());
        output.writeCharSequence(envelope.getMessageTypeName(), UTF_8);

        output.writeInt(envelope.getSerializedMessage().length());
        output.writeCharSequence(envelope.getSerializedMessage(), UTF_8);
    }
}
