package org.iris.netty.haproxy.client;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ChannelInputShutdownReadComplete;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.incubator.codec.quic.QuicChannel;
import io.netty.incubator.codec.quic.QuicClientCodecBuilder;
import io.netty.incubator.codec.quic.QuicSslContext;
import io.netty.incubator.codec.quic.QuicSslContextBuilder;
import io.netty.incubator.codec.quic.QuicStreamChannel;
import io.netty.incubator.codec.quic.QuicStreamType;
import io.netty.util.CharsetUtil;
import io.netty.util.NetUtil;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * quic客户端
 * author heliang
 */
public class QuicClient {

    public static void main(String[] args)throws Exception {
        QuicSslContext context = QuicSslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).
                applicationProtocols("http/0.9").build();
        NioEventLoopGroup group = new NioEventLoopGroup(1);
        try {
            ChannelHandler codec = new QuicClientCodecBuilder()
                    .sslContext(context)
                    .maxIdleTimeout(5000, TimeUnit.MILLISECONDS)
                    .initialMaxData(10000000)
                    // 由于我们不想支持远程初始化流，只需设置本地初始化的限制
                    // 本例中的流。
                    .initialMaxStreamDataBidirectionalLocal(1000000)
                    .build();

            Bootstrap bs = new Bootstrap();
            Channel channel = bs.group(group)
                    .channel(NioDatagramChannel.class)
                    .handler(codec)
                    .bind(0).sync().channel();

            QuicChannel quicChannel = QuicChannel.newBootstrap(channel)
                    .streamHandler(new ChannelInboundHandlerAdapter() {
                        @Override
                        public void channelActive(ChannelHandlerContext ctx) {
                            // 因为我们不允许任何远程启动流，所以我们永远不会看到这个方法被调用。
                            // 也就是说，让我们把它保留在这里，以演示这个句柄将被调用
                            // 对于每个远程启动的流。
                            ctx.close();
                        }
                    })
                    .remoteAddress(new InetSocketAddress(NetUtil.LOCALHOST4, 9999))
                    .connect()
                    .get();

			QuicStreamChannel streamChannel = getQuicStreamChannel(quicChannel);
            //写入数据并发送FIN。在此之后，不可能再写入任何数据。
			System.out.println(System.currentTimeMillis() + " "+ ". writeAndFlush={} ");
			streamChannel.writeAndFlush(Unpooled.copiedBuffer("GET /\r\n", CharsetUtil.US_ASCII));


			for (int i = 0; i < 100; i++){
				Thread.sleep(100);
				//streamChannel = getQuicStreamChannel(quicChannel);
				System.out.println(System.currentTimeMillis() + " "+ ". writeAndFlush={} ");
				streamChannel.writeAndFlush(Unpooled.copiedBuffer("GET /\r\n", CharsetUtil.US_ASCII));
			}
            streamChannel.writeAndFlush(Unpooled.copiedBuffer("GET /\r\n", CharsetUtil.US_ASCII))
                    .addListener(QuicStreamChannel.SHUTDOWN_OUTPUT);

            //等待流通道和quic通道关闭(这将在我们收到FIN后发生)。
            //完成此操作后，我们将关闭底层数据报通道。
            streamChannel.closeFuture().sync();
            quicChannel.closeFuture().sync();
            channel.close().sync();
        } finally {
            group.shutdownGracefully();
        }
    }
	
	public static QuicStreamChannel getQuicStreamChannel (QuicChannel quicChannel) throws InterruptedException {
		QuicStreamChannel streamChannel = quicChannel.createStream(QuicStreamType.BIDIRECTIONAL,
				new ChannelInboundHandlerAdapter() {
					@Override
					public void channelRead(ChannelHandlerContext ctx, Object msg) {
						System.out.println(System.currentTimeMillis() + " "+ "channelRead. message={} " + msg);
						ByteBuf byteBuf = (ByteBuf) msg;
						System.err.println(byteBuf.toString(CharsetUtil.UTF_8));
						//byteBuf.release();
					}

					@Override
					public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
						System.out.println(System.currentTimeMillis() + "userEventTriggered. message={} " + evt);
						if (evt == ChannelInputShutdownReadComplete.INSTANCE) {
							//一旦远程对等端发送了此流的FIN，就关闭连接。
							((QuicChannel) ctx.channel().parent()).close(true, 0,
									ctx.alloc().directBuffer(16)
											.writeBytes(new byte[]{'k', 't', 'h', 'x', 'b', 'y', 'e'}));
						}
					}
				}).sync().getNow();
		return streamChannel;
	}
}
