package com.georgen.letterwind.transport.handlers;

import com.georgen.letterwind.broker.MessageFlow;
import com.georgen.letterwind.model.constants.FlowEvent;
import com.georgen.letterwind.model.transport.TransportEnvelope;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class IncomingEnvelopeHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        TransportEnvelope envelope = (TransportEnvelope) msg;
        if (envelope == null || !envelope.isValid()) return;

        MessageFlow.inform(envelope.toRegularEnvelope(), FlowEvent.RECEPTION);
    }
}
