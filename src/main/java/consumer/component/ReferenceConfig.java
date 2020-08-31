package consumer.component;


import com.alibaba.fastjson.JSONObject;
import register.RemoteRegister;
import sdk.Invoker;
import sdk.ProxyFactory;
import sdk.URL;
import sdk.cluster.Cluster;
import sdk.Directory;
import sdk.protocol.Protocol;
import spi.SPILoader;

import java.util.ArrayList;
import java.util.List;

/**
 * 消费端核心类
 * @param <T>
 */
public class ReferenceConfig<T> {
    //通过 SPILoader 声明一个 protocol 通过动态代理结合SPI，重写 refer 方法，为消费端生成一个动态的 Invoker
    private static final Protocol PROTOCOL = SPILoader.getExtensionLoader(Protocol.class).getAdaptiveExtension();
    //通过 SPILoader 声明一个 cluster 通过动态代理结合SPI，重写 join 方法，为消费端生成一个动态的集群容错 Invoker
    private static final Cluster CLUSTER = SPILoader.getExtensionLoader(Cluster.class).getAdaptiveExtension();
    //接口类信息
    private Class<?> interfaceClass;
    //集群容错策略 【set spi 的 key 值】
    protected String cluster;
    //负载均衡策略 【set spi 的 key 值】
    protected String loadbalance;
    //RPC具体调用方法
    protected String method;
    //超时时间
    protected Integer timeout;
    //集群容错之重试次数
    protected Integer retry;
    //具体实现类
    private transient volatile T ref;

    public Class<?> getInterfaceClass() {
        return interfaceClass;
    }

    public void setInterfaceClass(Class<?> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public String getLoadbalance() {
        return loadbalance;
    }

    public void setLoadbalance(String loadbalance) {
        this.loadbalance = loadbalance;
    }

    public T getRef() {
        return ref;
    }

    public void setRef(T ref) {
        this.ref = ref;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Integer getRetry() {
        return retry;
    }

    public void setRetry(Integer retry) {
        this.retry = retry;
    }

    public T get() {
        System.out.println("[wangxin]..." + interfaceClass.getName());
        List<Invoker<?>> invokers = new ArrayList<Invoker<?>>();
        List<URL> urls = RemoteRegister.get(interfaceClass.getName());
        for (URL url : urls) {
            Invoker invoker = PROTOCOL.refer(interfaceClass, url);
            invokers.add(invoker);
        }
        Directory directory = new Directory(invokers);
        System.out.println("[wangxin]..." + JSONObject.toJSONString(invokers));
        Invoker invoker = CLUSTER.join(directory, cluster);

        ref = (T) ProxyFactory.getProxy(this.interfaceClass, invoker, loadbalance, retry);
        return ref;
    }

    public static void main(String[] args) {
        System.out.println("中文");
    }
}
