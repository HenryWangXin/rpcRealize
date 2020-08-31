package sdk.cluster;

import sdk.Directory;
import sdk.Invocation;
import sdk.Invoker;
import sdk.loadbalance.LoadBalance;

import java.util.List;

public class FailsafeClusterInvoker<T> extends Invoker<T> {

    public FailsafeClusterInvoker(Directory directory) {
        super.setDirectory(directory);
    }

    public Object doInvoke(Invocation invocation, List<Invoker<T>> invokers, LoadBalance loadbalance,Integer retry) throws Exception {
        Invoker<T> invoker = loadbalance.select(invokers);//选一个
        try {
            return invoker.invoke(invocation);// 远程调用
        } catch (Throwable e) {
            return null;//不报错
        }
    }
}