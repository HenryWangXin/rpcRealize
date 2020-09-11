package sdk.cluster;

import sdk.Directory;
import sdk.Invocation;
import sdk.Invoker;
import sdk.loadbalance.LoadBalance;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 失败重试
 * @param <T>
 */
public class FailoverClusterInvoker<T> extends  Invoker<T> {

    public FailoverClusterInvoker(Directory directory) {
        super.setDirectory(directory);
    }

    @Override
    public Object doInvoke(Invocation invocation, List<Invoker<T>> invokers, LoadBalance loadbalance,Integer len)  {
        //所有服务提供者
        List<Invoker<T>> copyInvokers = invokers;
        //重试次数 及 ReferenceConfig 的 retry
        if (len <= 0) {
            len = 1;
        }
        Exception lastExc = null; // last exception.
        List<Invoker<T>> invoked = new ArrayList<Invoker<T>>(copyInvokers.size()); // invoked invokers.
        Set<String> providers = new HashSet<String>(len);
        for (int i = 0; i < len; i++) {//执行 retry 次的调用
            //已经调用过的就不再调用了
            copyInvokers.removeAll(invoked);
            //负载均衡选择一个调用者
            Invoker<T> invoker = loadbalance.select(copyInvokers);
            invoked.add(invoker); //用来保存已经调用的 invoker，已经调用过的就不再调用了
            try {
                Object result = invoker.invoke(invocation);
                return result;//调用成功则 return；跳出循环
            } catch (Throwable e) {//否则记录最后一次调用的 Exception；
                lastExc = new Exception(e.getMessage(), e);
            }

        }
        return lastExc;//返回最后一次的 Exception
    }

}
