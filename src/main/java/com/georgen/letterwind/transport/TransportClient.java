package com.georgen.letterwind.transport;

import com.georgen.letterwind.api.LetterwindTopic;
import com.georgen.letterwind.model.transport.TransportEnvelope;
import com.georgen.letterwind.transport.encoding.TransportEnvelopeEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class TransportClient implements AutoCloseable {
    private String host;
    private int port;
    private String topicName;
    private Bootstrap bootstrap;
    private EventLoopGroup workerGroup;

    public TransportClient(String host, int port){
        this.host = host;
        this.port = port;
        this.bootstrap = new Bootstrap();
        this.workerGroup = new NioEventLoopGroup();

        bootstrap
                .group(workerGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(getChannelInitializer());
    }

    public TransportClient(LetterwindTopic topic){
        this(topic.getRemoteHost(), topic.getRemotePort());
        this.topicName = topic.getName();
    }

    public void send(TransportEnvelope envelope) throws InterruptedException {
        ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
        Channel channel = channelFuture.channel();
        channel.writeAndFlush(envelope);
        channel.closeFuture().sync();
    }

    public void shutdown(){
        this.workerGroup.shutdownGracefully();
    }

    @Override
    public void close() throws Exception {
        shutdown();
    }

    private ChannelInitializer<SocketChannel> getChannelInitializer(){
        return new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel channel) throws Exception {
                channel.pipeline().addLast(
                        new TransportEnvelopeEncoder()
                );
            }
        };
    }

    public boolean hasTopic(String topicName){
        if (topicName == null || this.topicName == null) return false;
        return topicName.equals(this.topicName);
    }
}
