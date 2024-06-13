package com.georgen.letterwind.transport.encoding;

import com.georgen.letterwind.model.transport.TransportEnvelope;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.nio.charset.Charset;
import java.util.List;

public class TransportEnvelopeDecoder extends ReplayingDecoder<TransportEnvelope> {
    private static final Charset UTF_8 = Charset.forName("UTF-8");
    @Override
    protected void decode(
            ChannelHandlerContext channelHandlerContext,
            ByteBuf input,
            List<Object> decodedOutputList
    ) throws Exception {
        TransportEnvelope envelope = new TransportEnvelope();

        int idLength = input.readInt();
        CharSequence idChars = input.readCharSequence(idLength, UTF_8);
        if (idChars != null) envelope.setId(idChars.toString());

        int topicNameLength = input.readInt();
        CharSequence topicChars = input.readCharSequence(topicNameLength, UTF_8);
        if (topicChars != null) envelope.setTopicName(topicChars.toString());

        int messageTypeLength = input.readInt();
        CharSequence messageTypeChars = input.readCharSequence(messageTypeLength, UTF_8);
        if (messageTypeChars != null) envelope.setMessageTypeName(messageTypeChars.toString());

        int serializedMessageLength = input.readInt();
        CharSequence serializedMessageChars = input.readCharSequence(serializedMessageLength, UTF_8);
        if (serializedMessageChars != null) envelope.setSerializedMessage(serializedMessageChars.toString());

        decodedOutputList.add(envelope);
    }
}
