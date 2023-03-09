package org.iris.netty.haproxy.proxy;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.haproxy.*;
import io.netty.util.CharsetUtil;

/**
 * HaProxyClient
 * @author wuhao
 * @createTime 2023-03-09
 */
public class HaProxyClient {
	public static void main(String[] args) throws Exception {
		String host = "127.0.0.1";
		int port = 18888;
		startClient(host, port);
	}

	private static void startClient(String host, int port) throws InterruptedException {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group)
					.channel(NioSocketChannel.class)
					.handler(new ClientHandler());

			Channel ch = b.connect(host, port).sync().channel();

			HAProxyMessage message = new HAProxyMessage(
					HAProxyProtocolVersion.V2, HAProxyCommand.PROXY, HAProxyProxiedProtocol.TCP4,
					"127.0.0.1", "127.0.0.2", 8000, 9000);
			ch.writeAndFlush(message).sync();
			ch.writeAndFlush(Unpooled.copiedBuffer("this is a proxy protocol message!", CharsetUtil.UTF_8)).sync();
			ch.close().sync();
		} finally {
			group.shutdownGracefully();
		}
	}


}
class ClientHandler extends ChannelOutboundHandlerAdapter {

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		ctx.pipeline().addBefore(ctx.name(), null, HAProxyMessageEncoder.INSTANCE);
		super.handlerAdded(ctx);
	}

	@Override
	public void write(final ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		ChannelFuture future1 = ctx.write(msg, promise);
		if (msg instanceof HAProxyMessage) {
			future1.addListener((ChannelFutureListener) future2 -> {
				if (future2.isSuccess()) {
					ctx.pipeline().remove(HAProxyMessageEncoder.INSTANCE);
					ctx.pipeline().remove(ClientHandler.this);
				} else {
					ctx.close();
				}
			});
		}
	}
}