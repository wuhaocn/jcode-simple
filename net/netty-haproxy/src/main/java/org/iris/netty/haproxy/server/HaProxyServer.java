package org.iris.netty.haproxy.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.haproxy.HAProxyMessage;
import io.netty.handler.codec.haproxy.HAProxyMessageDecoder;

/**
 * HAProxyServer
 *
 * @author wuhao
 * @createTime 2023-03-09
 */
public class HaProxyServer {
	public static void main(String[] args) throws InterruptedException {
		startServer(18888);
	}

	private static void startServer(int port) throws InterruptedException {
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			System.out.println("HaProxyServer startServer:"+ port);
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					.childHandler(new ServerInitializer());
			b.bind(port).sync().channel().closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
}

class ServerInitializer extends ChannelInitializer<SocketChannel> {
	@Override
	public void initChannel(SocketChannel ch) throws Exception {
		ch.pipeline().addLast(
				new HAProxyMessageDecoder(),
				new SimpleChannelInboundHandler() {
					@Override
					protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
						if (msg instanceof HAProxyMessage) {
							System.out.println("HAProxyMessage message is:" + msg);
						} else if (msg instanceof ByteBuf) {
							System.out.println("ByteBuf message is:" + ByteBufUtil.prettyHexDump((ByteBuf) msg));
						}
					}
				});
	}
}