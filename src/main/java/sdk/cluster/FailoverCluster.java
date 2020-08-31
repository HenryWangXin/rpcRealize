package sdk.cluster;

import sdk.Directory;
import sdk.Invoker;

/**
 * FailoverCluster简介
 * 失败重试
 * @author wangxin119
 * @date 2020-08-29 23:49
 */
public class FailoverCluster implements Cluster {

    public final static String NAME = "failover";

    @Override
    public <T> Invoker<T> join(Directory directory, String clusterName) {
        return  new FailoverClusterInvoker(directory);
    }
}
