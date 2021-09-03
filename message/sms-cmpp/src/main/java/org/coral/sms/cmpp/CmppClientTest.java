package org.coral.sms.cmpp;

import com.zx.sms.codec.cmpp.msg.CmppSubmitRequestMessage;
import com.zx.sms.connect.manager.EndpointEntity;
import com.zx.sms.connect.manager.EndpointManager;
import com.zx.sms.connect.manager.cmpp.CMPPClientEndpointConnector;
import com.zx.sms.connect.manager.cmpp.CMPPClientEndpointEntity;
import com.zx.sms.handler.api.BusinessHandlerInterface;
import org.marre.sms.SmsAlphabet;
import org.marre.sms.SmsMsgClass;
import org.marre.sms.SmsTextMessage;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.LockSupport;

/**
 * @author wuhao
 * @createTime 2021-08-27 13:14:00
 */
public class CmppClientTest {
	public static void main(String[] args) throws Exception {
		CmppSubmitRequestMessage msg = CmppSubmitRequestMessage.create("18515996076", "18515996076", "");
		msg.setMsgContent(new SmsTextMessage("你好，我是闪信！", SmsAlphabet.UCS2, SmsMsgClass.CLASS_0));  //class0是闪信
		CMPPClientEndpointEntity endpoint = getCMPPEndpoint();

	}
	public static CMPPClientEndpointEntity getCMPPEndpoint() throws Exception {

		final EndpointManager manager = EndpointManager.INS;


		CMPPClientEndpointEntity client = new CMPPClientEndpointEntity();
		client.setId("client");
		client.setHost("117.50.39.33");
		client.setLocalhost("127.0.0.1");
		client.setLocalport(65521);
		client.setPort(8901);
		client.setChartset(Charset.forName("utf-8"));
		client.setGroupName("test");
		client.setUserName("rcloudtest");
		client.setPassword("rcloudtest");

		client.setMaxChannels((short)10);
		client.setVersion((short)0x30);
		client.setRetryWaitTimeSec((short)30);
		client.setUseSSL(false);
		client.setReSendFailMsg(true);
		client.setSupportLongmsg(EndpointEntity.SupportLongMessage.BOTH);
		List<BusinessHandlerInterface> clienthandlers = new ArrayList<BusinessHandlerInterface>();
		clienthandlers.add( new CMPPSessionConnectedHandler(10000));  //在这个handler里发送短信

		client.setBusinessHandlerSet(clienthandlers);
		manager.addEndpointEntity(client);
		manager.openEndpoint(client);
		manager.startConnectionCheckTask();
		return client;

	}
}
