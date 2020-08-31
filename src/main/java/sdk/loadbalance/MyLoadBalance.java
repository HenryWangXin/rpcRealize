package sdk.loadbalance;

import sdk.Invoker;

import java.util.List;

/**
 * MyLoadBalance简介
 * 自己测试
 * @author wangxin119
 * @date 2020-08-29 23:56
 */
public class MyLoadBalance implements LoadBalance {
    @Override
    public <T> Invoker<T> select(List<Invoker<T>> invokers) {
        return invokers.get(0);
    }
}
