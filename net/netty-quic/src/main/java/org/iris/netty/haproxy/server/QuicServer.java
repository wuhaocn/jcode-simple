package org.iris.netty.haproxy.server;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.incubator.codec.quic.InsecureQuicTokenHandler;
import io.netty.incubator.codec.quic.QuicChannel;
import io.netty.incubator.codec.quic.QuicServerCodecBuilder;
import io.netty.incubator.codec.quic.QuicSslContext;
import io.netty.incubator.codec.quic.QuicSslContextBuilder;
import io.netty.incubator.codec.quic.QuicStreamChannel;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * quic服务端
 * author heliang
 */
public class QuicServer {

    public static void main(String[] args)throws Exception {
        SelfSignedCertificate selfSignedCertificate = new SelfSignedCertificate();
        QuicSslContext context = QuicSslContextBuilder.forServer(
                selfSignedCertificate.privateKey(), null, selfSignedCertificate.certificate())
                .applicationProtocols("http/0.9").build();

        NioEventLoopGroup group = new NioEventLoopGroup(1);
        //创建quic服务端编码器
        ChannelHandler codec = new QuicServerCodecBuilder().sslContext(context)
                .maxIdleTimeout(5000, TimeUnit.MILLISECONDS)
                //最大数量配置
                .initialMaxData(10000000)
                .initialMaxStreamDataBidirectionalLocal(1000000)
                .initialMaxStreamDataBidirectionalRemote(1000000)
                .initialMaxStreamsBidirectional(100)
                .initialMaxStreamsUnidirectional(100)
                // 设置一个令牌处理程序。在生产系统中，您可能希望实现和提供您的定制
                .tokenHandler(InsecureQuicTokenHandler.INSTANCE)
                //添加到QuicChannel管道中的ChannelHandler。
                .handler(new ChannelInboundHandlerAdapter() {
                    @Override
                    public void channelActive(ChannelHandlerContext ctx) {
                        QuicChannel channel = (QuicChannel) ctx.channel();
                    }

                    public void channelInactive(ChannelHandlerContext ctx) {
                        ((QuicChannel) ctx.channel()).collectStats().addListener(new GenericFutureListener(){

                            @Override
                            public void operationComplete(Future future) throws Exception {
                                 if(future.isSuccess())
                                 {
                                     System.out.println("Connection closed:"+future.getNow());
                                 }
                            }
                        });
                    }

                    @Override
                    public boolean isSharable() {
                        return true;
                    }
                })
                .streamHandler(new ChannelInitializer<QuicStreamChannel>() {
                    @Override
                    protected void initChannel(QuicStreamChannel ch)  {
                        // 在这里添加一个LineBasedFrameDecoder，因为我们只是想做一些简单的HTTP 0.9处理。
                        ch.pipeline().addLast(new LineBasedFrameDecoder(1024))
                                .addLast(new ChannelInboundHandlerAdapter() {
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) {
                                        ByteBuf byteBuf = (ByteBuf) msg;
                                        try {
											System.out.println(byteBuf.toString(CharsetUtil.US_ASCII));
//                                            if (byteBuf.toString(CharsetUtil.US_ASCII).trim().equals("GET /")) {
//                                                ByteBuf buffer = ctx.alloc().directBuffer();
//                                                buffer.writeCharSequence("Hello World\r\n", CharsetUtil.UTF_8);
//                                                // 写入缓冲区并通过写入FIN关闭输出。
//                                                ctx.writeAndFlush(buffer);
//                                            }
											ByteBuf buffer = ctx.alloc().directBuffer();
											buffer.writeCharSequence("Hello World\r\n", CharsetUtil.UTF_8);
											// 写入缓冲区并通过写入FIN关闭输出。
											ctx.writeAndFlush(buffer);
                                        } finally {
                                            byteBuf.release();
                                        }
                                    }
                                });
                    }
                }).build();
        try {
            Bootstrap bs = new Bootstrap();
            Channel channel = bs.group(group)
                    .channel(NioDatagramChannel.class)
                    .handler(codec)
                    //设置端口号 9999
                    .bind(new InetSocketAddress(9999)).sync().channel();
            channel.closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }
}
