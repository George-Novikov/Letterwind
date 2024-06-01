package com.georgen.letterwind.transport.handlers;

import com.georgen.letterwind.broker.MessageFlow;
import com.georgen.letterwind.model.broker.Envelope;
import com.georgen.letterwind.model.constants.MessageFlowEvent;
import com.georgen.letterwind.model.constants.Locality;
import com.georgen.letterwind.model.transport.TransportEnvelope;
import com.georgen.letterwind.model.transport.TransportStatus;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class IncomingEnvelopeHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        TransportEnvelope transportEnvelope = (TransportEnvelope) msg;
        if (transportEnvelope == null || !transportEnvelope.isValid()) return;

        Envelope envelope = transportEnvelope.toRegularEnvelope(Locality.REMOTE);

        MessageFlow.push(envelope, MessageFlowEvent.RECEPTION);

        ChannelFuture responseFuture = ctx.writeAndFlush(TransportStatus.OK.getCode());
        responseFuture.addListener(ChannelFutureListener.CLOSE);

        System.out.println("The request reached the server.");
    }
}
