package sdk.protocol.http;

import sdk.LocalRegister;
import register.RemoteRegister;
import sdk.Invocation;
import sdk.Invoker;
import sdk.URL;
import sdk.protocol.Protocol;
import sdk.transport.Transport;


/**
 * HttpProtocol简介
 *
 * @author wangxin119
 * @date 2020-08-29 23:58
 */
public class HttpProtocol implements Protocol {

    public void start(URL url){
        HttpServer server = new HttpServer();
        server.start(url.getHostname(), url.getPort());
    }

    @Override
    public String send(URL url, Invocation invocation) {
        HttpClient client = new HttpClient();
        return client.send(url.getHostname(), url.getPort(), invocation);
    }

    @Override
    public String testSpi() {
        return "htttp";
    }

    @Override
    public void export(Invoker invoker) {
        try {
            URL url = invoker.getUrl();
            Class clazz = invoker.getRfs();
            //1.本地缓存
            LocalRegister.register(clazz.getName(), clazz.newInstance());
            LocalRegister.register(clazz.getInterfaces()[0].getName(), clazz.newInstance());
            // 2.远程注册：{服务名：List(url)}
            RemoteRegister.register(clazz.getName(), url);
            RemoteRegister.register(clazz.getInterfaces()[0].getName(), url);
            //3.启动
            HttpServer server = new HttpServer();
            server.start(url.getHostname(), url.getPort());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public <T> Invoker<T> refer(Class<T> type, URL url) {
        Invoker invoker = new Invoker();
        invoker.setUrl(url);
        invoker.setRfs(type);
        Transport client = new HttpClient();
        client.start(url.getHostname(),url.getPort());
        invoker.setNetClinet(client);
        return invoker;
    }

}
