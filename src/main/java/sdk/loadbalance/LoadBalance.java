package sdk.loadbalance;

import sdk.Invoker;

import java.util.List;

/**
 * LoadBalance简介
 * 负载均衡接口
 * @author wangxin119
 * @date 2020-08-29 23:56
 */
public interface LoadBalance {

    <T> Invoker<T> select(List<Invoker<T>> invokers);

}