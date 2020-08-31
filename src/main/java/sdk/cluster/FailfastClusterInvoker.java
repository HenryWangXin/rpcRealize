package sdk.cluster;

import sdk.Directory;
import sdk.Invocation;
import sdk.Invoker;
import sdk.loadbalance.LoadBalance;

import java.util.List;

/**
 * 快速失败
 * @param <T>
 */
public class FailfastClusterInvoker<T> extends Invoker<T> {

    public FailfastClusterInvoker(Directory directory) {
        super.setDirectory(directory);
    }

    public Object doInvoke(Invocation invocation, List<Invoker<T>> invokers, LoadBalance loadbalance,Integer retry) throws Exception {
        //通过负载均衡选择一个服务提供者
        Invoker<T> invoker = loadbalance.select(invokers);
        try {
            return invoker.invoke(invocation);//执行远程调用
        } catch (Throwable e) {
            throw new Exception("快速失败",e);//出错则抛出异常
        }
    }
}