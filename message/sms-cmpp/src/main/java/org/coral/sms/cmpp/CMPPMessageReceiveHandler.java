package org.coral.sms.cmpp;

import com.zx.sms.BaseMessage;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import org.coral.sms.cmpp.api.smsbiz.MessageReceiveHandler;

public class CMPPMessageReceiveHandler extends MessageReceiveHandler {

	@Override
	protected ChannelFuture reponse( ChannelHandlerContext ctx, Object msg) {
		
		if(msg instanceof BaseMessage) {
			BaseMessage basemsg = (BaseMessage)msg;
			if(basemsg.isRequest())
				return ctx.newSucceededFuture();
		}
		return null;
		
	}

}
