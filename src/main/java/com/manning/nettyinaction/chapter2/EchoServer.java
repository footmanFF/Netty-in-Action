package com.manning.nettyinaction.chapter2;

import java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Listing 2.3  of <i>Netty in Action</i>
 *
 * @author <a href="mailto:nmaurer@redhat.com">Norman Maurer</a>
 */
public class EchoServer {

    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public void start() throws Exception {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(group)
                     .channel(NioServerSocketChannel.class)
                     .localAddress(new InetSocketAddress(port))
                     .childHandler(new ChannelInitializer<SocketChannel>() {
                         @Override
                         public void initChannel(SocketChannel channel) throws Exception {
                             channel.pipeline().addLast(new EchoServerHandler());
                         }
                     });
            ChannelFuture f = bootstrap.bind().sync();
            System.out.println(EchoServer.class.getName() + " started and listen on " + f.channel().localAddress());
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws Exception {
        int port = 8081;
        new EchoServer(port).start();
    }
    
}

