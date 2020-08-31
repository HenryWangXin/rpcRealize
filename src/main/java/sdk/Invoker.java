package sdk;

import sdk.loadbalance.LoadBalance;
import sdk.transport.Transport;

import java.util.List;

public class Invoker<T> {
    private URL url;
    private Class<T> rfs;
    private Transport netClinet;
    private Directory directory;
    public Directory getDirectory() {
        return directory;
    }

    public void setDirectory(Directory directory) {
        this.directory = directory;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public Class<T> getRfs() {
        return rfs;
    }

    public void setRfs(Class<T> t) {
        rfs = t;
    }

    public Transport getNetClinet() {
        return netClinet;
    }

    public void setNetClinet(Transport netClinet) {
        this.netClinet = netClinet;
    }

    public Object invoke(Invocation invocation) {
       return netClinet.send(invocation);
    }

    public Object doInvoke(Invocation invocation, List<Invoker<T>> invokers, LoadBalance loadbalance,Integer retry) throws Exception { return null;}
}
