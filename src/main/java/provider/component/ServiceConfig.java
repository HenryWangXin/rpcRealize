package provider.component;

import sdk.Invoker;
import sdk.URL;
import sdk.protocol.Protocol;
import spi.SPILoader;

/**
 * 服务端 服务导出 核心类
 * @param <T>
 */
public class ServiceConfig<T> {
    //通过 SPILoader 声明一个 protocol 通过动态代理结合SPI，重写 doExport 方法，发布一个服务
    private static final Protocol adaptiveProtocol = SPILoader.getExtensionLoader(Protocol.class).getAdaptiveExtension();
    //具体实现类
    private T ref;
    //接口名
    private String interfaceName;
    //端口号
    private int port;
    //IP地址
    private String host;
    //协议 http or dubbo
    private String protocol;
    //权重
    private int weight;

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
    public T getRef() {
        return ref;
    }

    public void setRef(T ref) {
        this.ref = ref;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
    /**
     *
     */
    public synchronized void doExport(){
        String host = this.getHost();
        int port = this.getPort();
        String protocol = this.getProtocol();
        Object ref = this.getRef();
        //拼接URL
        URL url = new URL(host, port,protocol,weight);
        //初始化 invoker
        Invoker invoker = new Invoker();
        invoker.setRfs(ref.getClass());
        invoker.setUrl(url);
        adaptiveProtocol.export(invoker);
    }
}
