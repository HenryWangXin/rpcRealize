package sdk.protocol;

import sdk.protocol.dubbo.DubboProtocol;
import sdk.protocol.http.HttpProtocol;

/**
 * @Author: wanghz
 * @Date: 2020/5/1 6:25 PM
 */
public class ProtocolFactory {

    public static Protocol getProtocol() {
        String name = System.getProperty("protocolName");
        if (name == null || name.equals("")) {
            //name = "http";
            name = "dubbo";
        }

        // 协议扩展，Dubbo使用了SPI机制
        switch(name) {
	        case "http":
                return new HttpProtocol();
	        case "dubbo":
                return new DubboProtocol();
            default:
                break;

        }
        return new HttpProtocol();
    }

}
