package com.georgen.letterwind.transport;

import com.georgen.letterwind.transport.encoding.TransportEnvelopeDecoder;
import com.georgen.letterwind.transport.handlers.IncomingEnvelopeHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class TransportServer {
    private int port;
    private ServerBootstrap bootstrap;
    private EventLoopGroup masterGroup;
    private EventLoopGroup workerGroup;

    public TransportServer(int port){
        this(port, 0);
    }
    public TransportServer(int port, int threadsLimit){
        this.port = port;
        this.bootstrap = new ServerBootstrap();
        this.masterGroup = threadsLimit != 0 ? new NioEventLoopGroup(threadsLimit) : new NioEventLoopGroup();
        this.workerGroup = threadsLimit != 0 ? new NioEventLoopGroup(threadsLimit) : new NioEventLoopGroup();
    }

    public void run(){
        try {
            bootstrap
                    .group(masterGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(getChildHandler())
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            channelFuture.channel().closeFuture().sync();

        } catch (Exception e){
            shutdown();
        } finally {
            shutdown();
        }
    }

    public void shutdown() {
        masterGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

    private ChannelInitializer<SocketChannel> getChildHandler(){
        return new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel channel) throws Exception {
                channel.pipeline().addLast(
                        new TransportEnvelopeDecoder(),
                        new IncomingEnvelopeHandler()
                );
            }
        };
    }
}
