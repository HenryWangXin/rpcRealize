package sdk;


import register.RemoteRegister;
import sdk.loadbalance.LoadBalance;
import sdk.protocol.Protocol;
import sdk.protocol.ProtocolFactory;
import spi.SPILoader;

import java.lang.reflect.Proxy;


public class ProxyFactory {

    public static <T> T getProxy(Class<T> interfaceClass) {
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(),new Class[]{interfaceClass},
                (proxy, method, args) -> {

                    Protocol protocal = ProtocolFactory.getProtocol();

                    Invocation invocation = new Invocation(interfaceClass.getName(),method.getName(),method.getParameterTypes(),args);

                    URL url = RemoteRegister.getRandom(interfaceClass.getName());

                    String result = protocal.send(url, invocation);

                    return result;
                });
    }

    public static <T> T getProxy(Class<T> interfaceClass,Invoker invoker,String loadbalance,int retry) {
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(),new Class[]{interfaceClass},
                (proxy, method, args) -> {
                    Invocation invocation = new Invocation(interfaceClass.getName(),method.getName(),method.getParameterTypes(),args);
                    LoadBalance LOADBALANCE = SPILoader.getExtensionLoader(LoadBalance.class).getExtension(loadbalance);
                    return invoker.doInvoke(invocation, invoker.getDirectory().getInvokers(), LOADBALANCE,retry);

                    //URL url = RemoteRegister.getRandom(interfaceClass.getName());

                    //String result = protocal.send(url, invocation);

                    //return result;
                });
    }
}
